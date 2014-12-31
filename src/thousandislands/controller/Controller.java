package thousandislands.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;

import thousandislands.model.Feld;
import thousandislands.model.Inventar;
import thousandislands.model.Person;
import thousandislands.model.Spieldaten;
import thousandislands.model.enums.Ladung;
import thousandislands.model.enums.Schiffsteile;
import thousandislands.model.enums.Typ;
import thousandislands.model.enums.Zweck;
import thousandislands.view.GUI;


public class Controller extends KeyAdapter implements ActionListener {
	private GUI gui;
	private Person person;
	private Inventar inventar;
	private Set<Schiffsteile> schiffsteile;
	
	public static void main (String[] args) {
		new Controller();
	}
	
	Controller() {
		Feld[][] spielfeld = new SpielfeldErsteller().getSpielfeld();
		person = new Person(spielfeld[0][0]);
		spielfeld[0][0].setPersonDa(true);
		Spieldaten spieldaten = new Spieldaten(spielfeld, person);
		
		inventar = new Inventar();
		schiffsteile = new HashSet<>();
		
		gui = new GUI(spieldaten);
		gui.aktualisiere();
		gui.addKeyListener(this);
		gui.actionListenerHinzufuegen(this);
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
		//TODO: Nachricht, dass man nur ueber Strand auf Insel kommt?
		if (bewegt) {
			person.wasserAbziehen();
			person.nahrungAbziehen();
			gui.aktualisiere();
		}
		
		Feld aktuellesFeld = person.getAktuellesFeld();
		if (aktuellesFeld.getTyp() == Typ.ZWECK) {
			behandleZweckfeld(aktuellesFeld.getZweck());
		}
		if (person.hatSchatzkarte() && aktuellesFeld.getTyp() == Typ.SCHATZ) {
			behandleSchatzfund();
		}
		if (aktuellesFeld.hatFlaschenpost()) {
			person.kriegtSchatzkarte();
		}
		
		//Strand mit Holz und Liane -> Floss bauen
		if (!person.hatFloss() && inventar.enthaelt(Ladung.HOLZ)
				&& inventar.enthaelt(Ladung.LIANE)
				&& aktuellesFeld.getTyp() == Typ.STRAND) {
			gui.setzeKnopf("FLOSS_BAUEN");
		}
		
		//TODO: Floss muss am Strand bleiben, waehrend Maennchen rumlaeuft, oder?
		
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
			gui.zeigeNachricht("Hier gibt es jede Menge Holz! Und Holz schwimmt gut...");
			gui.setzeKnopf("HOLZ_MITNEHMEN");
			
//			//am Anfang braucht man Holz, um Floss zu bauen
//			if (!person.hatFloss()) {
//				inventar.ladungHinzufuegen(Ladung.HOLZ);
//				if (inventar.enthaelt(Ladung.LIANE)) {
//					gui.zeigeNachricht("Ich habe Holz und Lianen! Jetzt kann ich mir am Strand ein Floss bauen.");
//				} else {
//					gui.zeigeNachricht("Ich habe Holz! Wenn ich jetzt noch Lianen finde, kann ich mir ein Floss bauen.");
//				}
//			} else { //hat Floss
//				//wenn schon Holz auf Floss oder auf Schiffbauinsel, dann braucht man keins mehr
//				if (inventar.enthaelt(Ladung.HOLZ) || schiffsteile.contains(Schiffsteile.RUMPF)) {
//					gui.zeigeNachricht("Ich habe schon genug Holz für mein Schiff!");
//					break;
//				} else {
//					//man braucht noch Holz fuer Schiffbau
//					gui.zeigeNachricht("Aus diesem Holz kann ich den Rumpf für mein Schiff bauen.");
//					gui.setzeKnopf("HOLZ_MITNEHMEN");
//				}
//			}
			
			break;
	    
		case LIANEN:
			gui.zeigeNachricht("Lianen! Die kann ich gut als Seile verwenden.");
			gui.setzeKnopf("LIANEN_MITNEHMEN");
			
//			//am Anfang braucht man Lianen, um Floss zu bauen
//			if (!person.hatFloss()) {
//				inventar.ladungHinzufuegen(Ladung.LIANE);
//				if (inventar.enthaelt(Ladung.HOLZ)) {
//					gui.zeigeNachricht("Ich habe Holz und Lianen! Jetzt kann ich mir am Strand ein Floss bauen.");
//				} else {
//					gui.zeigeNachricht("Ich habe Lianen! Wenn ich jetzt noch Holz finde, kann ich mir ein Floss bauen.");
//				}
//			} else {  //hat schon Floss
//				//wenn schon Lianen auf Floss oder auf Schiffbauinsel, braucht man keine mehr
//				if (inventar.enthaelt(Ladung.LIANE) || schiffsteile.contains(Schiffsteile.SEILE)) {
//					gui.zeigeNachricht("Ich habe schon genug Lianen für mein Schiff!");
//					break;
//				} else {
//					//man braucht noch Lianen fuer Schiffbau
//					gui.zeigeNachricht("Diese Lianen kann ich gut als Taue für mein Schiff benutzen.");
//					gui.setzeKnopf("LIANEN_MITNEHMEN");
//				}
//			}
			break;
			
		case TON:
			//wenn schon Ton auf Floss oder auf Schiffbauinsel, dann braucht man keinen mehr
			if (inventar.enthaelt(Ladung.TON)
					|| inventar.enthaelt(Ladung.KRUG_LEER)
					|| inventar.enthaelt(Ladung.KRUG_VOLL)
					|| schiffsteile.contains(Schiffsteile.WASSER)) {
				gui.zeigeNachricht("Ich brauche nicht noch mehr Ton!");
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
			gui.zeigeNachricht("Hey, der große Baum hier kann ein super Mast fuer mein Schiff werden!");
			gui.setzeKnopf("BAUM_FAELLEN");
			//TODO: braucht man ihn noch?
			break;
			
		case PAPAYA:
			if (inventar.enthaelt(Ladung.PAPAYA)
					|| inventar.enthaelt(Ladung.WAFFEN)
					|| schiffsteile.contains(Schiffsteile.KOMPASS)) {
				gui.zeigeNachricht("Ich glaube, ich brauche wirklich keine Papaya mehr. "
						+ "Können wir jetzt bitte wieder gehen?");
			} else { //wir brauchen Papaya, verdammt
				gui.zeigeNachricht("Bäh, Papaya! Was soll ich denn damit?");
				gui.setzeKnopf("PAPAYA_MITNEHMEN");				
			}				
			break;
			
		case RUINE:
			//TODO
			break;
		
		case HUETTE:
			//TODO
			break;
			
		default: break;
	    }
	}	
	
	private void behandleSchatzfund() {
		// TODO: Segel in Inventar (wenn es nicht zu schwer ist!)
		// TODO: Schatzkartenknopf ausgrauen
	}
	
	private int getTragfaehigkeit() {
		if (person.hatFloss()) {
			return 10;
		} else {
			return 1;
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		switch(event.getActionCommand()) {
		case "HOLZ_MITNEHMEN":
			//am Anfang braucht man Holz, um Floss zu bauen
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
					if (inventar.getGesamtgewicht() <= getTragfaehigkeit()) {
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
					if (inventar.getGesamtgewicht() <= getTragfaehigkeit()) {
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
					if (inventar.getGesamtgewicht() <= getTragfaehigkeit()) {
						gui.zeigeNachricht("Toll, ich habe einen Krug fuer mein Wasser "
								+ "fuer die Ueberfahrt zum Festland! Aber ich muss ihn noch brennen...");
						inventar.ladungHinzufuegen(Ladung.KRUG_LEER);
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
					if (inventar.getGesamtgewicht() <= getTragfaehigkeit()) {
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
				person.setKrug(true);
			} else {
				gui.zeigeNachricht("Jetzt kann ich Wasser fuer die Ueberfahrt zum Festland mitnehmen!");
			}			
			break;
			
		case "PAPAYA_MITNEHMEN":
			// Gewicht okay
			if (inventar.getGesamtgewicht() <= getTragfaehigkeit()) {
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

		case "FLOSS_BAUEN":
			person.setFloss(true);
			gui.zeigeNachricht("Toll, jetzt habe ich ein Floss, so dass ich schneller vorankomme "
					+ "und mehr Dinge transportieren kann.");
			break;
	    
		case "ABLADEN":
			//TODO: auf Schiffbauinsel
			// alles einzeln listen?
			// oder alles auf einmal abladen?
			
		default:
			break;
		}
	}

}
