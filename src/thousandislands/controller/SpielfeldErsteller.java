package thousandislands.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import thousandislands.model.Feld;
import thousandislands.model.Insel;
import thousandislands.model.enums.Typ;
import thousandislands.model.enums.Zweck;

public class SpielfeldErsteller {
	
//	private static final int SPIELFELDBREITE_PX = 1200;
//	private static final int SPIELFELDHOEHE_PX = 800;
	private static final int FELDBREITE_PX = 10;
	private static final int FELDHOEHE_PX = 10;
	private static final int FELDANZAHL_WAAGERECHT = 100;
	private static final int FELDANZAHL_SENKRECHT = 60;
	private static final int ABSTAND_FUER_ZENTRUM = 6;
	private static final int ANZAHL_INSELN = 15;

	private Feld [][] felder = new Feld[FELDANZAHL_WAAGERECHT][FELDANZAHL_SENKRECHT];
	private List<Insel> inseln = new ArrayList<Insel>();
	private List<Feld> zweckfelder = new ArrayList<>();
	private Feld anfangSchatzinsel;
	private Random rand = new Random();
	
	
	public SpielfeldErsteller(){
		erzeugeFelder();
		verbindeFelder();
		erstelleInseln();
		
		for (int i = 0; i<FELDANZAHL_WAAGERECHT; i++) {
			for (int j=0; j<FELDANZAHL_SENKRECHT; j++) {
				if (felder[i][j].getTyp() == Typ.VOR_MEER) {
					felder[i][j].setTyp(Typ.MEER); 
				}
			}
		}
		verteileZwecke();
		
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
					felder[i][j].setNachbarW(felder[i-1][j]);
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
					felder[i][j].setNachbarO(felder[i+1][j]);
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
					felder[i][j].setNachbarN(felder[i][j-1]);
				}
				if (j < FELDANZAHL_SENKRECHT-1) {
					felder[i][j].setNachbar(felder[i][j+1]);
					felder[i][j].setDirekterNachbar(felder[i][j+1]);
					felder[i][j].setNachbarS(felder[i][j+1]);
				}
			}
		}		
	}
	
	
	public void erstelleInseln() {
		int inselanzahl = 0;
		
		//TODO: erste Insel erstellen mit Wasser
		//TODO: zweite Insel erstellen mit Nahrung		
		//TODO: dritte Insel erstellen als Schiffbauinsel (rechts, Mitte, großer Strand)
		
		while (inselanzahl < ANZAHL_INSELN) {
			Feld zentrum = findeZentrum();
			if (!istUmgebungFrei(zentrum)) {
				continue;
			}

			zentrum = felder[zentrum.getX()-6][zentrum.getY()-6];
			InselBauer inselbauer = new InselBauer(zentrum,felder);
			zweckfelder.add(inselbauer.getZweckfeld());
			
			if (inselanzahl == 4) {
				anfangSchatzinsel = zentrum;
				inselbauer.versteckeSchatz();
			}
			
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
	
	private void verteileZwecke() {
		
		//TODO: Inseln ausnehmen, die schon Zweck haben (erste, zweite, Schiffbau)
		
		List<Feld> ersteZwei = sucheInselnLinksOben();
		ersteZwei.get(0).setTyp(Typ.ZWECK);
		ersteZwei.get(0).setZweck(Zweck.WASSER);
		ersteZwei.get(1).setTyp(Typ.ZWECK);
		ersteZwei.get(1).setZweck(Zweck.NAHRUNG);

//		for (Feld feld : zweckfelder) {
//			if (feld.getTyp() == Typ.ZWECK) {
//				int zufall = rand.nextInt(3);
//				if (zufall == 0) {
//					feld.setTyp(Typ.QUELLE);
//				}
//				if (zufall == 1) {
//					feld.setTyp(Typ.FRUECHTE);
//				}
//				if (zufall == 2) {
//					feld.setTyp(Typ.HOLZ);
//				}
//			}
//		}
	}
	
	private List<Feld> sucheInselnLinksOben() {
		Feld naechstes = new Feld(-1, -1);
		Feld zweitnaechstes = new Feld(-1, -1);
		int kleinsterAbstand1 = 100000;
		int kleinsterAbstand2 = 100000;
		
		for (Feld feld : zweckfelder ) {
			
			int entfernung = feld.getX() + feld.getY();
			System.out.println("Entfernung: " + entfernung);
			System.out.println(feld.getX() + ", " + feld.getY());
			
			if (entfernung < kleinsterAbstand1) {
				naechstes = feld;
				kleinsterAbstand1 = entfernung;
			} else if (entfernung < kleinsterAbstand2) {
				zweitnaechstes = feld;
				kleinsterAbstand2 = entfernung;
			}
		}
		naechstes.setTyp(Typ.ROT);
		zweitnaechstes.setTyp(Typ.ROT);
		
		List<Feld> ersteZweiInseln = new ArrayList<>();
		ersteZweiInseln.add(naechstes);
		ersteZweiInseln.add(zweitnaechstes);
		return ersteZweiInseln;		
	}
}
