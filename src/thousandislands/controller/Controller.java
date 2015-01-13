package thousandislands.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import thousandislands.model.Feld;
import thousandislands.model.Flaschenpost;
import thousandislands.model.Inventar;
import thousandislands.model.Person;
import thousandislands.model.Spieldaten;
import thousandislands.model.enums.Fortbewegung;
import thousandislands.model.enums.Ladung;
import thousandislands.model.enums.Schiffsteile;
import thousandislands.model.enums.Typ;
import thousandislands.model.enums.Zweck;
import thousandislands.view.Fenster;


public class Controller extends KeyAdapter implements ActionListener {
	private static final int FLASCHENPOST_LAENGE = 50;
	private static final int FLASCHENPOST_ABSTAND = 50;	
	private GuiController gui;
	private Person person;
	private Inventar inventar;
	private Set<Schiffsteile> schiffsteile;
	private int schrittzaehler = 0;
	private boolean flaschenpostSichtbar;
	private Flaschenpost flaschenpost;
	
	public static void main (String[] args) {
		new Controller();
	}
	
	Controller() {
		SpielfeldErsteller ersteller = new SpielfeldErsteller();
		Feld[][] spielfeld = ersteller.getSpielfeld();
		person = new Person(spielfeld[0][0]);
		spielfeld[0][0].setPersonDa(true);
		Spieldaten spieldaten = new Spieldaten(spielfeld, person);
		flaschenpost = new Flaschenpost(spielfeld);
		
		inventar = new Inventar();
		schiffsteile = new HashSet<>();
		
		gui = new GuiController(spieldaten);
		gui.aktualisiere();
		gui.keyListenerHinzufuegen(this);
		gui.actionListenerHinzufuegen(this);
		gui.erstelleSchatzkarte(ersteller.getSchatzkartenanfang());
		gui.zeigeNachricht("Hallo!");
	}
	
	
	@Override
	public void keyReleased(KeyEvent event) {
		boolean bewegt = false;
		
		switch (event.getKeyCode()) {
		case KeyEvent.VK_LEFT: 
			bewegt = person.bewegeNachW();
			break;
		case KeyEvent.VK_RIGHT:
			bewegt = person.bewegeNachO();
			break;
		case KeyEvent.VK_UP:
			bewegt = person.bewegeNachN();
			break;
		case KeyEvent.VK_DOWN:
			bewegt = person.bewegeNachS();
			break;
		}
		
		if (bewegt) {
			bewegungBearbeiten();
		}
	}
	
	private void bewegungBearbeiten() {
		schrittzaehler++;
		person.wasserAbziehen();
		person.nahrungAbziehen();
		gui.aktualisiere();

		//TODO: Nachricht, dass man nur ueber Strand auf Insel kommt?
		//-> Person->setzePersonWeiter() muesste dazu entsprechende Exception werfen

		
		//Flaschenpost erscheinen und verschwinden lassen
		if (!person.hatSchatzkarte()) {
			flaschenpostZeigen();
		}
		
		//Flaschenpost aufnehmen -> Schatzkarte kriegen
		Feld aktuellesFeld = person.getAktuellesFeld();
		if (aktuellesFeld.hatFlaschenpost()) {
			aktuellesFeld.setFlaschenpost(false);
			person.kriegtSchatzkarte();
			gui.kartenknopfSichtbar(true);
			gui.zeigeNachricht("Ich habe die Flaschenpost endlich erwischt!");
		}
		
		//Schatz heben
		if (person.hatSchatzkarte() 
				&& aktuellesFeld.getTyp() == Typ.SCHATZ 
				&& !inventar.enthaelt(Ladung.SEGEL)
				&& !schiffsteile.contains(Schiffsteile.SEGEL)) {
			behandleSchatzfund();
		}

		//Zweckfelder behandeln
		if (aktuellesFeld.getTyp() == Typ.ZWECK) {
			behandleZweckfeld(aktuellesFeld.getZweck());
		}
		
		//Strand mit Holz und Liane -> Floss bauen
		if (!person.hatFloss() && inventar.enthaelt(Ladung.HOLZ)
				&& inventar.enthaelt(Ladung.LIANE)
				&& aktuellesFeld.getTyp() == Typ.STRAND) {
			gui.setzeKnopf("FLOSS_BAUEN");
		}
		
		//TODO: Floss muss am Strand bleiben, waehrend Maennchen rumlaeuft!
		//TODO: Fortbewegungsarten!
//		Feld vorigesFeld = person.getVorigesFeld();
//		if (person.getFortbewegung() == Fortbewegung.FLOSSFAHREN && aktuellesFeld.getTyp() == Typ.STRAND 
//				&& vorigesFeld.getTyp() == Typ.MEER) {
//			//Floss am Strand lassen
//			aktuellesFeld.setFlossDa(true);
//			person.setFortbewegung(Fortbewegung.LAUFEN);
//		} else if (person.getFortbewegung() == Fortbewegung.LAUFEN && aktuellesFeld.istFlossDa() == true) {
//			aktuellesFeld.setFlossDa(false);
//			person.setFortbewegung(Fortbewegung.FLOSSFAHREN);			
//		}		
		
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
		gui.setzeKnopf("SEGEL_MITNEHMEN");
	}
	
	
	private void behandleZweckfeld(Zweck zweck) {		
		switch (zweck) {
		case WASSER:
			gui.zeigeNachricht("Wasser!");
			person.setWasser(person.getMaxWasser());
			if (person.hatFloss() && !person.hatKrug()) {
				gui.zeigeNachricht("Wenn ich einen Krug haette, koennte ich Wasser mitnehmen...");
			}
			
			if (inventar.enthaelt(Ladung.KRUG_LEER)) {
				gui.zeigeNachricht("Ich kann jetzt die Wasservorräte für mein Schiff auffüllen.");
				gui.setzeKnopf("KRUG_FUELLEN");
			}
			break;
			
		case NAHRUNG:
			gui.zeigeNachricht("Fruechte!");
			person.setNahrung(person.getMaxNahrung());
			if (person.hatFloss() && !person.hatKorb()) {
				gui.zeigeNachricht("Wenn ich einen Korb haette, koennte ich Fruechte mitnehmen...");
			}

			if (inventar.enthaelt(Ladung.KORB_LEER)) {
				gui.zeigeNachricht("Ich kann jetzt die Nahrungsvorräte für mein Schiff auffüllen.");
				gui.setzeKnopf("KORB_FUELLEN");
			}
			break;
			

		case HOLZ:
			//TODO: abhängig davon, ob man Holz braucht oder nicht!
			gui.zeigeNachricht("Hier gibt es jede Menge Holz! Und Holz schwimmt gut...");
			gui.setzeKnopf("HOLZ_MITNEHMEN");
			break;
	    
		case LIANEN:
			//TODO: abhängig davon, ob man Lianen braucht oder nicht!
			gui.zeigeNachricht("Lianen! Die kann ich gut als Seile verwenden.");
			gui.setzeKnopf("LIANEN_MITNEHMEN");
			break;
			
		case TON:
			//wenn schon Ton auf Floss oder auf Schiffbauinsel, dann braucht man keinen mehr
			if (inventar.enthaelt(Ladung.TON)
					|| inventar.enthaelt(Ladung.KRUG_LEER)
					|| inventar.enthaelt(Ladung.KRUG_VOLL)
					|| schiffsteile.contains(Schiffsteile.WASSER)) {
				gui.zeigeNachricht("Ich brauche vorerst nicht noch mehr Ton.");
			} else {
				gui.zeigeNachricht("Hier gibt's Ton! Daraus kann ich mir einen Krug fuer mein Wasser formen.");
				gui.setzeKnopf("KRUG_FORMEN");				
			}
			break;
		
		case SCHILF:
			//wenn schon Korb auf Floss oder auf Schiffbauinsel, dann braucht man keinen mehr
			if (inventar.enthaelt(Ladung.KORB_LEER) 
					|| inventar.enthaelt(Ladung.KORB_VOLL)
					|| schiffsteile.contains(Schiffsteile.NAHRUNG)) {
				gui.zeigeNachricht("Ich habe schon einen Korb für meinen Proviant!");
			} else {
				gui.zeigeNachricht("Schilf! Daraus kann ich mir einen Korb für die Früchte flechten.");
				gui.setzeKnopf("KORB_FLECHTEN");			
			}
			break;
		
		case FEUER:
			//im Inventar oder auf Schiffbau-Insel schon Krug vorhanden
			if (inventar.enthaelt(Ladung.KRUG_LEER)
					|| inventar.enthaelt(Ladung.KRUG_VOLL)
					|| schiffsteile.contains(Schiffsteile.WASSER)) {
				gui.zeigeNachricht("Ich muss nicht noch mehr Tongefäße brennen!");
			} else {  //kann noch Krug gebrauchen
				gui.zeigeNachricht("Hier gibt's Feuersteine! Mit denen und den Stöcken, die es hier gibt, "
						+ "kann ich gut ein Feuer machen.");
				gui.setzeKnopf("FEUER_MACHEN");				
			}
			break;
		
		case GROSSER_BAUM:
			//TODO: abhängig von Werkzeug?
			//im Inventar oder auf Schiffbau-Insel schon Mast vorhanden
			if (inventar.enthaelt(Ladung.MAST)
					|| schiffsteile.contains(Schiffsteile.MAST)) {
				gui.zeigeNachricht("Ich brauche nicht noch einen Mast fuer mein Schiff, einer reicht.");
			} else { //wir brauchen Mast
				gui.zeigeNachricht("Hey, der große Baum hier kann ein super Mast fuer mein Schiff werden!");
				gui.setzeKnopf("BAUM_FAELLEN");
			}
			break;
			
		case PAPAYA:
			if (inventar.enthaelt(Ladung.PAPAYA)
					|| person.hatWaffen()
					|| schiffsteile.contains(Schiffsteile.KOMPASS)) {
				gui.zeigeNachricht("Ich glaube, ich brauche wirklich keine Papaya mehr. "
						+ "Können wir jetzt bitte wieder gehen?");
			} else { //wir brauchen Papaya, verdammt
				gui.zeigeNachricht("Bäh, Papaya! Was soll ich denn damit?");
				gui.setzeKnopf("PAPAYA_MITNEHMEN");				
			}
			break;
			
		case RUINE:
			//im Inventar oder auf Schiffbau-Insel schon Kompass vorhanden
			if (inventar.enthaelt(Ladung.KOMPASS)
					|| schiffsteile.contains(Schiffsteile.KOMPASS)) {
				gui.zeigeNachricht("Ich habe mir hier schon alles angeschaut. "
						+ "Hier ist nichts Brauchbares mehr zu finden.");
			} else { //wir brauchen Kompass
				gui.zeigeNachricht("Hier steht ein alter Tempel mitten im Dschungel. "
						+ "Vielleicht finde ich hier etwas, das ich gebrauchen kann.");
				gui.setzeKnopf("RUINEN_DURCHSUCHEN");
			}
			break;
		
		case HUETTE:
			//TODO
			break;
			
		default: break;
	    }
	}	
	
	@Override
	public void actionPerformed(ActionEvent event) {
		
		switch(event.getActionCommand()) {
		case "HOLZ_MITNEHMEN":
			//am Anfang braucht man Holz, um Floss zu bauen
			//TODO: wenn man kein Floss, aber schon Holz hat, sollte man nicht immer wieder welches holen können
			if (!person.hatFloss()) {
				inventar.ladungHinzufuegen(Ladung.HOLZ);
				if (inventar.enthaelt(Ladung.LIANE)) {
					gui.zeigeNachricht("Ich habe Holz und Lianen! Jetzt kann ich mir am Strand ein Floss bauen.");
				} else {
					gui.zeigeNachricht("Ich habe Holz! Wenn ich jetzt noch Lianen finde, kann ich mir ein Floss bauen.");
				}
			} else { //hat Floss
				//wenn schon Holz auf Floss oder auf Schiffbauinsel, dann braucht man keins mehr
				if (inventar.enthaelt(Ladung.HOLZ) || schiffsteile.contains(Schiffsteile.RUMPF)) {
					gui.zeigeNachricht("Ich habe schon genug Holz für mein Schiff!");
					break;
				} else { //man braucht noch Holz fuer Schiffbau
					// Gewicht okay
					if (inventar.getGesamtgewicht() <= person.getTragfaehigkeit()) {
						inventar.ladungHinzufuegen(Ladung.HOLZ);
						gui.zeigeNachricht("Aus diesem Holz kann ich den Rumpf für mein Schiff bauen.");
					} else { //zuviel Gewicht
						gui.zeigeNachricht("Wenn ich das auch noch mitnehme, sinkt mein Floß! "
								+ "Ich muss erstmal etwas an meinem Schiffbauplatz abladen.");
					}
				}
			}			
			break;

		case "LIANEN_MITNEHMEN":
			//am Anfang braucht man Lianen, um Floss zu bauen
			//TODO: wenn man kein Floss, aber schon Lianen hat, sollte man nicht immer wieder welche holen können
			if (!person.hatFloss()) {
				inventar.ladungHinzufuegen(Ladung.LIANE);
				if (inventar.enthaelt(Ladung.HOLZ)) {
					gui.zeigeNachricht("Ich habe Holz und Lianen! Jetzt kann ich mir am Strand ein Floss bauen.");
				} else {
					gui.zeigeNachricht("Ich habe Lianen! Wenn ich jetzt noch Holz finde, kann ich mir ein Floss bauen.");
				}
			} else {  //hat schon Floss
				//wenn schon Lianen auf Floss oder auf Schiffbauinsel, braucht man keine mehr
				if (inventar.enthaelt(Ladung.LIANE) || schiffsteile.contains(Schiffsteile.SEILE)) {
					gui.zeigeNachricht("Ich habe schon genug Lianen für mein Schiff!");
					break;
				} else { //man braucht noch Lianen fuer Schiffbau
					// Gewicht okay
					if (inventar.getGesamtgewicht() <= person.getTragfaehigkeit()) {
						inventar.ladungHinzufuegen(Ladung.LIANE);
						gui.zeigeNachricht("Diese Lianen kann ich gut als Taue für mein Schiff benutzen.");
					} else { //zuviel Gewicht
						gui.zeigeNachricht("Wenn ich das auch noch mitnehme, sinkt mein Floß! "
								+ "Ich muss erstmal etwas an meinem Schiffbauplatz abladen.");
					}
				}
			}
			break;
		
		case "KRUG_FUELLEN":
			gui.zeigeNachricht("Nun habe ich Wasser fuer die Ueberfahrt zum Festland");
			inventar.ladungEntfernen(Ladung.KRUG_LEER);
			inventar.ladungHinzufuegen(Ladung.KRUG_VOLL);
			break;
		
		case "KORB_FUELLEN":
			gui.zeigeNachricht("Nun habe ich Proviant fuer die Ueberfahrt zum Festland");
			inventar.ladungEntfernen(Ladung.KORB_LEER);
			inventar.ladungHinzufuegen(Ladung.KORB_VOLL);
			break;
		
		case "KRUG_FORMEN":
			if (!person.hatFloss()) {
				gui.zeigeNachricht("Wie soll ich einen Krug beim Schwimmen mitnehmen? "
						+ "Ich brauche erstmal ein Floß!");
			} else {  //hat Floss
				if (!person.hatKrug()) {
					gui.zeigeNachricht("Toll, ich habe einen Krug! Aber ich muss ihn noch brennen...");
					inventar.ladungHinzufuegen(Ladung.TON);
				} else { //hat schon Krug fuer Floss, braucht aber noch einen fuers Schiff
					// Gewicht okay
					if (inventar.getGesamtgewicht() <= person.getTragfaehigkeit()) {
						gui.zeigeNachricht("Toll, ich habe einen Krug fuer mein Wasser "
								+ "fuer die Ueberfahrt zum Festland! Aber ich muss ihn noch brennen...");
						inventar.ladungHinzufuegen(Ladung.TON);
					} else { //zuviel Gewicht
						gui.zeigeNachricht("Wenn ich das auch noch mitnehme, sinkt mein Floß! "
								+ "Ich muss erstmal etwas an meinem Schiffbauplatz abladen.");
					}
				}
			}
			break;
		
		case "KORB_FLECHTEN":
			if (!person.hatFloss()) {
				gui.zeigeNachricht("Wie soll ich den Korb beim Schwimmen mitnehmen? "
						+ "Ich brauche erstmal ein Floß!");
			} else {  //hat Floss
				if (!person.hatKorb()) {
					gui.zeigeNachricht("Super, jetzt kann ich mehr Früchte mitnehmen!");
					person.setKorb(true);
				} else { //hat schon Korb fuer Floss, braucht aber noch einen fuers Schiff
					// Gewicht okay
					if (inventar.getGesamtgewicht() <= person.getTragfaehigkeit()) {
						gui.zeigeNachricht("Jetzt kann ich Proviant fuer die Fahrt zum Festland mitnehmen.");
						inventar.ladungHinzufuegen(Ladung.KORB_LEER);
					} else { //zuviel Gewicht
						gui.zeigeNachricht("Wenn ich das auch noch mitnehme, sinkt mein Floß! "
								+ "Ich muss erstmal etwas an meinem Schiffbauplatz abladen.");
					}
				}
			}
			break;
		
		case "FEUER_MACHEN":
			if (!inventar.enthaelt(Ladung.TON)) {
				gui.zeigeNachricht("Schön. Aber eigentlich ist es hier schon warm genug.");
			} else {
				gui.zeigeNachricht("Mit dem Feuer kann ich meinen Tonkrug brennen.");
				gui.setzeKnopf("KRUG_BRENNEN");
			}
			break;
			
		case "KRUG_BRENNEN":
			if (!person.hatKrug()) {
				gui.zeigeNachricht("Super, jetzt kann ich Wasser mitnehmen!");
				inventar.ladungEntfernen(Ladung.TON);
				person.setKrug(true);
			} else {
				inventar.ladungEntfernen(Ladung.TON);
				inventar.ladungHinzufuegen(Ladung.KRUG_LEER);
				gui.zeigeNachricht("Jetzt kann ich Wasser fuer die Ueberfahrt zum Festland mitnehmen!");
			}			
			break;
			
		case "PAPAYA_MITNEHMEN":
			// Gewicht okay
			if (inventar.getGesamtgewicht() <= person.getTragfaehigkeit()) {
				gui.zeigeNachricht("Ich hasse Papaya.");
				gui.setzeKnopf("TROTZDEM_MITNEHMEN");
			} else { //zuviel Gewicht
				gui.zeigeNachricht("Oh, mein Floß ist zu schwer beladen. "
						+ "Ehe ich die Papaya mitnehmen kann, muss ich erstmal "
						+ "etwas anderes an meinem Schiffbauplatz abladen. Schaaade.");
			}
			break;
	   
		case "TROTZDEM_MITNEHMEN":
			gui.zeigeNachricht("Na toll. Jetzt habe ich Papaya.");
			inventar.ladungHinzufuegen(Ladung.PAPAYA);
			break;

		case "BAUM_FAELLEN":
			// Gewicht okay
			if (inventar.getGesamtgewicht() <= person.getTragfaehigkeit()) {
				gui.zeigeNachricht("Jetzt habe ich einen Mast fuer mein Schiff!");
				inventar.ladungHinzufuegen(Ladung.MAST);
			} else { //zuviel Gewicht
				gui.zeigeNachricht("Wenn ich das auch noch mitnehme, sinkt mein Floß! "
						+ "Ich muss erstmal etwas an meinem Schiffbauplatz abladen.");
			}
			break;
			
		case "FLOSS_BAUEN":
			person.setFloss(true);
			gui.zeigeNachricht("Toll, jetzt habe ich ein Floss, so dass ich schneller vorankomme "
					+ "und mehr Dinge transportieren kann.");
			inventar.ladungEntfernen(Ladung.HOLZ);
			inventar.ladungEntfernen(Ladung.LIANE);
			break;
		
		case "RUINEN_DURCHSUCHEN":
			if (!person.hatWaffen()) {
				gui.zeigeNachricht("Oh nein, in den Ruinen wohnen silberne Panther! "
						+ "Sie sehen friedlich aus, aber ich traue mich da erst rein, "
						+ "wenn ich eine brauchbare Waffe habe.");
			} else {
				gui.zeigeNachricht("Wusste ich's doch: Die Panther sind friedlich. "
						+ "Und ich habe hier tatsächlich etwas Nützliches gefunden: Einen Kompass!");
				gui.setzeKnopf("KOMPASS_MITNEHMEN");
			}
			break;
		
		case "KOMPASS_MITNEHMEN":
			inventar.ladungHinzufuegen(Ladung.KOMPASS);
			//Gewicht ignoriert, da Taschenkompass
			break;
			
		case "SEGEL_MITNEHMEN":
			gui.zeigeNachricht("Jetzt habe ich ein Segel für mein Schiff!");
			inventar.ladungHinzufuegen(Ladung.SEGEL);
			//Gewicht ignoriert, da Feldtyp-Aendern gerade zu kompliziert
			break;
	    
		case "ABLADEN":
			//TODO: auf Schiffbauinsel
			// alles einzeln listen?
			// oder alles auf einmal abladen?
			
		default:
			break;
		}
		
		gui.fokusHolen();
		
		if (event.getActionCommand().equals("PAPAYA_MITNEHMEN")
				|| event.getActionCommand().equals("FEUER_MACHEN")
				|| event.getActionCommand().equals("KOMPASS_MITNEHMEN")) {
			gui.knopfFuerAllesSichtbar(true);
		} else {
			gui.knopfFuerAllesSichtbar(false);			
		}
	}
	
}
