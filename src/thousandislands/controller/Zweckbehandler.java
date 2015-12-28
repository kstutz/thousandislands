package thousandislands.controller;

import java.util.Set;

import thousandislands.model.Inventar;
import thousandislands.model.Person;
import thousandislands.model.enums.Aktion;
import thousandislands.model.enums.Ladung;
import thousandislands.model.enums.Zweck;

public class Zweckbehandler {
	private GuiController gui;
	private Person person;
	private Inventar inventar;
	private Set<Ladung> noetigeTeile;
		
	Zweckbehandler(GuiController gui, Person person, Inventar inventar, Set<Ladung> noetigeTeile) {
		this.gui = gui;
		this.person = person;
		this.inventar = inventar;
		this.noetigeTeile = noetigeTeile;
	}
		
	public void behandleZweckfeld(Zweck zweck, int level) {

		switch (zweck) {
		case WASSER:
			behandleZweckWasser();
			break;		
		case NAHRUNG:
			behandleZweckNahrung();
			break;
		case HOLZ:
			behandleZweckHolz(level);
			break;
		case LIANEN:
			lianenGefunden(level);
			break;	
		case TON:
			tonGefunden(level);
			break;
		case SCHILF:
			schilfGefunden();
			break;
		case FEUER:
			feuerGefunden();
			break;
		case GROSSER_BAUM:
			baumGefunden(level);
			break;
		case PAPAYA:
			papayaGefunden(level);
			break;
		case RUINE:
			ruineGefunden(level);
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
		if (person.hatFloss() && !person.hatKrug()) {
			gui.zeigeNachricht("Wenn ich einen Krug haette, koennte ich Wasser mitnehmen...");
		}
		
		if (inventar.enthaelt(Ladung.KRUG)) {
			gui.zeigeNachricht("Ich kann jetzt die Wasservorräte für mein Schiff auffüllen.");
			gui.setzeKnopf(Aktion.KRUG_FUELLEN);
		}		
	}

	private void behandleZweckNahrung() {
		person.setNahrung(person.getMaxNahrung());
		gui.aktualisiere();
		gui.zeigeNachricht("Fruechte!");
		if (person.hatFloss() && !person.hatKorb()) {
			gui.zeigeNachricht("Wenn ich einen Korb haette, koennte ich Fruechte mitnehmen...");
		}

		if (inventar.enthaelt(Ladung.KORB)) {
			gui.zeigeNachricht("Ich kann jetzt die Nahrungsvorräte für mein Schiff auffüllen.");
			gui.setzeKnopf(Aktion.KORB_FUELLEN);
		}
	}

	private void behandleZweckHolz(int level) {
		//Level 1: Holz vorhanden
		if (level == 1 && !noetigeTeile.contains(Ladung.HOLZ)) {
			gui.zeigeNachricht("Ich habe schon genug Holz.");				
		//Level 2: Holz vorhanden
		} else if (level == 2  && !noetigeTeile.contains(Ladung.RUMPF)) {
			gui.zeigeNachricht("Ich habe schon genug Holz für den Schiffsrumpf.");
		} else { //man braucht noch Holz
			gui.zeigeNachricht("Hier gibt es jede Menge Holz! Und Holz schwimmt gut...");
			gui.setzeKnopf(Aktion.HOLZ_MITNEHMEN);				
		}		
	}
	
	private void lianenGefunden(int level) {
		//Level 1: Lianen vorhanden
		if (level == 1 && !noetigeTeile.contains(Ladung.LIANE)) {
			gui.zeigeNachricht("Ich habe schon genug Lianen.");
		//Level 2: Lianen vorhanden
		} else if (level == 2 && !noetigeTeile.contains(Ladung.SEILE)) {
			gui.zeigeNachricht("Ich habe schon genug Seile.");
		} else { //man braucht noch Lianen
			gui.zeigeNachricht("Lianen! Die kann ich gut als Seile verwenden.");
			gui.setzeKnopf(Aktion.LIANEN_MITNEHMEN);				
		}		
	}
	
	private void tonGefunden(int level) {
		//wenn schon Ton vorhanden, dann braucht man keinen mehr
		if (inventar.enthaelt(Ladung.KRUG_UNGEBRANNT)
				|| !noetigeTeile.contains(Ladung.KRUG)) {
			gui.zeigeNachricht("Ich brauche vorerst nicht noch mehr Ton.");
		} else {
			gui.zeigeNachricht("Hier gibt's Ton! Daraus kann ich mir einen Krug fuer mein Wasser formen.");
			gui.setzeKnopf(Aktion.KRUG_FORMEN);
		}
	}
	
	private void schilfGefunden() {
		//wenn schon Korb auf Floss oder auf Schiffbauinsel, dann braucht man keinen mehr
		if (!noetigeTeile.contains(Ladung.KORB)) {
			gui.zeigeNachricht("Ich habe schon einen Korb für meinen Proviant!");
		} else {
			gui.zeigeNachricht("Schilf! Daraus kann ich mir einen Korb für die Früchte flechten.");
			gui.setzeKnopf(Aktion.KORB_FLECHTEN);			
		}
	}
	
	private void feuerGefunden() {
		//im Inventar oder auf Schiffbau-Insel schon Krug vorhanden
		if (!noetigeTeile.contains(Ladung.KRUG)) {
			gui.zeigeNachricht("Ich muss nicht noch mehr Tongefäße brennen!");
		} else {  //kann noch Krug gebrauchen
			gui.zeigeNachricht("Hier gibt's Feuersteine! Mit denen und den Stöcken, die es hier gibt, "
					+ "kann ich gut ein Feuer machen.");
			gui.setzeKnopf(Aktion.FEUER_MACHEN);				
		}		
	}
	
	private void baumGefunden(int level) {
		//TODO: abhängig von Werkzeug?
		
		if (level < 2) {
			gui.zeigeNachricht("Hier steht ein großer Baum.");
		} else {
			//im Inventar oder auf Schiffbau-Insel schon Mast vorhanden
			if (!noetigeTeile.contains(Ladung.MAST)) {
				gui.zeigeNachricht("Ich brauche nicht noch einen Mast fuer mein Schiff, einer reicht.");
			} else { //wir brauchen Mast
				gui.zeigeNachricht("Hey, der große Baum hier kann ein super Mast fuer mein Schiff werden!");
				gui.setzeKnopf(Aktion.BAUM_FAELLEN);
			}				
		}			
	}
	
	private void papayaGefunden(int level) {
		if (level < 2) {
			gui.zeigeNachricht("Hier wachsen jede Menge Papaya. Igitt.");				
		} else {
			if (inventar.enthaelt(Ladung.PAPAYA)
					|| person.hatSpeer()) {
				gui.zeigeNachricht("Ich glaube, ich brauche wirklich keine Papaya mehr. "
						+ "Können wir jetzt bitte wieder gehen?");
			} else { //wir brauchen Papaya, verdammt
				gui.zeigeNachricht("Bäh, Papaya! Was soll ich denn damit?");
				gui.setzeKnopf(Aktion.PAPAYA_MITNEHMEN);				
			}
		}
	}
	
	private void ruineGefunden(int level) {
		if (level < 2) {
			gui.zeigeNachricht("Hier steht ein alter Tempel mitten im Dschungel.");		
		} else {
			//Kompass schon vorhanden
			if (!noetigeTeile.contains(Ladung.KOMPASS)) {
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
