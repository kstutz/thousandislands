package thousandislands.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import thousandislands.model.enums.Status;
import thousandislands.model.enums.Typ;

public class Feld {
	private Status status;
	private Typ typ;
	private List<Feld> nachbarn = new ArrayList<Feld>();
	private List<Feld> direkteNachbarn = new ArrayList<Feld>();
	private int x;
	private int y;
	
	public Feld (int x, int y) {
		status = Status.ENTDECKT;
//		status = Status.UNENTDECKT;
		typ = Typ.VOR_MEER;
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
	
	public Color getFarbe() {
		switch (typ) {
		case MEER: return Color.BLUE;
		case STRAND: return Color.YELLOW;	
		case DSCHUNGEL: return Color.GREEN;
		case ZENTRUM: return Color.BLACK;
		default: return Color.GRAY;
		}
	}
}
