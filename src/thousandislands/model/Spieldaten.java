package thousandislands.model;

public class Spieldaten {
	private Feld[][] felder;
	private Person person;
	
	public Spieldaten(Feld[][] felder, Person person) {
		this.felder = felder;
		this.person = person;
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
}
