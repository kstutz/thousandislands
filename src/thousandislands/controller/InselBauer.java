package thousandislands.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import thousandislands.model.Feld;
import thousandislands.model.enums.Typ;
import thousandislands.model.enums.Zweck;

public class InselBauer {
	private int xDazu;
	private int yDazu;
	private Feld[][] spielfeld;
	private Zweck zweck;
	private int[][] spielfeldteil;
	private List<Feld> inselfelder = new ArrayList<>();
	private Feld zweckfeld;
	private Random rand = new Random();
	
	public InselBauer(Feld anfang, Feld[][] spielfeld, Zweck zweck) {
		this.xDazu = anfang.getX();
		this.yDazu = anfang.getY();
		this.spielfeld = spielfeld;
		this.zweck = zweck;
		
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
		
		if (zweck == Zweck.SCHIFFBAU) {
		    fuelleArraySchiffbau();
			wuerfele();
			entferneSeen();
			erstelleSchiffbaustrand();
			erstelleZweckfeld();
		} else {
			wuerfele();
			entferneSeen();
			erstelleStrand();
			erstelleZweckfeld();			
		}
	}
	

	private void erstelleSchiffbaustrand() {
		for (Feld feld : inselfelder) {
			if (feld.getTyp() == Typ.STRAND) {
				feld.setTyp(Typ.SCHIFFBAUSTRAND);
			}
		}
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

	private void fuelleArraySchiffbau() {
		
		int muster[][] = { 	{0,0,0,0,0,0,0,0,0,0,0,0},
							{0,0,0,-3,-3,-3,-3,-3,-3,0,0,0},
							{0,0,-3,-4,-5,-5,-5,-5,-4,-3,0,0},
							{0,1,2,3,-5,-5,-5,-5,3,2,1,0},
							{0,2,3,4,5,-5,-5,-5,4,3,2,0},
							{0,2,3,4,5,5,5,5,4,3,2,0},
							{0,2,4,5,5,5,5,5,5,4,2,0},
							{0,2,4,5,5,5,5,5,5,4,2,0},
							{0,1,2,3,4,5,5,4,3,2,1,0},
							{0,0,1,2,3,4,4,3,2,1,0,0},
							{0,0,0,1,2,2,2,2,1,0,0,0},
							{0,0,0,0,0,0,0,0,0,0,0,0}};
		
		spielfeldteil = muster;
	}

	private void wuerfele() {
		for (int i = 0; i<12; i++) {
			for (int j = 0; j<12; j++) {
				spielfeldteil[i][j] += rand.nextInt(4);
				if (spielfeldteil[i][j] >= 0 && spielfeldteil[i][j] < 4) {
					spielfeld[i+xDazu][j+yDazu].setTyp(Typ.MEER);
				} else if (spielfeldteil[i][j] < 0) {
					spielfeld[i+xDazu][j+yDazu].setTyp(Typ.STRAND);
					inselfelder.add(spielfeld[i+xDazu][j+yDazu]);
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
		List<Feld> nachbarn = getDirekteNachbarn(feld);
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
			
			List<Feld> nachbarn = getDirekteNachbarn(feld);
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
		List<Feld> nachbarn = getDirekteNachbarn(feld);
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
		
		for (Feld nachbar : getDirekteNachbarn(feld)) {
			if (nachbar.getTyp() == Typ.DSCHUNGEL) {
				return false;
			}
			if (nachbar.getTyp() == Typ.STRAND) {
				strandNachbar = true;
			}
		}
		return strandNachbar;
	}
	
	private void erstelleZweckfeld() {
		do {
			int zufall = rand.nextInt(inselfelder.size());
			Feld zweckfeld = inselfelder.get(zufall);
			if (zweckfeld.getTyp() == Typ.DSCHUNGEL) {
				if(koennteZweckfeldSein(zweckfeld)) {
					zweckfeld.setTyp(Typ.ZWECK);
					this.zweckfeld = zweckfeld;
				}
			}
		} while (this.zweckfeld == null);
		
		zweckfeld.setTyp(Typ.ZWECK);

		if (zweck != null) {
			zweckfeld.setZweck(zweck);
		}
	}
	
	private boolean koennteZweckfeldSein(Feld feld) {
		for (Feld nachbar : getDirekteNachbarn(feld)) {
			if (nachbar.getTyp() == Typ.MEER) {
				return false;
			}
		}
		return true;
	}

	public void versteckeSchatz() {
		int zufall;
		Feld schatzfeld;
		do {
			zufall = rand.nextInt(inselfelder.size());
			schatzfeld = inselfelder.get(zufall);			
		} while (schatzfeld.getTyp() != Typ.DSCHUNGEL);
		schatzfeld.setTyp(Typ.SCHATZ);
	}


	public Feld findeStartpunkt() {
		for (Feld feld: inselfelder) {
			if (feld.getTyp() == Typ.STRAND) {
				return feld;
			}
		}
		return null;
	}

	private List<Feld> getDirekteNachbarn(Feld feld) {
		List<Feld> nachbarn = new ArrayList<>();
		int x = feld.getX();
		int y = feld.getY();

		if (x > 0) {
			nachbarn.add(spielfeld[x-1][y]);
		}
		if (x < spielfeld.length-1) {
			nachbarn.add(spielfeld[x+1][y]);
		}
		if (y > 0) {
			nachbarn.add(spielfeld[x][y-1]);
		}
		if (y < spielfeld[0].length-1) {
			nachbarn.add(spielfeld[x][y+1]);
		}
		return nachbarn;
	}
	
}
