package thousandislands.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import thousandislands.model.Feld;
import thousandislands.model.Insel;
import thousandislands.model.enums.Typ;

public class SpielfeldErsteller {
	
//	private static final int SPIELFELDBREITE_PX = 1200;
//	private static final int SPIELFELDHOEHE_PX = 800;
	private static final int FELDBREITE_PX = 10;
	private static final int FELDHOEHE_PX = 10;
	private static final int FELDANZAHL_WAAGERECHT = 100;
	private static final int FELDANZAHL_SENKRECHT = 60;
	private static final int ABSTAND_FUER_ZENTRUM = 6;
//	private static final int BEREICHSBREITE_IN_FELDERN = 20;
//	private static final int BEREICHSHOEHE_IN_FELDERN = 20;	
	private static final int ANZAHL_INSELN = 15;

	private Feld [][] felder = new Feld[FELDANZAHL_WAAGERECHT][FELDANZAHL_SENKRECHT];
	private List<Insel> inseln = new ArrayList<Insel>();
	private Random rand = new Random();
	
	
	public SpielfeldErsteller(){
		erzeugeFelder();
		verbindeFelder();
		erstelleInseln2();
		
		for (int i = 0; i<FELDANZAHL_WAAGERECHT; i++) {
			for (int j=0; j<FELDANZAHL_SENKRECHT; j++) {
				if (felder[i][j].getTyp() == Typ.VOR_MEER) {
					felder[i][j].setTyp(Typ.MEER); 
				}
			}
		}
	}
	
	public Feld[][] getSpielfeld() {
		return felder;
	}
	

	private void erzeugeFelder() {
		for (int i = 0; i<FELDANZAHL_WAAGERECHT; i++) {
			for (int j=0; j<FELDANZAHL_SENKRECHT; j++) {
				felder[i][j] = new Feld(i,j);
			}
		}
	}
	
	private void verbindeFelder() {
		for (int i = 0; i<FELDANZAHL_WAAGERECHT; i++) {
			for (int j=0; j<FELDANZAHL_SENKRECHT; j++) {
				if (i > 0) {
					felder[i][j].setNachbar(felder[i-1][j]);
					felder[i][j].setDirekterNachbar(felder[i-1][j]);
					if (j > 0) {
						felder[i][j].setNachbar(felder[i-1][j-1]);
					}
				    if (j < FELDANZAHL_SENKRECHT-1) {
					    felder[i][j].setNachbar(felder[i-1][j+1]);
				    }
				}
				if (i < FELDANZAHL_WAAGERECHT-1) { 
					felder[i][j].setNachbar(felder[i+1][j]);
					felder[i][j].setDirekterNachbar(felder[i+1][j]);
					if (j > 0) {
						felder[i][j].setNachbar(felder[i+1][j-1]);
					}
				    if (j < FELDANZAHL_SENKRECHT-1) {
					    felder[i][j].setNachbar(felder[i+1][j+1]);
				    }
				}
				if (j > 0) {
					felder[i][j].setNachbar(felder[i][j-1]);
					felder[i][j].setDirekterNachbar(felder[i][j-1]);
				}
				if (j < FELDANZAHL_SENKRECHT-1) {
					felder[i][j].setNachbar(felder[i][j+1]);
					felder[i][j].setDirekterNachbar(felder[i][j+1]);
				}
			}
		}		
	}
	
	
	public void erstelleInseln2() {
		int inselanzahl = 0;
		
		while (inselanzahl < ANZAHL_INSELN) {
			Feld zentrum = findeZentrum();
			if (!istUmgebungFrei(zentrum)) {
				continue;
			}
			new InselBauer(zentrum, felder);
			inselanzahl++;
		}
	}

	private Feld findeZentrum() {
		int x;
		int y;
		do {
			x = rand.nextInt(FELDANZAHL_WAAGERECHT - 2*ABSTAND_FUER_ZENTRUM) + ABSTAND_FUER_ZENTRUM;
			y = rand.nextInt(FELDANZAHL_SENKRECHT - 2*ABSTAND_FUER_ZENTRUM) + ABSTAND_FUER_ZENTRUM;
			System.out.print("o");
		} while (felder[x][y].getTyp() != Typ.VOR_MEER);
		System.out.println();
		return felder[x][y];
	}
	
	
	private Boolean istUmgebungFrei(Feld feld) {
		int x = feld.getX() - ABSTAND_FUER_ZENTRUM;
		int y = feld.getY() - ABSTAND_FUER_ZENTRUM;
		
		for (int i=0; i<2*ABSTAND_FUER_ZENTRUM; i++) {
			for (int j=0; j<2*ABSTAND_FUER_ZENTRUM; j++) {
				if (felder[x+i][y+j].getTyp() != Typ.VOR_MEER) {
					return false;
				}
			}
		}
		
		return true;
	}	
}
