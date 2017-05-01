package thousandislands.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import thousandislands.model.enums.Richtung;
import thousandislands.model.enums.Typ;

public class Flaschenpost {
	private List<Richtung> richtungen = new ArrayList<>();
	private Feld aktuellesFeld;
	private Spielfeld spielfeld;
	private Random wuerfel = new Random();
	
	public Flaschenpost(Spielfeld spielfeld) {
		this.spielfeld = spielfeld;
	}
	
	public void erzeugen() {
		aktuellesFeld = spielfeld.getStartfeldFuerFlaschenpost();
		richtungenWuerfeln();
	}
	
	public void bewegen() {
		Feld naechstesFeld;

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
			aktuellesFeld.setFlaschenpost(false);
			naechstesFeld.setFlaschenpost(true);
			aktuellesFeld = naechstesFeld;
		}
	}
	
	public void entfernen() {
		aktuellesFeld.setFlaschenpost(false);
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
