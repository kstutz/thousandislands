package thousandislands.controller;

import java.util.Random;

import thousandislands.model.Feld;
import thousandislands.model.Spielfeld;
import thousandislands.model.enums.Richtung;
import thousandislands.model.enums.Typ;
import thousandislands.model.enums.Zweck;

public class SpielfeldErsteller {
	
	private static final int FELDANZAHL_WAAGERECHT = 100;
	private static final int FELDANZAHL_SENKRECHT = 60;
	private static final int INSELBREITE = 12;
	private static final int ANZAHL_INSELN = 15;

	private Feld [][] felder = new Feld[FELDANZAHL_WAAGERECHT][FELDANZAHL_SENKRECHT];
	private Feld anfangsfeld;
	private Feld schatzkartenanfang;
	private Random rand = new Random();
	
	
	public SpielfeldErsteller(){
		erzeugeFelder();
		erstelleInseln();
	}

	public Spielfeld getSpielfeld() {
		return new Spielfeld(felder);
	}

	private void erzeugeFelder() {
		for (int i = 0; i<FELDANZAHL_WAAGERECHT; i++) {
			for (int j=0; j<FELDANZAHL_SENKRECHT; j++) {
				felder[i][j] = new Feld(i,j);
			}
		}
	}

	public void erstelleInseln() {
		InselBauer inselbauer;
		Feld startpunkt;
		int inselanzahl = 0;

		//erste Insel erstellen mit Huette
		startpunkt = findeStartpunkt(0, 0, 20, 20);
		inselbauer = new InselBauer(startpunkt, felder, Zweck.HUETTE);
		anfangsfeld = inselbauer.findeStartpunkt();
		inselanzahl++;

		//zweite Insel erstellen als Schiffbauinsel
		do {
			startpunkt = findeStartpunkt(80, 20, 20, 20);
		} while (!istUmgebungFrei(startpunkt));
		inselbauer = new InselBauer(startpunkt, felder, Zweck.SCHIFFBAU);
		inselanzahl++;
		
		while (inselanzahl < ANZAHL_INSELN) {
			startpunkt = findeStartpunkt(0, 0, FELDANZAHL_WAAGERECHT, FELDANZAHL_SENKRECHT);
			if (!istUmgebungFrei(startpunkt)) {
				continue;
			}
			
     		inselbauer = new InselBauer(startpunkt, felder, Zweck.OFFEN);
			
			if (inselanzahl == 4) {
				schatzkartenanfang = startpunkt;
				inselbauer.versteckeSchatz();
			}
			
			inselanzahl++;
		}
	}
	
	private Feld findeStartpunkt(int x, int y, int breite, int hoehe) {
		int a;
		int b;
		do {
			a = rand.nextInt(breite - INSELBREITE) + x;
			b = rand.nextInt(hoehe - INSELBREITE) + y;
			System.out.print("o");
		} while (felder[a][b].getTyp() != Typ.MEER);
		System.out.println();
		return felder[a][b];
	}
	
	private Boolean istUmgebungFrei(Feld feld) {
		int x = feld.getX();
		int y = feld.getY();
		
		for (int i=0; i<INSELBREITE; i++) {
			for (int j=0; j<INSELBREITE; j++) {
				if (felder[x+i][y+j].getTyp() != Typ.MEER) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public Feld getSchatzkartenanfang() {
		return schatzkartenanfang;
	}
	
	public Feld getSpielanfang() {
		return anfangsfeld;
	}
	
	public void versteckeWrack() {
		int a;
		int b;
		do {
			a = rand.nextInt(FELDANZAHL_WAAGERECHT);
			b = rand.nextInt(FELDANZAHL_SENKRECHT);
		} while (felder[a][b].getTyp() != Typ.MEER);
		felder[a][b].setTyp(Typ.WRACK);
	}
}
