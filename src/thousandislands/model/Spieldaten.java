package thousandislands.model;

public class Spieldaten {
	private Feld[][] felder;
	private Person person;
	private String[] schiffsteile = {	"Mast", "Rumpf", "Segel", "Kompass", "Werkzeug", 
			"Tonkruege", "Koerbe", "Wasser", "Nahrung"};
	
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

	public Feld[][] getFelder() {
		return felder;
	}
	
	public String[] getSchiffsteile() {
		return schiffsteile;
	}
}
