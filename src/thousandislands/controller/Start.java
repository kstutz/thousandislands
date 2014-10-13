package thousandislands.controller;

import thousandislands.model.Feld;
import thousandislands.view.GUI;


public class Start {
	private static final int FELDANZAHL_WAAGERECHT = 100;
	private static final int FELDANZAHL_SENKRECHT = 60;
	
	public static void main (String[] args) {
//		Feld[][] spielfeld = new Feld[FELDANZAHL_WAAGERECHT][FELDANZAHL_SENKRECHT];
		
		Feld[][] spielfeld = new SpielfeldErsteller().getSpielfeld();
		GUI gui = new GUI(spielfeld);
	}

}
