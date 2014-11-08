package thousandislands.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import thousandislands.model.Feld;
import thousandislands.model.enums.Typ;

public class InselBauer2 {
	private int xDazu;
	private int yDazu;
	private Feld[][] spielfeld;
	private int[][] spielfeldteil;
	private List<Feld> inselfelder = new ArrayList<>();
	private Feld zweckfeld;
	private Random rand = new Random();
	
	public InselBauer2(Feld anfang, Feld[][] spielfeld) {
		this.xDazu = anfang.getX();
		this.yDazu = anfang.getY();
		this.spielfeld = spielfeld;
		
		erstelleInsel();
	}
	
	
	public void erstelleInsel(){
		
		int zufall = rand.nextInt(3);
		if (zufall == 0) {
			fuelleArrayKlein();
		} else if (zufall == 1) {
			fuelleArrayMittel();
		} else {
			fuelleArrayGross();
		}
		
		wuerfele();
		entferneSeen();
		erstelleStrand();
//		erstelleZweckfeld();
	}
	

	private void fuelleArrayGross() {
		int muster[][] = { 	{0,0,0,0,0,0,0,0,0,0,0,0},
							{0,0,0,1,2,2,2,2,1,0,0,0},
							{0,0,1,2,3,4,4,3,2,1,0,0},
							{0,1,2,3,4,5,5,4,3,2,1,0},
							{0,2,3,4,5,5,5,5,4,3,2,0},
							{0,2,4,5,5,5,5,5,5,4,2,0},
							{0,2,4,5,5,5,5,5,5,4,2,0},
							{0,2,3,4,5,5,5,5,4,3,2,0},
							{0,1,2,3,4,5,5,4,3,2,1,0},
							{0,0,1,2,3,4,4,3,2,1,0,0},
							{0,0,0,1,2,2,2,2,1,0,0,0},
							{0,0,0,0,0,0,0,0,0,0,0,0}};
		
		spielfeldteil = muster;
	}
	
	private void fuelleArrayMittel() {
		int muster[][] = { 	{0,0,0,0,0,0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0,0,0,0,0,0},
							{0,0,0,0,2,2,2,2,0,0,0,0},
							{0,0,0,2,3,4,4,3,2,0,0,0},
							{0,0,2,3,4,5,5,4,3,2,0,0},
							{0,0,2,4,5,5,5,5,4,2,0,0},
							{0,0,2,4,5,5,5,5,4,2,0,0},
							{0,0,2,3,4,5,5,4,3,2,0,0},
							{0,0,0,2,3,4,4,3,2,0,0,0},
							{0,0,0,0,2,2,2,2,0,0,0,0},
							{0,0,0,0,0,0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0,0,0,0,0,0}};

		spielfeldteil = muster;
	}

	private void fuelleArrayKlein() {
		
		int muster[][] = { 	{0,0,0,0,0,0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0,0,0,0,0,0},
							{0,0,0,0,0,3,3,0,0,0,0,0},
							{0,0,0,0,3,4,4,3,0,0,0,0},
							{0,0,0,3,4,5,5,4,3,0,0,0},
							{0,0,0,3,4,5,5,4,3,0,0,0},
							{0,0,0,0,3,4,4,3,0,0,0,0},
							{0,0,0,0,0,3,3,0,0,0,0,0},
							{0,0,0,0,0,0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0,0,0,0,0,0},
							{0,0,0,0,0,0,0,0,0,0,0,0}};

		spielfeldteil = muster;
	}
	
	private void wuerfele() {
		for (int i = 0; i<12; i++) {
			for (int j = 0; j<12; j++) {
				spielfeldteil[i][j] += rand.nextInt(4);
				if (spielfeldteil[i][j] < 4) {
					spielfeld[i+xDazu][j+yDazu].setTyp(Typ.MEER);
				} else {
					spielfeld[i+xDazu][j+yDazu].setTyp(Typ.DSCHUNGEL);
					inselfelder.add(spielfeld[i+xDazu][j+yDazu]);
				}
			}
		}
	}
	
	private void entferneSeen() {
		for (int i = xDazu; i<xDazu+12; i++) {
			for (int j = yDazu; j<yDazu+12; j++) {
				if (spielfeld[i][j].getTyp() == Typ.MEER){
					if (!liegtAmMeer(spielfeld[i][j])) {
						spielfeld[i][j].setTyp(Typ.DSCHUNGEL);
						inselfelder.add(spielfeld[i][j]);
					}
				}
			}
		}
	}

	private boolean liegtAmMeer(Feld feld) {
		List<Feld> nachbarn = feld.getDirekteNachbarn();
		for (Feld nachbar : nachbarn) {
			if (nachbar.getTyp() == Typ.MEER) {
				return true;
			}
		}
		return false;
	}
	
	private void erstelleStrand() {		
		int anzahlStraende = 2 + rand.nextInt(4); 
		
		for (int i=0; i<anzahlStraende; i++) {
			int zufall;
			Feld feld;
			
			do {
				zufall = rand.nextInt(inselfelder.size());
				feld = inselfelder.get(zufall);			
			} while (!koennteStrandSein(feld));
			
			feld.setTyp(Typ.STRAND);
			
			List<Feld> nachbarn = feld.getDirekteNachbarn();
			for (Feld nachbar : nachbarn) {
				if (koennteStrandSein(nachbar)) {
					nachbar.setTyp(Typ.STRAND);
				}
			}
		}
		
		for (Feld feld : inselfelder) {
			if (feld.getTyp() == Typ.DSCHUNGEL && sollteStrandSein(feld)) {
				feld.setTyp(Typ.STRAND);
			}
		}
		
	}
	
	private boolean koennteStrandSein(Feld feld) {
		if (feld.getTyp() == Typ.DSCHUNGEL && liegtAmMeer(feld) && !istInselchen(feld)) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean istInselchen(Feld feld) {
		List<Feld> nachbarn = feld.getDirekteNachbarn();
		if (feld.getTyp() != Typ.DSCHUNGEL) {
			return false;
		}
		for (Feld nachbar : nachbarn) {
			if (nachbar.getTyp() != Typ.MEER) {
				return false;
			}
		}
		return true;
	}

	private boolean sollteStrandSein(Feld feld) {
		boolean strandNachbar = false;
		
		for (Feld nachbar : feld.getDirekteNachbarn()) {
			if (nachbar.getTyp() == Typ.DSCHUNGEL) {
				return false;
			}
			if (nachbar.getTyp() == Typ.STRAND) {
				strandNachbar = true;
			}
		}
		return strandNachbar;
	}

	
	public Feld getZweckfeld() {
		return zweckfeld;
	}
	


	private void erstelleZweckfeld() {
		boolean gefunden = false;
		do {
			int zufall = rand.nextInt(inselfelder.size());
			Feld zweckfeld = inselfelder.get(zufall);
			if (zweckfeld.getTyp() == Typ.DSCHUNGEL) {
				gefunden = true;
				List<Feld> direkteNachbarn = zweckfeld.getDirekteNachbarn();
				for (Feld nachbar : direkteNachbarn) {
					if (nachbar.getTyp() == Typ.MEER || nachbar.getTyp() == Typ.VOR_MEER ) {
						gefunden = false;
						break;
					}
				}
				if (gefunden == true) {
					zweckfeld.setTyp(Typ.ZWECK);
					this.zweckfeld = zweckfeld;
				}
			}
		} while (!gefunden);
	}
}
