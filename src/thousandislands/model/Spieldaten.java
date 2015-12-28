package thousandislands.model;

import java.util.Set;

import thousandislands.model.enums.Ladung;

public class Spieldaten {
	private Feld[][] felder;
	private Person person;
	private int level;
	private Inventar inventar;
	private Set<Ladung> noetigeTeile;
	
	public Spieldaten(Feld[][] felder, Person person, Inventar inventar, Set<Ladung> noetigeTeile) {
		this.felder = felder;
		this.person = person;
		level = 0;
		this.inventar = inventar;
		this.noetigeTeile = noetigeTeile;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}

	public Person getPerson() {
		return person;
	}

	public String getWasser() {
		return person.getWasser() + " / " + person.getMaxWasser();
	}

	public String getNahrung() {
		return person.getNahrung() + " / " + person.getMaxNahrung();
	}
	
	public boolean hatFloss() {
		return person.hatFloss();
	}

	public Feld[][] getFelder() {
		return felder;
	}
	
	public Set<Ladung> getNoetigeTeile() {
		return noetigeTeile;
	}
	
	public Inventar getInventar() {
		return inventar;
	}
}
