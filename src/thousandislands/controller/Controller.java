package thousandislands.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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


public class Controller extends KeyAdapter {
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
		
		gui = new GUI(spieldaten);
		gui.aktualisiere();
		gui.addKeyListener(this);
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
			gui.beschrifteKnopf("Floss bauen");
		}
			
		
	}

	private void behandleZweckfeld(Zweck zweck) {		
		switch (zweck) {
		case WASSER:
			gui.zeigeNachricht("Wasser!");
			person.setWasser(person.getMaxWasser());
			if (person.hatFloss() && !person.hatKrug()) {
				gui.zeigeNachricht("Wenn ich einen Krug haette, koennte ich Wasser mitnehmen...");
			}
			//TODO: Krug fuer Schiff aufgefuellt
			break;
			
		case NAHRUNG:
			gui.zeigeNachricht("Fruechte!");
			person.setNahrung(person.getMaxNahrung());
			if (person.hatFloss() && !person.hatKorb()) {
				gui.zeigeNachricht("Wenn ich einen Korb haette, koennte ich Fruechte mitnehmen...");
			}
			//TODO: Korb fuer Schiff aufgefuellt
			break;
			

		case HOLZ:
			gui.zeigeNachricht("Holz!");
			//wenn schon Holz auf Floss oder auf Schiffbauinsel, dann braucht man keins mehr
			if (inventar.enthaelt(Ladung.HOLZ) || schiffsteile.contains(Schiffsteile.RUMPF)) {
				gui.zeigeNachricht("Ich habe schon genug Holz für mein Schiff!");
				break;
			}
			
			//am Anfang braucht man Holz, um Floss zu bauen
			if (!person.hatFloss()) {
				inventar.ladungHinzufuegen(Ladung.HOLZ);
				if (inventar.enthaelt(Ladung.LIANE)) {
					gui.zeigeNachricht("Ich habe Holz und Lianen! Jetzt kann ich mir am Strand ein Floss bauen.");
				} else {
					gui.zeigeNachricht("Ich habe Holz! Wenn ich jetzt noch Lianen finde, kann ich mir ein Floss bauen.");
				}
			}
			
			//wenn man schon zuviel auf dem Floss hat, kann man das Holz nicht mitnehmen
			if (inventar.getGesamtgewicht() > getTragfaehigkeit()) {
				gui.zeigeNachricht("Zu schwer!");
			} else {
				inventar.ladungHinzufuegen(Ladung.HOLZ);
			}

			break;
	    
		case LIANEN:
			gui.zeigeNachricht("Lianen! Die kann ich gut als Seile verwenden.");
			
			//wenn schon Lianen auf Floss oder auf Schiffbauinsel, braucht man keine mehr
			if (inventar.enthaelt(Ladung.LIANE) || schiffsteile.contains(Schiffsteile.SEILE)) {
				gui.zeigeNachricht("Ich habe schon genug Lianen für mein Schiff!");
				break;
			}

			//am Anfang braucht man Holz, um Floss zu bauen
			if (!person.hatFloss()) {
				inventar.ladungHinzufuegen(Ladung.LIANE);
				if (inventar.enthaelt(Ladung.HOLZ)) {
					gui.zeigeNachricht("Ich habe Holz und Lianen! Jetzt kann ich mir am Strand ein Floss bauen.");
				} else {
					gui.zeigeNachricht("Ich habe Lianen! Wenn ich jetzt noch Holz finde, kann ich mir ein Floss bauen.");
				}
			}
			
			break;
			
			
		default: break;
	    }
	}	
	
	private void behandleSchatzfund() {
		// TODO Auto-generated method stub		
	}
	
	private int getTragfaehigkeit() {
		if (person.hatFloss()) {
			return 10;
		} else {
			return 1;
		}
	}


}
