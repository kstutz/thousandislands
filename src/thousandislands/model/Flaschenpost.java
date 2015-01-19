package thousandislands.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import thousandislands.model.enums.Richtung;
import thousandislands.model.enums.Typ;

public class Flaschenpost {
	private List<Richtung> richtungen = new ArrayList<>();
	private Feld aktuellesFeld;
	private Feld[][] spielfeld;
	private Random wuerfel = new Random();
	
	public Flaschenpost(Feld[][] spielfeld) {
		this.spielfeld = spielfeld;
	}
	
	public void erzeugen() {
		aktuellesFeld = startfeldWuerfeln();
		richtungenWuerfeln();
	}
	
	public void bewegen() {
		Feld naechstesFeld;

		int zufall = wuerfel.nextInt(3);
		switch (richtungen.get(zufall)) {
		case NORDEN: 
			naechstesFeld = aktuellesFeld.getNachbar(Richtung.NORDEN);
			break;
		case OSTEN: 
			naechstesFeld = aktuellesFeld.getNachbar(Richtung.OSTEN);
			break;
		case SUEDEN: 
			naechstesFeld = aktuellesFeld.getNachbar(Richtung.SUEDEN);
			break;
		default: 
			naechstesFeld = aktuellesFeld.getNachbar(Richtung.WESTEN);
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
	
	private Feld startfeldWuerfeln() {
		while(true) {
			int x = wuerfel.nextInt(spielfeld.length);
			int y = wuerfel.nextInt(spielfeld[0].length);
			if (spielfeld[x][y].getTyp() == Typ.MEER) {
				return spielfeld[x][y];
			}
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
