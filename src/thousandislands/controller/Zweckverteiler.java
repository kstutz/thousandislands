package thousandislands.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import thousandislands.model.Feld;
import thousandislands.model.enums.Zweck;

public class Zweckverteiler {
	private List<Zweck> ersteZwecke = new LinkedList<>(Arrays.asList(
			Zweck.WASSER, Zweck.NAHRUNG, Zweck.LIANEN, Zweck.HOLZ));
	private List<Zweck> restlicheZwecke = new LinkedList<>(Arrays.asList(
			Zweck.TON, Zweck.FEUER,
			Zweck.GROSSER_BAUM, Zweck.SCHILF,
			Zweck.PAPAYA, Zweck.RUINE,
			Zweck.WASSER, Zweck.NAHRUNG,
			Zweck.LEER));
	private List<Zweck> zweckliste = new ArrayList<>();

	Zweckverteiler() {
		erstelleZweckliste();
	}

	public List<Zweck> getZweckliste() {
		return zweckliste;
	}

	private void erstelleZweckliste() {
		zweckliste.addAll(ersteZwecke);
		Random rand = new Random();
		int zufallszahl;
		
		while (!restlicheZwecke.isEmpty()) {
			zufallszahl = rand.nextInt(restlicheZwecke.size());
			zweckliste.add(restlicheZwecke.remove(zufallszahl));
		}
	}
}
