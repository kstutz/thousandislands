package thousandislands.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import javax.swing.Timer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import thousandislands.model.*;
import thousandislands.model.enums.*;


public class Controller extends KeyAdapter implements ActionListener {
	private static final int ZEITTAKT_IN_MS = 200;
	private GuiController gui;
	private Spielfeld spielfeld;
	private Person person;
	private Inventar inventar;
	private Map<Ladung, Boolean> noetigeTeile;
	private Flaschenpost flaschenpost;
	private int gedrueckteTaste;
	private Timer timer;
	private Zweckbehandler zweckbehandler = new Zweckbehandler(gui, person, inventar, noetigeTeile);
	private Knopfauswerter knopfauswerter;
	private Spiel spiel;

	public static void main (String[] args) {
		new Controller();
	}
	
	Controller() {

		File gespeichertesSpiel = new File(Konfiguration.DATEINAME);
		if (gespeichertesSpiel.exists()) {
			spielstandLaden(gespeichertesSpiel, true);
		} else {
			spielInitialisieren();
		}
	}
	
	private void spielInitialisieren() {
		SpielfeldErsteller ersteller = new SpielfeldErsteller();
		spielfeld = ersteller.getSpielfeld();
		person = new Person();
		spielfeld.setAktuellesFeldPerson(ersteller.getSpielanfang());
		flaschenpost = new Flaschenpost(spielfeld);

		inventar = new Inventar();
		noetigeTeile = new LinkedHashMap<>();
		spiel = new Spiel(spielfeld, person, inventar, noetigeTeile, flaschenpost);

		timer = new Timer(ZEITTAKT_IN_MS, this);
		timer.setInitialDelay(0);
		timer.setActionCommand("TIMER");

		if (gui == null) {
			gui = new GuiController(spielfeld, person, inventar, noetigeTeile);
		} else {
			gui.resetGui(spielfeld, person, inventar, noetigeTeile);
		}

		deckeUmgebungAuf(spielfeld.getAktuellesFeldPerson());
		gui.aktualisiere();

		gui.keyListenerHinzufuegen(this);
		gui.actionListenerHinzufuegen(this);
		gui.erstelleSchatzkarte(spielfeld.getSchatzkartenanfang());
		gui.zeigeNachricht("Ich sollte erstmal diese Insel, wo ich gestrandet bin, erkunden.");

		zweckbehandler = new Zweckbehandler(gui, person, inventar, noetigeTeile);
		knopfauswerter = new Knopfauswerter(gui, person, inventar, noetigeTeile, spielfeld);
		gui.fokusHolen();
	}

	private void initialisieren(Spielfeld spielfeld, Person person, Inventar inventar, Map<Ladung, Boolean> noetigeTeile,
								Flaschenpost flaschenpost, boolean fensterErstellen) {
		this.spielfeld = spielfeld;
		this.person = person;
		this.inventar = inventar;
		this.noetigeTeile = noetigeTeile;
		this.flaschenpost = flaschenpost;
		flaschenpost.setSpielfeld(spielfeld);

		timer = new Timer(ZEITTAKT_IN_MS, this);
		timer.setInitialDelay(0);
		timer.setActionCommand("TIMER");

		if (noetigeTeile == null) {
			noetigeTeile = new LinkedHashMap<>();
		}

		if (fensterErstellen) {
			gui = new GuiController(spielfeld, person, inventar, noetigeTeile);
		} else {
			gui.resetGui(spielfeld, person, inventar, noetigeTeile);
		}

		gui.aktualisiere();
		gui.aktualisiereListen();

		gui.keyListenerHinzufuegen(this);
		gui.actionListenerHinzufuegen(this);
		gui.erstelleSchatzkarte(spielfeld.getSchatzkartenanfang());

		zweckbehandler = new Zweckbehandler(gui, person, inventar, noetigeTeile);
		knopfauswerter = new Knopfauswerter(gui, person, inventar, noetigeTeile, spielfeld);
		gui.fokusHolen();
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
		person.erhoeheSchrittzahl();
		gui.loescheNachrichten();
		gui.knopfFuerAllesSichtbar(false);

//		System.out.println(noetigeTeile.entrySet());
//		inventar.listeAusgeben();

		Feld aktuellesFeld = spielfeld.getAktuellesFeldPerson();
		deckeUmgebungAuf(aktuellesFeld);
		if (person.getSchrittzahl() % 2 != 1) {
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
		//-> Spielfeld->setzePersonWeiter() muesste dazu entsprechende Exception werfen

		//TODO: wenn Level 0, dann sollte es nicht weitergehen, wenn man nicht bei Eingeborenen war
		// Problem wie oben?

		//Flaschenpost aufnehmen -> Schatzkarte kriegen
		if (aktuellesFeld.equals(spielfeld.getFlaschenpostFeld())) {
			spielfeld.setFlaschenpostFeld(null);
			person.kriegtSchatzkarte();
			gui.kartenknopfSichtbar(true);
			gui.zeigeNachricht("Ich habe die Flaschenpost endlich erwischt!");
		}

		//Flaschenpost erscheinen und verschwinden lassen
		if (!person.hatSchatzkarte() && person.getLevel() == 2) {
			flaschenpost.aktualisieren(person.getSchrittzahl());
		}

		//Zweckfelder behandeln
		if (aktuellesFeld.getTyp() == Typ.ZWECK) {
			if (aktuellesFeld.getZweck() == Zweck.OFFEN) {
				aktuellesFeld.setZweck(spielfeld.getNaechsterZweck());
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

		if (person.getLevel() == 1 && person.hatFloss()) {
			gui.zeigeNachricht("Ich habe ein Floss gebaut! \n" +
					"Ich sollte zu den Eingeborenen zurueckkehren.");
		}

		//Schiffbaustrand
		if (aktuellesFeld.getTyp() == Typ.SCHIFFBAUSTRAND) {
			if (person.getLevel() == 2 && !inventar.istLeer()) {
				gui.setzeKnopf(Aktion.ABLADEN);
			}
			if (allesGefunden()) {
				gui.setzeKnopf(Aktion.SCHIFF_BAUEN);
			}
		}

		gui.aktualisiere();
	}

	private void deckeUmgebungAuf(Feld feld) {
		List<Feld> nachbarn = new ArrayList<>(spielfeld.getNachbarn(feld));
		for (Feld nachbar : nachbarn) {
			nachbar.setEntdeckt(true);
		}
	}
	
	private void redeMitEingeborenen() {
		//TODO: richtige Dialoge und Pop-up
		
		if (person.getLevel() == 0) {
			//erste Begegnung: man bekommt Liste mit Flossteilen
			person.setLevel(1);
			gui.zeigeNachricht("Hier ist die Liste, was Du brauchst.");
			fuelleTeileliste();
		} else if (person.getLevel() == 1) {
			if (noetigeTeile.values().contains(false)) {
				//Tipps, was man noch braucht
			} else { //man hat schon alle Teile fuers Floss
				//man bekommt Liste mit Schiffsteilen
				person.setLevel(2);
				gui.zeigeNachricht("Hier ist die Liste, was Du brauchst, um ein Schiff zu bauen.");
				inventar.leeren();
				fuelleTeileliste();
			}
		} else { //level == 2
			//Papaya gegen Speer tauschen
			if (inventar.enthaelt(Ladung.PAPAYA)) {
				inventar.ladungEntfernen(Ladung.PAPAYA);
				inventar.ladungHinzufuegen(Ladung.SPEER);
				gui.zeigeNachricht("Ich bin endlich die Papaya losgeworden - und habe dafuer einen Speer bekommen! " +
						"Nun kann ich mich auch zu den silbernen Panthern wagen.");
			} else {
				//Tipps, was man noch braucht
			}
		}
		gui.aktualisiereListen();
	}
	
	private void fuelleTeileliste() {
		if (person.getLevel() == 1) {
			noetigeTeile.put(Ladung.HOLZ, false);
			noetigeTeile.put(Ladung.LIANE, false);
//			noetigeTeile.add(Ladung.KRUG);
//			noetigeTeile.add(Ladung.KORB);
		} else {
			noetigeTeile.clear();
			noetigeTeile.put(Ladung.KRUG, false);
			noetigeTeile.put(Ladung.WASSER, false);
			noetigeTeile.put(Ladung.KORB, false);
			noetigeTeile.put(Ladung.NAHRUNG, false);
			noetigeTeile.put(Ladung.SEILE, false);
			noetigeTeile.put(Ladung.PLANKEN, false);
			noetigeTeile.put(Ladung.MAST, false);
			noetigeTeile.put(Ladung.SEGEL, false);
			noetigeTeile.put(Ladung.WERKZEUG, false);
			noetigeTeile.put(Ladung.KOMPASS, false);
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
			if (schonGefunden(Ladung.WERKZEUG)) {
				gui.zeigeNachricht("Ich habe das Wrack schon durchsucht. "
						+ "Hier gibt es nichts mehr zu holen.");
			} else {  //man braucht noch Werkzeug
				gui.zeigeNachricht("Hier liegt ein Schiffswrack im Wasser. "
						+ "Vielleicht finde ich dort etwas für mein Schiff!");
				gui.setzeKnopf(Aktion.WRACK_DURCHSUCHEN);
			}			
		}
	}

	private boolean schonGefunden(Ladung ladung) {
		return noetigeTeile.get(ladung) || inventar.enthaelt(ladung);
	}

	private boolean allesGefunden() {
		return person.getLevel() == 2 && !noetigeTeile.values().contains(false);
	}

	private void spielSpeichern() {

		// create JAXB context and instantiate marshaller
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(Spiel.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(spiel, System.out);
			m.marshal(spiel, new File(Konfiguration.DATEINAME));
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


	private void spielstandLaden(File gespeichertesSpiel, boolean fensterErstellen) {
		JAXBContext context = null;
		Unmarshaller um = null;
		try {
			context = JAXBContext.newInstance(Spiel.class);
			um = context.createUnmarshaller();
			spiel = (Spiel) um.unmarshal(new FileReader(gespeichertesSpiel));
			initialisieren(spiel.getSpielfeld(), spiel.getPerson(),
					spiel.getInventar(), spiel.getNoetigeTeile(), spiel.getFlaschenpost(), fensterErstellen);
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
			File gespeichertesSpiel = new File(Konfiguration.DATEINAME);
			if (gespeichertesSpiel.exists()) {
				spielstandLaden(gespeichertesSpiel, false);
			}
		} else if (event.getActionCommand().equals("NOCHMAL")) {
			spielInitialisieren();
		} else if (event.getActionCommand().equals("BEENDEN")) {
			System.exit(0);
		} else {
			knopfauswerter.knopfGedrueckt(event.getActionCommand());
		}
	}	
}
