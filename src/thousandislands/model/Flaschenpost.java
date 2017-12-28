package thousandislands.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import thousandislands.controller.Konfiguration;
import thousandislands.model.enums.Richtung;
import thousandislands.model.enums.Typ;

import javax.xml.bind.annotation.XmlElement;

public class Flaschenpost {
	@XmlElement
	private List<Richtung> richtungen = new ArrayList<>();
	private Spielfeld spielfeld;
	private Random wuerfel = new Random();
	
	public Flaschenpost(Spielfeld spielfeld) {
		this.spielfeld = spielfeld;
	}

	//fuer JAXB
	public Flaschenpost() {}

	public void setSpielfeld(Spielfeld spielfeld) {
		this.spielfeld = spielfeld;
	}

	public void aktualisieren(int schrittzahl) {
		int intervall = Konfiguration.FLASCHENPOST_LAENGE + Konfiguration.FLASCHENPOST_ABSTAND;

		if (schrittzahl % intervall == 0) {
			erscheinenLassen();
		}

		if (schrittzahl % (Konfiguration.FLASCHENPOST_LAENGE + Konfiguration.FLASCHENPOST_ABSTAND) < Konfiguration.FLASCHENPOST_LAENGE) {
			if (spielfeld.getFlaschenpostFeld() == null) { erscheinenLassen(); }
			bewegen();
		}

		if (schrittzahl % intervall == Konfiguration.FLASCHENPOST_LAENGE) {
			verschwindenLassen();
		}
	}

	private void erscheinenLassen() {
		spielfeld.setFlaschenpostFeld(spielfeld.getZufaelligesMeerfeld());
		richtungenWuerfeln();
	}

	private void verschwindenLassen() {
		spielfeld.setFlaschenpostFeld(null);
	}

	private void bewegen() {
		Feld naechstesFeld;
		Feld aktuellesFeld = spielfeld.getFlaschenpostFeld();

		int zufall = wuerfel.nextInt(3);
		switch (richtungen.get(zufall)) {
		case NORDEN: 
			naechstesFeld = spielfeld.getNachbar(aktuellesFeld, Richtung.NORDEN);
			break;
		case OSTEN: 
			naechstesFeld = spielfeld.getNachbar(aktuellesFeld, Richtung.OSTEN);
			break;
		case SUEDEN: 
			naechstesFeld = spielfeld.getNachbar(aktuellesFeld, Richtung.SUEDEN);
			break;
		default: 
			naechstesFeld = spielfeld.getNachbar(aktuellesFeld, Richtung.WESTEN);
			break;
		}
		
		if (naechstesFeld != null && naechstesFeld.getTyp() == Typ.MEER) {
			spielfeld.setFlaschenpostFeld(naechstesFeld);
		}
	}

	private void richtungenWuerfeln() {		
		for (int i=0; i<3; i++) {
			int zufall = wuerfel.nextInt(4);
			if (zufall == 0) {
				richtungen.add(Richtung.NORDEN);
			} else if (zufall == 1) {
				richtungen.add(Richtung.OSTEN);
			} else if (zufall == 2) {
				richtungen.add(Richtung.SUEDEN);
			} else {
				richtungen.add(Richtung.WESTEN);
			}
		}
	}
}
