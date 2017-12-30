package thousandislands.model;

import java.util.HashSet;
import java.util.Set;

import thousandislands.model.enums.Ladung;
import thousandislands.model.enums.Typ;
import thousandislands.model.enums.Zweck;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Feld {

	@XmlElement
	private boolean entdeckt;
	private Typ typ;
	private Zweck zweck;
	@XmlElement
	private int x;
	@XmlElement
	private int y;
	@XmlElement
	private Set<Ladung> ladungshaufen = new HashSet<>();
	
	public Feld (int x, int y) {
//		entdeckt = false;
		entdeckt = true;
		typ = Typ.MEER;
		this.x = x;
		this.y = y;
	}

	//noetig fuer JAXB
	public Feld() {}

	public boolean istEntdeckt() {
		return entdeckt;
	}

	public void setEntdeckt(boolean entdeckt) {
		this.entdeckt = entdeckt;
	}

	public Typ getTyp() {
		return typ;
	}
	
	public void setTyp(Typ typ) {
		this.typ = typ;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public Zweck getZweck() {
		return zweck;
	}

	public void setZweck(Zweck zweck) {
		this.zweck = zweck;
	}
	
	public void ladeTeilAb(Ladung teil) {
		ladungshaufen.add(teil);
	}
	
	public Set<Ladung> getLadung() {
		return ladungshaufen;
	}

	public boolean equals(Feld anderesFeld) {
		return anderesFeld != null && x == anderesFeld.getX() && y == anderesFeld.getY();
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 17 + x;
		hash = hash * 31 + y;
		return hash;
	}

	public String toString() {
		return "x: " + x + ", y: " + y;
	}
}
