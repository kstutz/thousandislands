package thousandislands.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import thousandislands.model.Feld;
import thousandislands.model.Insel;
import thousandislands.model.enums.Typ;
import thousandislands.model.enums.Zweck;

public class SpielfeldErsteller {
	
//	private static final int SPIELFELDBREITE_PX = 1200;
//	private static final int SPIELFELDHOEHE_PX = 800;
//	private static final int FELDBREITE_PX = 10;
//	private static final int FELDHOEHE_PX = 10;
	private static final int FELDANZAHL_WAAGERECHT = 100;
	private static final int FELDANZAHL_SENKRECHT = 60;
	private static final int INSELBREITE = 12;
	private static final int ANZAHL_INSELN = 15;

	private Feld [][] felder = new Feld[FELDANZAHL_WAAGERECHT][FELDANZAHL_SENKRECHT];
	private List<Insel> inseln = new ArrayList<Insel>();
	private Feld schatzkartenanfang;
	private Random rand = new Random();
	
	
	public SpielfeldErsteller(){
		erzeugeFelder();
		verbindeFelder();
		erstelleInseln();
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
		InselBauer inselbauer;
		Feld startpunkt;
		int inselanzahl = 0;
		List<Zweck> zwecke = new LinkedList<>(Arrays.asList(
				Zweck.HOLZ, Zweck.LIANEN, Zweck.TON, Zweck.FEUER, 
				Zweck.GROSSER_BAUM, Zweck.SCHILF,
				Zweck.HUETTE, Zweck.PAPAYA, Zweck.RUINE,
				Zweck.WASSER, Zweck.NAHRUNG,
				Zweck.LEER, Zweck.LEER));

		//erste Insel erstellen mit Wasser
		startpunkt = findeStartpunkt(0, 0, 30, 30);
		inselbauer = new InselBauer(startpunkt, felder, Zweck.WASSER);
		inselanzahl++;

		//zweite Insel erstellen mit Nahrung
		do {
			startpunkt = findeStartpunkt(0, 0, 30, 30);
		} while (!istUmgebungFrei(startpunkt));
		inselbauer = new InselBauer(startpunkt, felder, Zweck.NAHRUNG);
		inselanzahl++;

		//dritte Insel erstellen als Schiffbauinsel
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
			
     		inselbauer = new InselBauer(startpunkt, felder, zwecke.get(0));				
			zwecke.remove(0);				
			
			if (inselanzahl == 4) {
				schatzkartenanfang = startpunkt;
				inselbauer.versteckeSchatz();
			}
			
			inselanzahl++;
		}
	}
	
	public Feld getSchatzkartenanfang() {
		return schatzkartenanfang;
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
	
}
