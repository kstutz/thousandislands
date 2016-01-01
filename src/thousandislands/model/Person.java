package thousandislands.model;

import thousandislands.model.enums.Richtung;
import thousandislands.model.enums.Typ;

public class Person {
	private static final int MAX_WASSER = 100;
	private static final int MAX_WASSER_KRUG = 200;
	private static final int MAX_NAHRUNG = 100;
	private static final int MAX_NAHRUNG_KORB = 200;
	private static final int TRAGKRAFT_MIT_FLOSS = 10;

	private int level;
	private int wasser;
	private int nahrung;
	private Feld aktuellesFeld;
	private Feld vorigesFeld;
	private boolean hatSchatzkarte;
	private boolean hatFloss;
	private boolean hatKrug;
	private boolean hatKorb;
	private boolean hatSpeer;
	
	public Person(Feld feld) {
		vorigesFeld = feld;
		aktuellesFeld = feld;
		hatSchatzkarte = false;
		hatFloss = false;
		hatKrug = false;
		hatKorb = false;
		hatSpeer = false;
		wasser = 100;
		nahrung = 100;
		level = 0;
	}
	
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
			return MAX_WASSER_KRUG;
		} else {
			return MAX_WASSER;
		}
	}
	
	public int getMaxNahrung() {
		if (hatKorb()) {
			return MAX_NAHRUNG_KORB;
		} else {
			return MAX_NAHRUNG;
		}
	}
	
	public int getTragfaehigkeit() {
		return TRAGKRAFT_MIT_FLOSS;
	}
	
	public boolean bewegeNach(Richtung richtung) {
		Feld neuesFeld = null;
		switch(richtung) {
		case NORDEN:
			neuesFeld = aktuellesFeld.getNachbar(Richtung.NORDEN);
			break;
		case OSTEN:
			neuesFeld = aktuellesFeld.getNachbar(Richtung.OSTEN);
			break;
		case SUEDEN:
			neuesFeld = aktuellesFeld.getNachbar(Richtung.SUEDEN);
			break;
		case WESTEN:
			neuesFeld = aktuellesFeld.getNachbar(Richtung.WESTEN);		
			break;
		}
		return setzePersonWeiter(neuesFeld);		
	}
	
	public void wasserAbziehen() {
		wasser--;		
	}

	public void nahrungAbziehen() {
		nahrung--;
	}

	public Feld getAktuellesFeld() {
		return aktuellesFeld;
	}
	
	public Feld getVorigesFeld() {
		return vorigesFeld;
	}

	private boolean setzePersonWeiter(Feld neuesFeld) {
		
		if (neuesFeld == null) {
			return false;
		}
		if (aktuellesFeld.getTyp() == Typ.MEER && neuesFeld.getTyp() == Typ.DSCHUNGEL) {
			return false;
		}
		if (aktuellesFeld.getTyp() == Typ.DSCHUNGEL && neuesFeld.getTyp() == Typ.MEER) {
			return false;
		}
		
		//Floss mitbewegen
		if (aktuellesFeld.istFlossDa() && neuesFeld.getTyp() == Typ.MEER) {
			aktuellesFeld.setFlossDa(false);
			neuesFeld.setFlossDa(true);
		}
		
		//Person bewegen
		aktuellesFeld.setPersonDa(false);
		neuesFeld.setPersonDa(true);
		vorigesFeld = aktuellesFeld;
		aktuellesFeld = neuesFeld;
		return true;
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
