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
import thousandislands.model.enums.Aktion;
import thousandislands.model.enums.Ladung;
import thousandislands.model.enums.Richtung;
import thousandislands.model.enums.Teile;
import thousandislands.model.enums.Typ;
import thousandislands.model.enums.Zweck;


public class Controller extends KeyAdapter implements ActionListener {
	private static final int FLASCHENPOST_LAENGE = 50;
	private static final int FLASCHENPOST_ABSTAND = 50;	
	private GuiController gui;
	private Person person;
	private Inventar inventar;
	private Set<Teile> teile;
	private int schrittzaehler = 0;
	private int level = 0;
	private boolean flaschenpostSichtbar;
	private Flaschenpost flaschenpost;
	private Zweckverteiler zweckverteiler;
	
	public static void main (String[] args) {
		new Controller();
	}
	
	Controller() {
		SpielfeldErsteller ersteller = new SpielfeldErsteller();
		Feld[][] spielfeld = ersteller.getSpielfeld();
		Feld anfangsfeld = ersteller.getSpielanfang();
		ersteller.versteckeWrack();
		person = new Person(anfangsfeld);
		anfangsfeld.setPersonDa(true);
		Spieldaten spieldaten = new Spieldaten(spielfeld, person);
		flaschenpost = new Flaschenpost(spielfeld);
		
		inventar = new Inventar();
		teile = new HashSet<>();
		zweckverteiler = new Zweckverteiler();
		
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
			bewegt = person.bewegeNach(Richtung.WESTEN);
			break;
		case KeyEvent.VK_RIGHT:
			bewegt = person.bewegeNach(Richtung.OSTEN);
			break;
		case KeyEvent.VK_UP:
			bewegt = person.bewegeNach(Richtung.NORDEN);
			break;
		case KeyEvent.VK_DOWN:
			bewegt = person.bewegeNach(Richtung.SUEDEN);
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
		if (!person.hatSchatzkarte() && level == 2) {
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
				&& teile.contains(Teile.SEGEL)) {
			behandleSchatzfund();
		}

		//Zweckfelder behandeln
		if (aktuellesFeld.getTyp() == Typ.ZWECK) {
			if (aktuellesFeld.getZweck() == Zweck.OFFEN) {
				zweckverteiler.setzeNächstenZweck(aktuellesFeld);
			}
			behandleZweckfeld(aktuellesFeld.getZweck());
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
		gui.setzeKnopf(Aktion.SEGEL_MITNEHMEN);
	}
	
	private void behandleWrackfund() {
		//noch im ersten Spielteil
		if (level < 2) {
			gui.zeigeNachricht("Hier liegt ein Schiffswrack im Wasser! "
					+ "Vielleicht kann mir das später noch nützen.");
		} else { //im zweiten Spielteil
			//man hat schon Werkzeug
			if (!inventar.enthaelt(Ladung.WERKZEUG)
					|| teile.contains(Teile.WERKZEUG)) {
				gui.zeigeNachricht("Ich habe das Wrack schon durchsucht. "
						+ "Hier gibt es nichts mehr zu holen.");
			} else {  //man braucht noch Werkzeug
				gui.zeigeNachricht("Hier liegt ein Schiffswrack im Wasser. "
						+ "Vielleicht finde ich dort etwas für mein Schiff!");
				gui.setzeKnopf(Aktion.WRACK_DURCHSUCHEN);
			}			
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
			
			if (inventar.enthaelt(Ladung.KRUG_LEER)) {
				gui.zeigeNachricht("Ich kann jetzt die Wasservorräte für mein Schiff auffüllen.");
				gui.setzeKnopf(Aktion.KRUG_FUELLEN);
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
				gui.setzeKnopf(Aktion.KORB_FUELLEN);
			}
			break;
			

		case HOLZ:
			//Level 1: Holz vorhanden
			if (level == 1 && !teile.contains(Teile.HOLZ)) {
				gui.zeigeNachricht("Ich habe schon genug Holz.");				
			//Level 2: Holz vorhanden
			} else if (level == 2  && !teile.contains(Teile.RUMPF)) {
				gui.zeigeNachricht("Ich habe schon genug Holz für den Schiffsrumpf.");
			} else { //man braucht noch Holz
				gui.zeigeNachricht("Hier gibt es jede Menge Holz! Und Holz schwimmt gut...");
				gui.setzeKnopf(Aktion.HOLZ_MITNEHMEN);				
			}		
			break;
	    
		case LIANEN:
			//Level 1: Lianen vorhanden
			if (level == 1 && !teile.contains(Teile.LIANE)) {
				gui.zeigeNachricht("Ich habe schon genug Lianen.");
			//Level 2: Lianen vorhanden
			} else if (level == 2 && !teile.contains(Teile.SEILE)) {
				gui.zeigeNachricht("Ich habe schon genug Seile.");
			} else { //man braucht noch Lianen
				gui.zeigeNachricht("Lianen! Die kann ich gut als Seile verwenden.");
				gui.setzeKnopf(Aktion.LIANEN_MITNEHMEN);				
			}		
			break;
			
		case TON:
			//wenn schon Ton vorhanden, dann braucht man keinen mehr
			if (inventar.enthaelt(Ladung.TON)
					|| !teile.contains(Teile.KRUG)) {
				gui.zeigeNachricht("Ich brauche vorerst nicht noch mehr Ton.");
			} else {
				gui.zeigeNachricht("Hier gibt's Ton! Daraus kann ich mir einen Krug fuer mein Wasser formen.");
				gui.setzeKnopf(Aktion.KRUG_FORMEN);				
			}
			break;
		
		case SCHILF:
			//wenn schon Korb auf Floss oder auf Schiffbauinsel, dann braucht man keinen mehr
			if (inventar.enthaelt(Ladung.KORB_LEER) 
					|| inventar.enthaelt(Ladung.KORB_VOLL)
					|| !teile.contains(Teile.NAHRUNG)) {
				gui.zeigeNachricht("Ich habe schon einen Korb für meinen Proviant!");
			} else {
				gui.zeigeNachricht("Schilf! Daraus kann ich mir einen Korb für die Früchte flechten.");
				gui.setzeKnopf(Aktion.KORB_FLECHTEN);			
			}
			break;
		
		case FEUER:
			//im Inventar oder auf Schiffbau-Insel schon Krug vorhanden
			if (inventar.enthaelt(Ladung.KRUG_LEER)
					|| inventar.enthaelt(Ladung.KRUG_VOLL)
					|| !teile.contains(Teile.WASSER)) {
				gui.zeigeNachricht("Ich muss nicht noch mehr Tongefäße brennen!");
			} else {  //kann noch Krug gebrauchen
				gui.zeigeNachricht("Hier gibt's Feuersteine! Mit denen und den Stöcken, die es hier gibt, "
						+ "kann ich gut ein Feuer machen.");
				gui.setzeKnopf(Aktion.FEUER_MACHEN);				
			}
			break;
		
		case GROSSER_BAUM:
			//TODO: abhängig von Werkzeug?
			
			if (level < 2) {
				gui.zeigeNachricht("Hier steht ein großer Baum.");
			} else {
				//im Inventar oder auf Schiffbau-Insel schon Mast vorhanden
				if (inventar.enthaelt(Ladung.MAST)
						|| !teile.contains(Teile.MAST)) {
					gui.zeigeNachricht("Ich brauche nicht noch einen Mast fuer mein Schiff, einer reicht.");
				} else { //wir brauchen Mast
					gui.zeigeNachricht("Hey, der große Baum hier kann ein super Mast fuer mein Schiff werden!");
					gui.setzeKnopf(Aktion.BAUM_FAELLEN);
				}				
			}			
			break;
			
		case PAPAYA:
			if (level < 2) {
				gui.zeigeNachricht("Hier wachsen jede Menge Papaya. Igitt.");				
			} else {
				if (inventar.enthaelt(Ladung.PAPAYA)
						|| person.hatWaffen()) {
					gui.zeigeNachricht("Ich glaube, ich brauche wirklich keine Papaya mehr. "
							+ "Können wir jetzt bitte wieder gehen?");
				} else { //wir brauchen Papaya, verdammt
					gui.zeigeNachricht("Bäh, Papaya! Was soll ich denn damit?");
					gui.setzeKnopf(Aktion.PAPAYA_MITNEHMEN);				
				}
			}
			break;
			
		case RUINE:
			if (level < 2) {
				gui.zeigeNachricht("Hier steht ein alter Tempel mitten im Dschungel.");		
			} else {
				//im Inventar oder auf Schiffbau-Insel schon Kompass vorhanden
				if (inventar.enthaelt(Ladung.KOMPASS)
						|| !teile.contains(Teile.KOMPASS)) {
					gui.zeigeNachricht("Ich habe mir hier schon alles angeschaut. "
							+ "Hier ist nichts Brauchbares mehr zu finden.");
				} else { //wir brauchen Kompass
					gui.zeigeNachricht("Hier steht ein alter Tempel mitten im Dschungel. "
							+ "Vielleicht finde ich hier etwas, das ich gebrauchen kann.");
					gui.setzeKnopf(Aktion.RUINEN_DURCHSUCHEN);
				}				
			}
			break;
		
		case HUETTE:
			//TODO: richtige Dialoge und Pop-up
			
			if (level == 0) {
				//erste Begegnung: man bekommt Liste mit Flossteilen
				level = 1;
				gui.zeigeNachricht("Hier ist die Liste, was Du brauchst.");
				fuelleTeileliste();
				gui.setzeTeileliste(teile);
			} else if (level == 1) {
				if (!teile.isEmpty()) {
					//Tipps, was man noch braucht
				} else { //man hat schon alle Teile fuers Floss
					//man bekommt Liste mit Schiffsteilen
					level = 2;
					gui.zeigeNachricht("Hier ist die Liste, was Du brauchst, um ein Schiff zu bauen.");
					inventar.leeren();
					fuelleTeileliste();
					gui.setzeTeileliste(teile);
				}					
			} else { //level == 2			
				//Tipps, was man noch braucht				
			}
			break;
			
		case LEER:
			gui.zeigeNachricht("Auf dieser Insel scheint es gar nichts Interessantes zu geben!");
			break;
			
		default: break;
	    }
	}	
	
	private void fuelleTeileliste() {
		if (level == 1) {
			teile.add(Teile.HOLZ);
			teile.add(Teile.LIANE);
			teile.add(Teile.KRUG);
			teile.add(Teile.KORB);
		} else {
			teile.add(Teile.RUMPF);
			teile.add(Teile.MAST);
			teile.add(Teile.KOMPASS);
			teile.add(Teile.KRUG);
			teile.add(Teile.KORB);
			teile.add(Teile.WASSER);
			teile.add(Teile.NAHRUNG);
			teile.add(Teile.SEILE);
			teile.add(Teile.SEGEL);
			teile.add(Teile.WERKZEUG);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		switch(event.getActionCommand()) {
		case "HOLZ_MITNEHMEN":
			//am Anfang braucht man Holz, um Floss zu bauen
			if (!person.hatFloss()) {
				teile.remove(Teile.HOLZ);
				inventar.ladungHinzufuegen(Ladung.HOLZ);
				gui.markiereTeil(Ladung.HOLZ.toString());
				if (inventar.enthaelt(Ladung.LIANE)) {
					gui.zeigeNachricht("Ich habe Holz und Lianen! Jetzt kann ich mir am Strand ein Floss bauen.");
				} else {
					gui.zeigeNachricht("Ich habe Holz! Wenn ich jetzt noch Lianen finde, kann ich mir ein Floss bauen.");
				}
			} else { //hat Floss
				// Gewicht okay
				if (inventar.getGesamtgewicht() + Ladung.HOLZ.getGewicht() 
						<= person.getTragfaehigkeit()) {
					inventar.ladungHinzufuegen(Ladung.HOLZ);
					teile.remove(Teile.RUMPF);
					gui.markiereTeil(Ladung.HOLZ.toString());
					gui.zeigeNachricht("Aus diesem Holz kann ich den Rumpf für mein Schiff bauen.");
				} else { //zuviel Gewicht
					gui.zeigeNachricht("Wenn ich das auch noch mitnehme, sinkt mein Floß! "
							+ "Ich muss erstmal etwas an meinem Schiffbauplatz abladen.");
				}
			}			
			break;

		case "LIANEN_MITNEHMEN":
			//am Anfang braucht man Lianen, um Floss zu bauen
			if (!person.hatFloss()) {
				teile.remove(Teile.LIANE);
				inventar.ladungHinzufuegen(Ladung.LIANE);
				gui.markiereTeil(Ladung.LIANE.toString());
				if (inventar.enthaelt(Ladung.HOLZ)) {
					gui.zeigeNachricht("Ich habe Holz und Lianen! Jetzt kann ich mir am Strand ein Floss bauen.");
				} else {
					gui.zeigeNachricht("Ich habe Lianen! Wenn ich jetzt noch Holz finde, kann ich mir ein Floss bauen.");
				}
			} else {  //hat schon Floss
				// Gewicht okay
				if (inventar.getGesamtgewicht() + Ladung.LIANE.getGewicht() 
						<= person.getTragfaehigkeit()) {
					inventar.ladungHinzufuegen(Ladung.LIANE);
					teile.remove(Teile.SEILE);
					gui.markiereTeil(Ladung.LIANE.toString());
					gui.zeigeNachricht("Diese Lianen kann ich gut als Taue für mein Schiff benutzen.");
				} else { //zuviel Gewicht
					gui.zeigeNachricht("Wenn ich das auch noch mitnehme, sinkt mein Floß! "
							+ "Ich muss erstmal etwas an meinem Schiffbauplatz abladen.");
				}
			}
			break;
		
		case "KRUG_FUELLEN":
			gui.zeigeNachricht("Nun habe ich Wasser fuer die Ueberfahrt zum Festland");
			inventar.ladungEntfernen(Ladung.KRUG_LEER);
			inventar.ladungHinzufuegen(Ladung.KRUG_VOLL);
			teile.remove(Teile.WASSER);
			break;
		
		case "KORB_FUELLEN":
			gui.zeigeNachricht("Nun habe ich Proviant fuer die Ueberfahrt zum Festland");
			inventar.ladungEntfernen(Ladung.KORB_LEER);
			inventar.ladungHinzufuegen(Ladung.KORB_VOLL);
			teile.remove(Teile.NAHRUNG);
			break;
		
		case "KRUG_FORMEN":
			//TODO: haut das noch hin?
			if (!person.hatFloss()) {
				gui.zeigeNachricht("Wie soll ich einen Krug beim Schwimmen mitnehmen? "
						+ "Ich brauche erstmal ein Floß!");
			} else {  //hat Floss
				if (!person.hatKrug()) {
					gui.zeigeNachricht("Toll, ich habe einen Krug! Aber ich muss ihn noch brennen...");
					inventar.ladungHinzufuegen(Ladung.TON);
				} else { //hat schon Krug fuer Floss, braucht aber noch einen fuers Schiff
					// Gewicht okay
					if (inventar.getGesamtgewicht() + Ladung.TON.getGewicht() 
							<= person.getTragfaehigkeit()) {
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
			//TODO: haut das noch hin?
			if (!person.hatFloss()) {
				gui.zeigeNachricht("Wie soll ich den Korb beim Schwimmen mitnehmen? "
						+ "Ich brauche erstmal ein Floß!");
			} else {  //hat Floss
				if (!person.hatKorb()) {
					gui.zeigeNachricht("Super, jetzt kann ich mehr Früchte mitnehmen!");
					gui.markiereTeil(Teile.KORB.toString());
					teile.remove(Teile.KORB);
					person.setKorb(true);
				} else { //hat schon Korb fuer Floss, braucht aber noch einen fuers Schiff
					// Gewicht okay
					if (inventar.getGesamtgewicht() + Ladung.KORB_LEER.getGewicht() 
							<= person.getTragfaehigkeit()) {
						gui.zeigeNachricht("Jetzt kann ich Proviant fuer die Fahrt zum Festland mitnehmen.");
						inventar.ladungHinzufuegen(Ladung.KORB_LEER);
						teile.remove(Teile.KORB);
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
				gui.setzeKnopf(Aktion.KRUG_BRENNEN);
			}
			break;
			
		case "KRUG_BRENNEN":
			if (!person.hatKrug()) {
				gui.zeigeNachricht("Super, jetzt kann ich Wasser mitnehmen!");
				inventar.ladungEntfernen(Ladung.TON);
				teile.remove(Teile.KRUG);
				person.setKrug(true);
			} else {
				inventar.ladungEntfernen(Ladung.TON);
				inventar.ladungHinzufuegen(Ladung.KRUG_LEER);
				teile.remove(Teile.KRUG);
				gui.zeigeNachricht("Jetzt kann ich Wasser fuer die Ueberfahrt zum Festland mitnehmen!");
			}			
			break;
			
		case "PAPAYA_MITNEHMEN":
			// Gewicht okay
			if (inventar.getGesamtgewicht()  + Ladung.PAPAYA.getGewicht() 
					<= person.getTragfaehigkeit()) {
				gui.zeigeNachricht("Ich hasse Papaya.");
				gui.setzeKnopf(Aktion.TROTZDEM_MITNEHMEN);
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
			if (inventar.getGesamtgewicht()  + Ladung.MAST.getGewicht() 
					<= person.getTragfaehigkeit()) {
				gui.zeigeNachricht("Jetzt habe ich einen Mast fuer mein Schiff!");
				inventar.ladungHinzufuegen(Ladung.MAST);
				teile.remove(Teile.MAST);
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
			person.getAktuellesFeld().setFlossDa(true);
			break;
		
		case "RUINEN_DURCHSUCHEN":
			if (!person.hatWaffen()) {
				gui.zeigeNachricht("Oh nein, in den Ruinen wohnen silberne Panther! "
						+ "Sie sehen friedlich aus, aber ich traue mich da erst rein, "
						+ "wenn ich eine brauchbare Waffe habe.");
			} else {
				gui.zeigeNachricht("Wusste ich's doch: Die Panther sind friedlich. "
						+ "Und ich habe hier tatsächlich etwas Nützliches gefunden: Einen Kompass!");
				gui.setzeKnopf(Aktion.KOMPASS_MITNEHMEN);
			}
			break;
		
		case "KOMPASS_MITNEHMEN":
			// Gewicht okay
			if (inventar.getGesamtgewicht() + Ladung.KOMPASS.getGewicht() 
					<= person.getTragfaehigkeit()) {
				gui.zeigeNachricht("Jetzt habe ich einen Kompass für mein Schiff!");
				inventar.ladungHinzufuegen(Ladung.KOMPASS);
				teile.remove(Teile.KOMPASS);
			} else { //zuviel Gewicht
				gui.zeigeNachricht("Wenn ich das auch noch mitnehme, sinkt mein Floß! "
						+ "Ich muss erstmal etwas an meinem Schiffbauplatz abladen.");
			}
			break;
			
		case "SEGEL_MITNEHMEN":
			gui.zeigeNachricht("Jetzt habe ich ein Segel für mein Schiff!");
			inventar.ladungHinzufuegen(Ladung.SEGEL);
			teile.remove(Teile.SEGEL);
			//Gewicht ignoriert, da Feldtyp-Aendern gerade zu kompliziert
			break;

		case "WRACK_DURCHSUCHEN":
			gui.zeigeNachricht("Im Wrack gibt es nicht viel Brauchbares, "
					+ "aber ich habe immerhin einige Werkzeuge gefunden.");
			gui.setzeKnopf(Aktion.WERKZEUG_MITNEHMEN);
			break;

		case "WERKZEUG_MITNEHMEN":
			// Gewicht okay
			if (inventar.getGesamtgewicht() + Ladung.WERKZEUG.getGewicht() 
					<= person.getTragfaehigkeit()) {
				gui.zeigeNachricht("Jetzt habe ich Werkzeug für den Schiffbau!");
				inventar.ladungHinzufuegen(Ladung.WERKZEUG);
				teile.remove(Teile.WERKZEUG);
			} else { //zuviel Gewicht
				gui.zeigeNachricht("Wenn ich das auch noch mitnehme, sinkt mein Floß! "
						+ "Ich muss erstmal etwas an meinem Schiffbauplatz abladen.");
			}
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
				|| event.getActionCommand().equals("KOMPASS_MITNEHMEN")
				|| event.getActionCommand().equals("WRACK_DURCHSUCHEN")) {
			gui.knopfFuerAllesSichtbar(true);
		} else {
			gui.knopfFuerAllesSichtbar(false);			
		}
	}	
}
