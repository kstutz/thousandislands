package thousandislands.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import thousandislands.model.Inventar;
import thousandislands.model.Person;
import thousandislands.model.enums.Aktion;
import thousandislands.model.enums.Ladung;
import thousandislands.model.enums.Typ;

public class Knopfauswerter {
	private GuiController gui;
	private Person person;
	private Inventar inventar;
	private Set<Ladung> noetigeTeile;

	Knopfauswerter(GuiController gui, Person person, Inventar inventar, Set<Ladung> noetigeTeile) {
		this.gui = gui;
		this.person = person;
		this.inventar = inventar;
		this.noetigeTeile = noetigeTeile;		
	}
	
	public void knopfGedrueckt(String aktion) {
		switch(aktion) {
		case "HOLZ_MITNEHMEN":
			holzMitnehmen();
			break;
		case "LIANEN_MITNEHMEN":
			lianenMitnehmen();
			break;
		case "KRUG_FUELLEN":
			krugFuellen();
			break;
		case "KORB_FUELLEN":
			korbFuellen();
			break;
		case "KRUG_FORMEN":
			krugFormen();
			break;
		case "KORB_FLECHTEN":
			korbFlechten();
			break;
		case "FEUER_MACHEN":
			feuerMachen();
			break;
		case "KRUG_BRENNEN":
			krugBrennen();			
			break;
		case "PAPAYA_MITNEHMEN":
			papayaMitnehmen();
			break;
		case "TROTZDEM_MITNEHMEN":
			trotzdemMitnehmen();
			break;
		case "BAUM_FAELLEN":
			baumFaellen();
			break;
		case "FLOSS_BAUEN":
			flossBauen();
			break;
		case "RUINEN_DURCHSUCHEN":
			ruinenDurchsuchen();
			break;
		case "KOMPASS_MITNEHMEN":
			kompassMitnehmen();
			break;
		case "SEGEL_MITNEHMEN":
			segelMitnehmen();
			break;
		case "WRACK_DURCHSUCHEN":
			wrackDurchsuchen();
			break;
		case "WERKZEUG_MITNEHMEN":
			werkzeugMitnehmen();
			break;
		case "ABLADEN":
			abladen();
			break;
		case "SCHIFF_BAUEN":
			schiffBauen();
			break;			
		default:
			break;
		}
			
		gui.fokusHolen();
		gui.aktualisiere();
	}

	private void holzMitnehmen() {
		//am Anfang braucht man Holz, um Floss zu bauen
		if (!person.hatFloss()) {
			noetigeTeile.remove(Ladung.HOLZ);
			inventar.ladungHinzufuegen(Ladung.HOLZ);
			gui.markiereTeil(Ladung.HOLZ.toString());
			if (inventar.enthaelt(Ladung.LIANE)) {
				gui.zeigeNachricht("Ich habe Holz und Lianen! Jetzt kann ich mir am Strand ein Floss bauen.");
			} else {
				gui.zeigeNachricht("Ich habe Holz! Wenn ich jetzt noch Lianen finde, kann ich mir ein Floss bauen.");
			}
		} else { //hat Floss
			// Gewicht okay
			if (inventar.getGesamtgewicht() + Ladung.RUMPF.getGewicht() 
					<= person.getTragfaehigkeit()) {
				inventar.ladungHinzufuegen(Ladung.RUMPF);
				noetigeTeile.remove(Ladung.RUMPF);
				gui.markiereTeil(Ladung.RUMPF.toString());
				gui.zeigeNachricht("Aus diesem Holz kann ich den Rumpf für mein Schiff bauen.");
			} else { //zuviel Gewicht
				gui.zeigeNachricht("Wenn ich das auch noch mitnehme, sinkt mein Floß! "
						+ "Ich muss erstmal etwas an meinem Schiffbauplatz abladen.");
			}
		}
		gui.knopfFuerAllesSichtbar(false);
	}
	
	private void lianenMitnehmen() {
		//am Anfang braucht man Lianen, um Floss zu bauen
		if (!person.hatFloss()) {
			noetigeTeile.remove(Ladung.LIANE);
			inventar.ladungHinzufuegen(Ladung.LIANE);
			gui.markiereTeil(Ladung.LIANE.toString());
			if (inventar.enthaelt(Ladung.HOLZ)) {
				gui.zeigeNachricht("Ich habe Holz und Lianen! Jetzt kann ich mir am Strand ein Floss bauen.");
			} else {
				gui.zeigeNachricht("Ich habe Lianen! Wenn ich jetzt noch Holz finde, kann ich mir ein Floss bauen.");
			}
		} else {  //hat schon Floss
			// Gewicht okay
			if (inventar.getGesamtgewicht() + Ladung.SEILE.getGewicht() 
					<= person.getTragfaehigkeit()) {
				inventar.ladungHinzufuegen(Ladung.SEILE);
				noetigeTeile.remove(Ladung.SEILE);
				gui.markiereTeil(Ladung.SEILE.toString());
				gui.zeigeNachricht("Diese Lianen kann ich gut als Taue für mein Schiff benutzen.");
			} else { //zuviel Gewicht
				gui.zeigeNachricht("Wenn ich das auch noch mitnehme, sinkt mein Floß! "
						+ "Ich muss erstmal etwas an meinem Schiffbauplatz abladen.");
			}
		}
		gui.knopfFuerAllesSichtbar(false);			
	}

	private void krugFuellen() {
		gui.zeigeNachricht("Nun habe ich Wasser fuer die Ueberfahrt zum Festland");
		inventar.ladungHinzufuegen(Ladung.WASSER);
		gui.markiereTeil(Ladung.WASSER.toString());
		noetigeTeile.remove(Ladung.WASSER);
		gui.knopfFuerAllesSichtbar(false);			
	}
	
	private void korbFuellen() {
		gui.zeigeNachricht("Nun habe ich Proviant fuer die Ueberfahrt zum Festland");
		inventar.ladungHinzufuegen(Ladung.NAHRUNG);
		gui.markiereTeil(Ladung.NAHRUNG.toString());
		noetigeTeile.remove(Ladung.NAHRUNG);
		gui.knopfFuerAllesSichtbar(false);			
	}

	private void krugFormen() {
		if (!person.hatFloss()) {
			gui.zeigeNachricht("Wie soll ich einen Krug beim Schwimmen mitnehmen? "
					+ "Ich brauche erstmal ein Floß!");
		} else {  //hat Floss
			if (person.getLevel() == 1) {
				gui.zeigeNachricht("Toll, ich habe einen Krug! Aber ich muss ihn noch brennen...");
				inventar.ladungHinzufuegen(Ladung.KRUG_UNGEBRANNT);
				gui.hosentascheHinzufuegen(Ladung.KRUG_UNGEBRANNT);
			} else { //level == 2
				// Gewicht okay
				if (inventar.getGesamtgewicht() + Ladung.KRUG_UNGEBRANNT.getGewicht() 
						<= person.getTragfaehigkeit()) {
					gui.zeigeNachricht("Toll, ich habe einen Krug fuer mein Wasser "
							+ "fuer die Ueberfahrt zum Festland! Aber ich muss ihn noch brennen...");
					inventar.ladungHinzufuegen(Ladung.KRUG_UNGEBRANNT);
					gui.hosentascheHinzufuegen(Ladung.KRUG_UNGEBRANNT);
				} else { //zuviel Gewicht
					gui.zeigeNachricht("Wenn ich das auch noch mitnehme, sinkt mein Floß! "
							+ "Ich muss erstmal etwas an meinem Schiffbauplatz abladen.");
				}					
			}
		}
		gui.knopfFuerAllesSichtbar(false);			
	}

	private void korbFlechten() {
		if (!person.hatFloss()) {
			gui.zeigeNachricht("Wie soll ich einen Korb beim Schwimmen mitnehmen? "
					+ "Ich brauche erstmal ein Floß!");
		} else {  //hat Floss
			if (person.getLevel() == 1) {
				gui.zeigeNachricht("Super, jetzt kann ich mehr Früchte mitnehmen!");
				gui.markiereTeil(Ladung.KORB.toString());
				noetigeTeile.remove(Ladung.KORB);
				person.setKorb(true);					
			} else { //level == 2
				// Gewicht okay
				if (inventar.getGesamtgewicht() + Ladung.KORB.getGewicht() 
						<= person.getTragfaehigkeit()) {
					gui.zeigeNachricht("Jetzt kann ich Proviant fuer die Fahrt zum Festland mitnehmen.");
					inventar.ladungHinzufuegen(Ladung.KORB);
					gui.markiereTeil(Ladung.KORB.toString());
					noetigeTeile.remove(Ladung.KORB);
				} else { //zuviel Gewicht
					gui.zeigeNachricht("Wenn ich das auch noch mitnehme, sinkt mein Floß! "
							+ "Ich muss erstmal etwas an meinem Schiffbauplatz abladen.");
				}					
			}
		}
		gui.knopfFuerAllesSichtbar(false);			
	}

	private void feuerMachen() {
		if (!inventar.enthaelt(Ladung.KRUG_UNGEBRANNT)) {
			gui.zeigeNachricht("Schön. Aber eigentlich ist es hier schon warm genug.");
			gui.knopfFuerAllesSichtbar(false);			
		} else {
			gui.zeigeNachricht("Mit dem Feuer kann ich meinen Tonkrug brennen.");
			gui.setzeKnopf(Aktion.KRUG_BRENNEN);
		}
	}

	private void krugBrennen() {
		if (!person.hatKrug()) {
			gui.zeigeNachricht("Super, jetzt kann ich Wasser mitnehmen!");
			inventar.ladungEntfernen(Ladung.KRUG_UNGEBRANNT);
			gui.hosentascheEntfernen(Ladung.KRUG_UNGEBRANNT);
			gui.markiereTeil(Ladung.KRUG.toString());
			noetigeTeile.remove(Ladung.KRUG);
			person.setKrug(true);
		} else {
			inventar.ladungEntfernen(Ladung.KRUG_UNGEBRANNT);
			gui.hosentascheEntfernen(Ladung.KRUG_UNGEBRANNT);
			inventar.ladungHinzufuegen(Ladung.KRUG);
			gui.markiereTeil(Ladung.KRUG.toString());
			noetigeTeile.remove(Ladung.KRUG);
			gui.zeigeNachricht("Jetzt kann ich Wasser fuer die Ueberfahrt zum Festland mitnehmen!");
		}
		gui.knopfFuerAllesSichtbar(false);			
	}

	private void papayaMitnehmen() {
		// Gewicht okay
		if (inventar.getGesamtgewicht()  + Ladung.PAPAYA.getGewicht() 
				<= person.getTragfaehigkeit()) {
			gui.zeigeNachricht("Ich HASSE Papaya.");
			gui.setzeKnopf(Aktion.TROTZDEM_MITNEHMEN);
		} else { //zuviel Gewicht
			gui.zeigeNachricht("Oh, mein Floß ist zu schwer beladen. "
					+ "Ehe ich die Papaya mitnehmen kann, muss ich erstmal "
					+ "etwas anderes an meinem Schiffbauplatz abladen. Schaaade.");
			gui.knopfFuerAllesSichtbar(false);
		}
	}

	private void trotzdemMitnehmen() {
		gui.zeigeNachricht("Na toll. Jetzt habe ich Papaya.");
		inventar.ladungHinzufuegen(Ladung.PAPAYA);
		gui.hosentascheHinzufuegen(Ladung.PAPAYA);
		gui.knopfFuerAllesSichtbar(false);			
	}

	private void baumFaellen() {
		// Gewicht okay
		if (inventar.getGesamtgewicht()  + Ladung.MAST.getGewicht() 
				<= person.getTragfaehigkeit()) {
			gui.zeigeNachricht("Jetzt habe ich einen Mast fuer mein Schiff!");
			inventar.ladungHinzufuegen(Ladung.MAST);
			gui.markiereTeil(Ladung.MAST.toString());
			noetigeTeile.remove(Ladung.MAST);
			gui.setzeBaumstumpf();
		} else { //zuviel Gewicht
			gui.zeigeNachricht("Wenn ich das auch noch mitnehme, sinkt mein Floß! "
					+ "Ich muss erstmal etwas an meinem Schiffbauplatz abladen.");
		}
		gui.knopfFuerAllesSichtbar(false);			
	}

	private void flossBauen() {
		person.setFloss(true);
		gui.zeigeNachricht("Toll, jetzt habe ich ein Floss, so dass ich schneller vorankomme "
				+ "und mehr Dinge transportieren kann.");
		inventar.ladungEntfernen(Ladung.HOLZ);
		inventar.ladungEntfernen(Ladung.LIANE);
		person.getAktuellesFeld().setFlossDa(true);
		gui.knopfFuerAllesSichtbar(false);			
	}

	private void ruinenDurchsuchen() {
		if (!person.hatSpeer()) {
			gui.zeigeNachricht("Oh nein, in den Ruinen wohnen silberne Panther! "
					+ "Sie sehen friedlich aus, aber ich traue mich da erst rein, "
					+ "wenn ich eine brauchbare Waffe habe.");
			gui.knopfFuerAllesSichtbar(false);			
		} else {
			gui.zeigeNachricht("Wusste ich's doch: Die Panther sind friedlich. "
					+ "Und ich habe hier tatsächlich etwas Nützliches gefunden: Einen Kompass!");
			gui.setzeKnopf(Aktion.KOMPASS_MITNEHMEN);
		}
	}

	private void kompassMitnehmen() {
		// Gewicht okay
		if (inventar.getGesamtgewicht() + Ladung.KOMPASS.getGewicht() 
				<= person.getTragfaehigkeit()) {
			gui.zeigeNachricht("Jetzt habe ich einen Kompass für mein Schiff!");
			inventar.ladungHinzufuegen(Ladung.KOMPASS);
			gui.markiereTeil(Ladung.KOMPASS.toString());
			noetigeTeile.remove(Ladung.KOMPASS);
		} else { //zuviel Gewicht
			gui.zeigeNachricht("Wenn ich das auch noch mitnehme, sinkt mein Floß! "
					+ "Ich muss erstmal etwas an meinem Schiffbauplatz abladen.");
		}
		gui.knopfFuerAllesSichtbar(false);			
	}

	private void segelMitnehmen() {
		if (inventar.getGesamtgewicht() + Ladung.SEGEL.getGewicht() 
				<= person.getTragfaehigkeit()) {
			gui.zeigeNachricht("Jetzt habe ich ein Segel für mein Schiff!");
			inventar.ladungHinzufuegen(Ladung.SEGEL);
			gui.markiereTeil(Ladung.SEGEL.toString());
			noetigeTeile.remove(Ladung.SEGEL);
		} else { //zuviel Gewicht
			gui.zeigeNachricht("Wenn ich das auch noch mitnehme, sinkt mein Floß! "
					+ "Ich muss erstmal etwas an meinem Schiffbauplatz abladen.");
		}
		person.getAktuellesFeld().setTyp(Typ.SCHATZ_GEFUNDEN);
		gui.knopfFuerAllesSichtbar(false);			
	}

	private void wrackDurchsuchen() {
		gui.zeigeNachricht("Im Wrack gibt es nicht viel Brauchbares, "
				+ "aber ich habe immerhin einige Werkzeuge gefunden.");
		gui.setzeKnopf(Aktion.WERKZEUG_MITNEHMEN);
	}

	private void werkzeugMitnehmen() {
		// Gewicht okay
		if (inventar.getGesamtgewicht() + Ladung.WERKZEUG.getGewicht() 
				<= person.getTragfaehigkeit()) {
			gui.zeigeNachricht("Jetzt habe ich Werkzeug für den Schiffbau!");
			inventar.ladungHinzufuegen(Ladung.WERKZEUG);
			gui.markiereTeil(Ladung.WERKZEUG.toString());
			noetigeTeile.remove(Ladung.WERKZEUG);
		} else { //zuviel Gewicht
			gui.zeigeNachricht("Wenn ich das auch noch mitnehme, sinkt mein Floß! "
					+ "Ich muss erstmal etwas an meinem Schiffbauplatz abladen.");
		}
		gui.knopfFuerAllesSichtbar(false);			
	}

	private void abladen() {
		StringBuilder text = new StringBuilder();
		List<Ladung> fracht = new ArrayList<>(Arrays.asList(
				Ladung.RUMPF, Ladung.KOMPASS, Ladung.KORB, Ladung.KRUG,
				Ladung.SEILE, Ladung.MAST, Ladung.SEGEL, Ladung.WERKZEUG,
				Ladung.WASSER, Ladung.NAHRUNG));
		List<String> abladeliste = new ArrayList<>();
				
		//TODO entfernen
		System.out.println("Vorher: ");
		inventar.listeAusgeben();

		if (inventar.enthaelt(Ladung.KORB) && !inventar.enthaelt(Ladung.NAHRUNG)) {
			text.append("Ich muss den Korb erst mit Fruechten fuellen, ehe ich ihn abladen kann!\n");
		}

		if (inventar.enthaelt(Ladung.KRUG) && !inventar.enthaelt(Ladung.WASSER)) {
			text.append("Ich muss den Krug erst mit Wasser fuellen, ehe ich ihn abladen kann!\n");
		}

		if (inventar.enthaelt(Ladung.KRUG_UNGEBRANNT)) {
			text.append("Ich muss den Krug erst brennen und mit Wasser fuellen, ehe ich ihn abladen kann!\n");
		}
		
		if (inventar.enthaelt(Ladung.PAPAYA)) {
			text.append("Ich will definitiv keine Papaya mit auf mein Schiff nehmen!\n");			
		}
		
		for (Ladung einzelteil: fracht) {
			if (inventar.enthaelt(einzelteil)) {
				if ((einzelteil == Ladung.KORB && !inventar.enthaelt(Ladung.NAHRUNG)) 
						|| (einzelteil == Ladung.KRUG && !inventar.enthaelt(Ladung.WASSER))) {
					continue;
				}
				inventar.ladungEntfernen(einzelteil);
				person.getAktuellesFeld().ladeTeilAb(einzelteil);
				abladeliste.add(einzelteil.toString());
			}
		}
		
		String abgeladen = String.join(", ", abladeliste);
		
		if (!abgeladen.isEmpty()) {
			text.append("Ich habe abgeladen: ");
			text.append(abgeladen);			
		}
		
		//TODO entfernen
		System.out.println("Nachher: ");
		inventar.listeAusgeben();

		if (noetigeTeile.isEmpty()) {
			text.append("Ich habe alle noetigen Bauteile zusammen, also kann ich endlich mein Schiff bauen!");
			gui.setzeKnopf(Aktion.SCHIFF_BAUEN);
		} else {
			gui.knopfFuerAllesSichtbar(false);
		}
		
		gui.zeigeNachricht(text.toString());		
	}
	
	private void schiffBauen() {
		gui.zeigeNachricht("Mein Schiff ist fertig und ich kann endlich zum Festland segeln! Tschues!");
		gui.spielende();
	}
}
