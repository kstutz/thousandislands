package thousandislands.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import thousandislands.model.enums.Ladung;
import thousandislands.model.enums.Richtung;
import thousandislands.model.enums.Status;
import thousandislands.model.enums.Typ;
import thousandislands.model.enums.Zweck;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Feld {
	@XmlElement
	private Status status;
	private Typ typ;
	private Zweck zweck;
	private boolean personDa = false;
	private boolean flossDa = false;
	private boolean hatFlaschenpost = false;
	@XmlElement
	private int x;
	@XmlElement
	private int y;
	@XmlElement
	private Set<Ladung> ladungshaufen = new HashSet<>();
	
	public Feld (int x, int y) {
		status = Status.ENTDECKT;
//		status = Status.UNENTDECKT;
		typ = Typ.MEER;
		this.x = x;
		this.y = y;
	}

	//noetig fuer JAXB
	public Feld() {}
	
	public Status getStatus() {
		return status;
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

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean istPersonDa() {
		return personDa;
	}

	public void setPersonDa(boolean personDa) {
		this.personDa = personDa;
	}

	public boolean istFlossDa() {
		return flossDa;
	}

	public void setFlossDa(boolean flossDa) {
		this.flossDa = flossDa;
	}

	public boolean hatFlaschenpost() {
		return hatFlaschenpost;
	}
	
	public void setFlaschenpost(boolean bool) {
		hatFlaschenpost = bool;
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
}
