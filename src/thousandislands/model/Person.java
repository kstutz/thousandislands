package thousandislands.model;

import thousandislands.model.enums.Fortbewegung;
import thousandislands.model.enums.Richtung;
import thousandislands.model.enums.Typ;

public class Person {
	private static final int MAX_WASSER = 100;
	private static final int MAX_WASSER_KRUG = 200;
	private static final int MAX_NAHRUNG = 100;
	private static final int MAX_NAHRUNG_KORB = 200;
	private static final int TRAGKRAFT_SCHWIMMEND = 1;
	private static final int TRAGKRAFT_MIT_FLOSS = 10;
	
	private int wasser;
	private int nahrung;
	private Feld aktuellesFeld;
	private Feld vorigesFeld;
	private Fortbewegung fortbewegung;
	private boolean hatSchatzkarte;
	private boolean hatFloss;
	private boolean hatKrug;
	private boolean hatKorb;
	private boolean hatWaffen;
	
	public Person(Feld feld) {
		vorigesFeld = feld;
		aktuellesFeld = feld;
		hatSchatzkarte = false;
		hatFloss = false;
		hatKrug = false;
		hatKorb = false;
		hatWaffen = false;
		fortbewegung = Fortbewegung.SCHWIMMEN;
		wasser = 100;
		nahrung = 100;
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
		if (hatFloss) {
			return TRAGKRAFT_MIT_FLOSS;
		} else {
			return TRAGKRAFT_SCHWIMMEND;
		}
	}
	
	public boolean bewegeNach(Richtung richtung) {
		Feld neuesFeld = null;
		switch(richtung) {
		case NORDEN:
			neuesFeld = aktuellesFeld.getNachbarN();
			break;
		case OSTEN:
			neuesFeld = aktuellesFeld.getNachbarO();		
			break;
		case SUEDEN:
			neuesFeld = aktuellesFeld.getNachbarS();		
			break;
		case WESTEN:
			neuesFeld = aktuellesFeld.getNachbarW();		
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

	public boolean hatWaffen() {
		return hatWaffen;
	}

	public void setWaffen(boolean hatWaffen) {
		this.hatWaffen = hatWaffen;
	}

	public Fortbewegung getFortbewegung() {
		return fortbewegung;
	}

	public void setFortbewegung(Fortbewegung fortbewegung) {
		this.fortbewegung = fortbewegung;
	}

}
