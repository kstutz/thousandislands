package thousandislands.model;

import java.util.ArrayList;
import java.util.List;

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
	private boolean hatFlaschenpost = false;
	private int x;
	private int y;
	private Feld nachbarO;
	private Feld nachbarW;
	private Feld nachbarN;
	private Feld nachbarS;
	
	public Feld (int x, int y) {
		status = Status.ENTDECKT;
//		status = Status.UNENTDECKT;
		typ = Typ.MEER;
//		typ = Typ.VOR_MEER;
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

	public Feld getNachbarO() {
		return nachbarO;
	}

	public void setNachbarO(Feld feld) {
		this.nachbarO = feld;
	}

	public Feld getNachbarW() {
		return nachbarW;
	}

	public void setNachbarW(Feld feld) {
		this.nachbarW = feld;
	}

	public Feld getNachbarN() {
		return nachbarN;
	}
	
	public void setNachbarN(Feld feld) {
		this.nachbarN = feld;
	}

	public Feld getNachbarS() {
		return nachbarS;
	}

	public void setNachbarS(Feld feld) {
		this.nachbarS = feld;
	}

	public boolean hatFlaschenpost() {
		return hatFlaschenpost;
	}

	public Zweck getZweck() {
		return zweck;
	}

	public void setZweck(Zweck zweck) {
		this.zweck = zweck;
	}

//	public void setSchatz(boolean hatSchatz) {
//		this.hatSchatz = hatSchatz;
//	}
//
//	public boolean hatSchatz() {
//		return hatSchatz;
//	}
}
