package thousandislands.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.print.Book;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Timer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import thousandislands.model.*;
import thousandislands.model.enums.Aktion;
import thousandislands.model.enums.Ladung;
import thousandislands.model.enums.Richtung;
import thousandislands.model.enums.Typ;
import thousandislands.model.enums.Zweck;


public class Controller extends KeyAdapter implements ActionListener {
	private static final int FLASCHENPOST_LAENGE = 50;
	private static final int FLASCHENPOST_ABSTAND = 50;
	private static final int ZEITTAKT_IN_MS = 200;
	private GuiController gui;
	private Spielfeld spielfeld;
	private Person person;
	private Inventar inventar;
	private Set<Ladung> noetigeTeile;
	private int schrittzaehler = 0;
	private boolean flaschenpostSichtbar;
	private Flaschenpost flaschenpost;
	private Zweckverteiler zweckverteiler;
	private int gedrueckteTaste;
	private Timer timer;
	private Zweckbehandler zweckbehandler;
	private Knopfauswerter knopfauswerter;
	private Spieldaten spieldaten;

	public static void main (String[] args) {
		new Controller();
	}
	
	Controller() {
		spielInitialisieren();
	}
	
	private void spielInitialisieren() {
		SpielfeldErsteller ersteller = new SpielfeldErsteller();
		spielfeld = ersteller.getSpielfeld();
		Feld anfangsfeld = ersteller.getSpielanfang();
		ersteller.versteckeWrack();
		person = new Person(anfangsfeld);
		anfangsfeld.setPersonDa(true);
		spielfeld.setAktuellesFeldPerson(anfangsfeld);
		flaschenpost = new Flaschenpost(spielfeld);

		inventar = new Inventar();
		noetigeTeile = new HashSet<>();
		spieldaten = new Spieldaten(spielfeld, person, inventar, noetigeTeile);
		zweckverteiler = new Zweckverteiler();
				
		timer = new Timer(ZEITTAKT_IN_MS, this);
		timer.setInitialDelay(0);
		timer.setActionCommand("TIMER");
		gui = new GuiController(spielfeld, person, inventar);
		gui.aktualisiere();
		gui.keyListenerHinzufuegen(this);
		gui.actionListenerHinzufuegen(this);
		gui.erstelleSchatzkarte(ersteller.getSchatzkartenanfang());
		gui.zeigeNachricht("Ich sollte erstmal diese Insel, wo ich gestrandet bin, erkunden.");
		
		zweckbehandler = new Zweckbehandler(gui, person, inventar, noetigeTeile);
		knopfauswerter = new Knopfauswerter(gui, person, inventar, noetigeTeile, spielfeld);
	}
	
	@Override
	public void keyPressed(KeyEvent event) {
		gedrueckteTaste = event.getKeyCode();
		timer.start();
	}
 	
	@Override
	public void keyReleased(KeyEvent event) {
		timer.stop();
		gedrueckteTaste = 0;
	}
	
	private void tastendruckAuswerten(){
		boolean bewegt = false;
		
		switch (gedrueckteTaste) {
		case KeyEvent.VK_LEFT: 
			bewegt = spielfeld.setzePersonWeiter(Richtung.WESTEN);
			break;
		case KeyEvent.VK_RIGHT:
			bewegt = spielfeld.setzePersonWeiter(Richtung.OSTEN);
			break;
		case KeyEvent.VK_UP:
			bewegt = spielfeld.setzePersonWeiter(Richtung.NORDEN);
			break;
		case KeyEvent.VK_DOWN:
			bewegt = spielfeld.setzePersonWeiter(Richtung.SUEDEN);
			break;
		}
		
		if (bewegt) {
			bewegungBearbeiten();
		}		
	}
	
	private void bewegungBearbeiten() {
		schrittzaehler++;
		gui.loescheNachrichten();
		gui.knopfFuerAllesSichtbar(false);

		Feld aktuellesFeld = spielfeld.getAktuellesFeldPerson();
		if (!aktuellesFeld.istFlossDa() || schrittzaehler % 2 != 1) {
			person.wasserAbziehen();
			person.nahrungAbziehen();			
		}
		gui.aktualisiere();
		
		if (!aktuellesFeld.getLadung().isEmpty()) {
			StringBuilder text = new StringBuilder("Hier liegt: ");
			for (Ladung teil : aktuellesFeld.getLadung()) {
				text.append("\n" + teil.toString());
			}
			gui.zeigeNachricht(text.toString());
		}

		//TODO: Nachricht, dass man nur ueber Strand auf Insel kommt?
		//-> Person->setzePersonWeiter() muesste dazu entsprechende Exception werfen

		//TODO: wenn Level 0, dann sollte es nicht weitergehen, wenn man nicht bei Eingeborenen war
		
		//Flaschenpost aufnehmen -> Schatzkarte kriegen
		if (aktuellesFeld.hatFlaschenpost()) {
			aktuellesFeld.setFlaschenpost(false);
			person.kriegtSchatzkarte();
			gui.kartenknopfSichtbar(true);
			gui.zeigeNachricht("Ich habe die Flaschenpost endlich erwischt!");
		}

		//Flaschenpost erscheinen und verschwinden lassen
		if (!person.hatSchatzkarte() && person.getLevel() == 2) {
			flaschenpostZeigen();
		}
		
		//Schatz heben
		if (person.hatSchatzkarte() 
				&& aktuellesFeld.getTyp() == Typ.SCHATZ 
				&& noetigeTeile.contains(Ladung.SEGEL)) {
			behandleSchatzfund();
		}

		//Zweckfelder behandeln
		if (aktuellesFeld.getTyp() == Typ.ZWECK) {
			if (aktuellesFeld.getZweck() == Zweck.OFFEN) {
				zweckverteiler.setzeNächstenZweck(aktuellesFeld);
			}
			if (aktuellesFeld.getZweck() == Zweck.HUETTE) {
				redeMitEingeborenen();
			} else {
				zweckbehandler.behandleZweckfeld(aktuellesFeld.getZweck());
			}
		}
		
		if (aktuellesFeld.getTyp() == Typ.WRACK) {
			behandleWrackfund();			
		}
		
		//Strand mit Holz und Liane -> Floss bauen
		if (!person.hatFloss() && inventar.enthaelt(Ladung.HOLZ)
				&& inventar.enthaelt(Ladung.LIANE)
				&& aktuellesFeld.getTyp() == Typ.STRAND) {
			gui.setzeKnopf(Aktion.FLOSS_BAUEN);
		}
		
		//Schiffbaustrand
		if (person.getLevel() == 2 && aktuellesFeld.getTyp() == Typ.SCHIFFBAUSTRAND
				&& !inventar.istLeer()) {
			gui.setzeKnopf(Aktion.ABLADEN);
		}
		
		if (person.getLevel() == 1 && person.hatFloss() 
				&& person.hatKrug() && person.hatKorb()) {
			gui.zeigeNachricht("Ich habe ein Floss, einen Korb und einen Krug! \n" +
					"Ich sollte zu den Eingeborenen zurueckkehren.");
		}
		
		gui.aktualisiere();
	}
	
	private void redeMitEingeborenen() {
		//TODO: richtige Dialoge und Pop-up
		
		if (person.getLevel() == 0) {
			//erste Begegnung: man bekommt Liste mit Flossteilen
			person.setLevel(1);
			gui.zeigeNachricht("Hier ist die Liste, was Du brauchst.");
			fuelleTeileliste(1);
			gui.setzeTeileliste(noetigeTeile);
		} else if (person.getLevel() == 1) {
			if (!noetigeTeile.isEmpty()) {
				//Tipps, was man noch braucht
			} else { //man hat schon alle Teile fuers Floss
				//man bekommt Liste mit Schiffsteilen
				person.setLevel(2);
				gui.zeigeNachricht("Hier ist die Liste, was Du brauchst, um ein Schiff zu bauen.");
				inventar.leeren();
				fuelleTeileliste(2);
				gui.setzeTeileliste(noetigeTeile);
			}					
		} else { //level == 2
			//Papaya gegen Speer tauchen
			if (inventar.enthaelt(Ladung.PAPAYA)) {
				inventar.ladungEntfernen(Ladung.PAPAYA);
				person.setSpeer(true);
				gui.zeigeNachricht("Ich bin endlich die Papaya losgeworden - und habe dafuer einen Speer bekommen! " +
						"Nun kann ich mich auch zu den silbernen Panthern wagen.");
			} else {
				//Tipps, was man noch braucht
			}
		}
	}
	
	private void fuelleTeileliste(int level) {
		if (level == 1) {
			noetigeTeile.add(Ladung.HOLZ);
			noetigeTeile.add(Ladung.LIANE);
			noetigeTeile.add(Ladung.KRUG);
			noetigeTeile.add(Ladung.KORB);
		} else {
			noetigeTeile.add(Ladung.RUMPF);
			noetigeTeile.add(Ladung.MAST);
			noetigeTeile.add(Ladung.KOMPASS);
			noetigeTeile.add(Ladung.KRUG);
			noetigeTeile.add(Ladung.KORB);
			noetigeTeile.add(Ladung.WASSER);
			noetigeTeile.add(Ladung.NAHRUNG);
			noetigeTeile.add(Ladung.SEILE);
			noetigeTeile.add(Ladung.SEGEL);
			noetigeTeile.add(Ladung.WERKZEUG);
		}
	}
	
	private void flaschenpostZeigen() {
		if ( !flaschenpostSichtbar ) {
			if (schrittzaehler % FLASCHENPOST_ABSTAND == 0) {
				flaschenpost.erzeugen();
				flaschenpostSichtbar = true;
			}
		} else {
			if (schrittzaehler % FLASCHENPOST_LAENGE == 0) {
				flaschenpost.entfernen();
				flaschenpostSichtbar = false;
			} else {
			    flaschenpost.bewegen();	
			}
		}
	}

	private void behandleSchatzfund() {
		gui.kartenknopfSichtbar(false);
		gui.zeigeNachricht("Hey, der Schatz ist eine alte Piraten-Notfallkiste! "
				+ "Sie enthält ein Segel, wie praktisch.");
		gui.setzeKnopf(Aktion.SEGEL_MITNEHMEN);
	}
	
	private void behandleWrackfund() {
		//noch im ersten Spielteil
		if (person.getLevel() < 2) {
			gui.zeigeNachricht("Hier liegt ein Schiffswrack im Wasser! "
					+ "Vielleicht kann mir das später noch nützen.");
		} else { //im zweiten Spielteil
			//man hat schon Werkzeug
			if (!noetigeTeile.contains(Ladung.WERKZEUG)) {
				gui.zeigeNachricht("Ich habe das Wrack schon durchsucht. "
						+ "Hier gibt es nichts mehr zu holen.");
			} else {  //man braucht noch Werkzeug
				gui.zeigeNachricht("Hier liegt ein Schiffswrack im Wasser. "
						+ "Vielleicht finde ich dort etwas für mein Schiff!");
				gui.setzeKnopf(Aktion.WRACK_DURCHSUCHEN);
			}			
		}
	}

	private void spielSpeichern() {
		//TODO: JAXB verwenden
		//TODO: spielfeld, person
		//TODO: inventar, hosentasche, noetige Teile
		//TODO: level, schrittzahl, flaschenpost sichtbar

		//TODO: Problem, weil Feld auf Nachbarfelder verweist
		//-> man muesste Spielfeld vielleicht mal umbauen
		//-> Spielfeld sollte Nachbarfelder on the fly berechnen
		//-> dann braucht jedes Feld nur x und y, die ja gegeben sind
		//dann braucht man doch eigene Spielfeldklasse, die Array hat und Nachbarn liefert, oder?
		//koennte die nicht auch Feld von Person und Floss speichern?

		// create JAXB context and instantiate marshaller

		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(Spieldaten.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(spieldaten, System.out);
			m.marshal(spieldaten, new File("spielstand.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}



		//TODO: mit Auswahl, unter welchem Namen man speichern moechte
		//TODO: Warnung, wenn Datei unter diesem Namen schon existiert
		//TODO: automatisch Ordner fuer die Spielstaende anlegen?
//		Path dateipfad = Paths.get("spielstand.txt");
//		Path datei;
//		try {
//			datei = Files.createFile(dateipfad);
//			Files.write(datei, spielstand.getBytes(), StandardOpenOption.WRITE);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		gui.fokusHolen();
	}


	private void spielLaden() {
		JAXBContext context = null;
		Unmarshaller um = null;
		try {
			um = context.createUnmarshaller();
			Spieldaten spieldaten = (Spieldaten) um.unmarshal(new FileReader("spielstand.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
	}
		
	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getActionCommand().equals("TIMER")) {
			tastendruckAuswerten();
		} else if (event.getActionCommand().equals("SPEICHERN")) {
			spielSpeichern();
		} else if (event.getActionCommand().equals("LADEN")) {
			spielLaden();
		} else if (event.getActionCommand().equals("NOCHMAL")) {
			spielInitialisieren();
		} else {
			knopfauswerter.knopfGedrueckt(event.getActionCommand());
		}
	}	
}
