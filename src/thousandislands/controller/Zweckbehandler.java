package thousandislands.controller;

import java.util.Map;

import thousandislands.model.Inventar;
import thousandislands.model.Person;
import thousandislands.model.enums.Aktion;
import thousandislands.model.enums.Ladung;
import thousandislands.model.enums.Zweck;

public class Zweckbehandler {
	private GuiController gui;
	private Person person;
	private Inventar inventar;
	private Map<Ladung,Boolean> noetigeTeile;
		
	Zweckbehandler(GuiController gui, Person person, Inventar inventar, Map<Ladung,Boolean> noetigeTeile) {
		this.gui = gui;
		this.person = person;
		this.inventar = inventar;
		this.noetigeTeile = noetigeTeile;
	}
		
	public void behandleZweckfeld(Zweck zweck) {

		switch (zweck) {
		case WASSER:
			behandleZweckWasser();
			break;		
		case NAHRUNG:
			behandleZweckNahrung();
			break;
		case HOLZ:
			behandleZweckHolz();
			break;
		case LIANEN:
			lianenGefunden();
			break;	
		case TON:
			tonGefunden();
			break;
		case SCHILF:
			schilfGefunden();
			break;
		case FEUER:
			feuerGefunden();
			break;
		case GROSSER_BAUM:
			baumGefunden();
			break;
		case PAPAYA:
			papayaGefunden();
			break;
		case RUINE:
			ruineGefunden();
			break;
		case LEER:
			gui.zeigeNachricht("Auf dieser Insel scheint es gar nichts Interessantes zu geben!");
			break;
			
		default: break;
	    }
	}
	
	private void behandleZweckWasser() {
		person.setWasser(person.getMaxWasser());
		gui.aktualisiere();
		gui.zeigeNachricht("Wasser!");

		if (person.getLevel() == 2) {
			if (!schonVorhanden(Ladung.KRUG)) {
				gui.zeigeNachricht("Wenn ich einen Krug haette, koennte ich Wasser " +
						"fuer die Ueberfahrt zum Festland mitnehmen.");
			} else if (!schonVorhanden(Ladung.WASSER)){
				gui.zeigeNachricht("Ich kann jetzt die Wasservorräte für mein Schiff auffüllen.");
				gui.setzeKnopf(Aktion.KRUG_FUELLEN);
			}
		}		
	}

	private void behandleZweckNahrung() {
		person.setNahrung(person.getMaxNahrung());
		gui.aktualisiere();
		gui.zeigeNachricht("Fruechte!");

		if (person.getLevel() == 2) {
			if (!schonVorhanden(Ladung.KORB)) {
				gui.zeigeNachricht("Wenn ich einen Korb haette, koennte ich Fruechte " +
						"fuer die Ueberfahrt zum Festland mitnehmen.");				
			} else if (!schonVorhanden(Ladung.NAHRUNG)){
				gui.zeigeNachricht("Ich kann jetzt die Nahrungsvorräte für mein Schiff auffüllen.");
				gui.setzeKnopf(Aktion.KORB_FUELLEN);				
			}
		}		
	}

	private void behandleZweckHolz() {
		//Level 1: Holz vorhanden
		if (person.getLevel() == 1 && noetigeTeile.get(Ladung.HOLZ)) {
			gui.zeigeNachricht("Ich habe schon genug Holz.");				
		//Level 2: Holz vorhanden
		} else if (person.getLevel() == 2  && schonVorhanden(Ladung.RUMPF)) {
			gui.zeigeNachricht("Ich habe schon genug Holz für den Schiffsrumpf.");
		} else { //man braucht noch Holz
			gui.zeigeNachricht("Hier gibt es jede Menge Holz! Und Holz schwimmt gut...");
			gui.setzeKnopf(Aktion.HOLZ_MITNEHMEN);				
		}		
	}
	
	private void lianenGefunden() {
		//Level 1: Lianen vorhanden
		if (person.getLevel() == 1 && noetigeTeile.get(Ladung.LIANE)) {
			gui.zeigeNachricht("Ich habe schon genug Lianen.");
		//Level 2: Lianen vorhanden
		} else if (person.getLevel() == 2 && schonVorhanden(Ladung.SEILE)) {
			gui.zeigeNachricht("Ich habe schon genug Seile.");
		} else { //man braucht noch Lianen
			gui.zeigeNachricht("Lianen! Die kann ich gut als Seile verwenden.");
			gui.setzeKnopf(Aktion.LIANEN_MITNEHMEN);				
		}		
	}
	
	private void tonGefunden() {
		//wenn schon Ton vorhanden, dann braucht man keinen mehr
		if (inventar.enthaelt(Ladung.KRUG_UNGEBRANNT)
				|| schonVorhanden(Ladung.KRUG)) {
			gui.zeigeNachricht("Ich brauche vorerst nicht noch mehr Ton.");
		} else {
			gui.zeigeNachricht("Hier gibt's Ton! Daraus kann ich mir einen Krug fuer mein Wasser formen.");
			gui.setzeKnopf(Aktion.KRUG_FORMEN);
		}
	}
	
	private void schilfGefunden() {
		//wenn schon Korb im Inventar oder auf Schiffbauinsel, dann braucht man keinen mehr
		if (schonVorhanden(Ladung.KORB)) {
			gui.zeigeNachricht("Ich habe schon einen Korb für meinen Proviant!");
		} else {
			gui.zeigeNachricht("Schilf! Daraus kann ich mir einen Korb für die Früchte flechten.");
			gui.setzeKnopf(Aktion.KORB_FLECHTEN);
		}
	}
	
	private void feuerGefunden() {
		//im Inventar oder auf Schiffbau-Insel schon Krug vorhanden
		if (schonVorhanden(Ladung.KRUG)) {
			gui.zeigeNachricht("Ich muss nicht noch mehr Tongefäße brennen!");
		} else {  //kann noch Krug gebrauchen
			gui.zeigeNachricht("Hier gibt's Feuersteine! Mit denen und den Stöcken, die es hier gibt, "
					+ "kann ich gut ein Feuer machen.");
			gui.setzeKnopf(Aktion.FEUER_MACHEN);				
		}		
	}
	
	private void baumGefunden() {
		//TODO: abhängig von Werkzeug?
		//Problem: dann muss man Werkzeug dabeihaben, kann es nicht abladen
		
		if (person.getLevel() < 2) {
			gui.zeigeNachricht("Hier steht ein großer Baum.");
		} else {
			gui.zeigeNachricht("Hey, der große Baum hier kann ein super Mast fuer mein Schiff werden!");
			gui.setzeKnopf(Aktion.BAUM_FAELLEN);
		}			
	}

	private boolean schonVorhanden(Ladung ladung) {
		return noetigeTeile.get(ladung) || inventar.enthaelt(ladung);
	}

	private void papayaGefunden() {
		if (person.getLevel() < 2) {
			gui.zeigeNachricht("Hier wachsen jede Menge Papaya. Igitt.");				
		} else {
			if (inventar.enthaelt(Ladung.PAPAYA)
					|| inventar.enthaelt(Ladung.SPEER)) {
				gui.zeigeNachricht("Ich glaube, ich brauche wirklich keine Papaya mehr. "
						+ "Können wir jetzt bitte wieder gehen?");
			} else { //wir brauchen Papaya, verdammt
				gui.zeigeNachricht("Bäh, Papaya! Was soll ich denn damit?");
				gui.setzeKnopf(Aktion.PAPAYA_MITNEHMEN);				
			}
		}
	}
	
	private void ruineGefunden() {
		if (person.getLevel() < 2) {
			gui.zeigeNachricht("Hier steht ein alter Tempel mitten im Dschungel.");		
		} else {
			//Kompass schon vorhanden
			if (noetigeTeile.get(Ladung.KOMPASS)) {
				gui.zeigeNachricht("Ich habe mir hier schon alles angeschaut. "
						+ "Hier ist nichts Brauchbares mehr zu finden.");
			} else { //wir brauchen Kompass
				gui.zeigeNachricht("Hier steht ein alter Tempel mitten im Dschungel. "
						+ "Vielleicht finde ich hier etwas, das ich gebrauchen kann.");
				gui.setzeKnopf(Aktion.RUINEN_DURCHSUCHEN);
			}				
		}
	}
}
