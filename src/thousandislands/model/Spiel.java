package thousandislands.model;

import java.util.Map;
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
	@XmlElement
	private Inventar inventar;
	@XmlElement
	private Map<Ladung,Boolean> noetigeTeile;
	@XmlElement
	private Flaschenpost flaschenpost;

	public Spiel(Spielfeld spielfeld, Person person, Inventar inventar, Map<Ladung,Boolean> noetigeTeile, Flaschenpost flaschenpost) {
		this.spielfeld = spielfeld;
		this.person = person;
		this.inventar = inventar;
		this.noetigeTeile = noetigeTeile;
		this.flaschenpost = flaschenpost;
	}

	//noetig fuer JAXB
	public Spiel() {}

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

	public Map<Ladung,Boolean> getNoetigeTeile() {
		return noetigeTeile;
	}

	public Inventar getInventar() {
		return inventar;
	}

	public Flaschenpost getFlaschenpost() {
		return flaschenpost;
	}
}
