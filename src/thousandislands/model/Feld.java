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

public class Feld {
	private Status status;
	private Typ typ;
	private Zweck zweck;
	private List<Feld> nachbarn = new ArrayList<Feld>();
	private List<Feld> direkteNachbarn = new ArrayList<Feld>();
	private boolean personDa = false;
	private boolean flossDa = false;
	private boolean hatFlaschenpost = false;
	private int x;
	private int y;
	private Feld nachbarO;
	private Feld nachbarW;
	private Feld nachbarN;
	private Feld nachbarS;
	private Set<Ladung> ladungshaufen = new HashSet<>();
	
	public Feld (int x, int y) {
		status = Status.ENTDECKT;
//		status = Status.UNENTDECKT;
		typ = Typ.MEER;
		this.x = x;
		this.y = y;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public Typ getTyp() {
		return typ;
	}
	
	public void setTyp(Typ typ) {
		this.typ = typ;
	}
	
	public List<Feld> getNachbarn() {
		return nachbarn;
	}
	
	public void setNachbar(Feld feld) {
		nachbarn.add(feld);
	}

	public List<Feld> getDirekteNachbarn() {
		return direkteNachbarn;
	}
	
	public void setDirekterNachbar(Feld feld) {
		direkteNachbarn.add(feld);
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
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
	
	public Feld getNachbar(Richtung richtung) {
		switch (richtung) {
		case NORDEN:
			return nachbarN;
		case OSTEN:
			return nachbarO;
		case SUEDEN:
			return nachbarS;
		default:
			return nachbarW;
		}
	}
	
	public void setNachbar(Feld feld, Richtung richtung) {
		switch (richtung) {
		case NORDEN:
			this.nachbarN = feld;
			break;
		case OSTEN:
			this.nachbarO = feld;
			break;
		case SUEDEN:
			this.nachbarS = feld;
			break;
		default:
			this.nachbarW = feld;
			break;
		}
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
