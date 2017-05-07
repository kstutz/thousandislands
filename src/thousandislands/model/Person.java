package thousandislands.model;

import thousandislands.controller.Konfiguration;
import thousandislands.model.enums.Richtung;
import thousandislands.model.enums.Typ;

import javax.xml.bind.annotation.XmlElement;

public class Person {

	private int level;
	private int wasser;
	private int nahrung;
	private boolean hatSchatzkarte;
	private boolean hatFloss;
	private boolean hatKrug;
	private boolean hatKorb;
	private boolean hatSpeer;
	
	public Person() {
		hatSchatzkarte = false;
		hatFloss = false;
		hatKrug = false;
		hatKorb = false;
		hatSpeer = false;
		wasser = 100;
		nahrung = 100;
		level = 0;
	}

//	//noetig fuer JAXB
//	public Person() {}

	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
		
	public int getWasser() {
		return wasser;
	}

	public void setWasser(int wasser) {
		this.wasser = wasser;
	}

	public int getNahrung() {
		return nahrung;
	}

	public void setNahrung(int nahrung) {
		this.nahrung = nahrung;
	}

	public int getMaxWasser() {
		if (hatKrug()) {
			return Konfiguration.MAX_WASSER_KRUG;
		} else {
			return Konfiguration.MAX_WASSER;
		}
	}
	
	public int getMaxNahrung() {
		if (hatKorb()) {
			return Konfiguration.MAX_NAHRUNG_KORB;
		} else {
			return Konfiguration.MAX_NAHRUNG;
		}
	}
	
	public int getTragfaehigkeit() {
		return Konfiguration.TRAGKRAFT_MIT_FLOSS;
	}
	

	public void wasserAbziehen() {
		wasser--;		
	}

	public void nahrungAbziehen() {
		nahrung--;
	}

	public boolean hatSchatzkarte() {
		return hatSchatzkarte;
	}
	
	public void kriegtSchatzkarte() {
		hatSchatzkarte = true;
	}

	public boolean hatFloss() {
		return hatFloss;
	}

	public void setFloss(boolean hatFloss) {
		this.hatFloss = hatFloss;
	}

	public boolean hatKrug() {
		return hatKrug;
	}

	public void setKrug(boolean hatKrug) {
		this.hatKrug = hatKrug;
	}

	public boolean hatKorb() {
		return hatKorb;
	}

	public void setKorb(boolean hatKorb) {
		this.hatKorb = hatKorb;
	}

	public boolean hatSpeer() {
		return hatSpeer;
	}

	public void setSpeer(boolean hatSpeer) {
		this.hatSpeer = hatSpeer;
	}
}
