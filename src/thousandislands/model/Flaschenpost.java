package thousandislands.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import thousandislands.model.enums.Typ;

public class Flaschenpost {
	private List<String> richtungen = new ArrayList<>();
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
		case "N": 
			naechstesFeld = aktuellesFeld.getNachbarN();
			break;
		case "O": 
			naechstesFeld = aktuellesFeld.getNachbarO();
			break;
		case "S": 
			naechstesFeld = aktuellesFeld.getNachbarS();
			break;
		default: 
			naechstesFeld = aktuellesFeld.getNachbarW();
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
				richtungen.add("N");
			} else if (zufall == 1) {
				richtungen.add("O");
			} else if (zufall == 2) {
				richtungen.add("S");
			} else {
				richtungen.add("W");
			}
		}
	}
}
