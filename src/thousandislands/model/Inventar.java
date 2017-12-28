package thousandislands.model;

import java.util.HashSet;
import java.util.Set;

import thousandislands.model.enums.Ladung;

import javax.xml.bind.annotation.XmlElement;

public class Inventar {
	@XmlElement
	private Set<Ladung> liste = new HashSet<>();
	
	public int getGesamtgewicht() {
		int gewicht = 0;
		for (Ladung sache : liste) {
			gewicht += sache.getGewicht();
		}
		return gewicht;
	}

	public void ladungHinzufuegen(Ladung ladung) {
		liste.add(ladung);
	}
	
	public void ladungEntfernen(Ladung ladung) {
		liste.remove(ladung);
	}
	
	public boolean enthaelt(Ladung ladung) {
		return liste.contains(ladung);
	}

	public Set<Ladung> getLadeliste() {
		return liste;
	}
	
	public boolean istLeer() {
		return liste.isEmpty();
	}
	
	public void leeren() {
		liste.clear();
	}
	
	public void listeAusgeben() {
		for (Ladung teil : liste) {
			System.out.println(teil.toString() + " - " + teil.getGewicht());
		}
	}
}
