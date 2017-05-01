package thousandislands.model;

import java.util.Set;

import thousandislands.model.enums.Ladung;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Spiel {
	@XmlElement
	private Spielfeld spielfeld;
	@XmlElement
	private Person person;
	private int level;
	@XmlElement
	private Inventar inventar;
	@XmlElement
	private Set<Ladung> noetigeTeile;

	public Spiel(Spielfeld spielfeld, Person person, Inventar inventar, Set<Ladung> noetigeTeile) {
		this.spielfeld = spielfeld;
		this.person = person;
		level = 0;
		this.inventar = inventar;
		this.noetigeTeile = noetigeTeile;
	}

	//noetig fuer JAXB
	public Spiel() {}

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

	public Spielfeld getSpielfeld() {
		return spielfeld;
	}

	public Set<Ladung> getNoetigeTeile() {
		return noetigeTeile;
	}

	public Inventar getInventar() {
		return inventar;
	}
}
