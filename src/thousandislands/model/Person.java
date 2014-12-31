package thousandislands.model;

import thousandislands.model.enums.Typ;

public class Person {
	private int wasser;
	private int nahrung;
	private int maxWasser;
	private int maxNahrung;
	private Feld aktuellesFeld;
	private boolean hatSchatzkarte;
	private boolean hatFloss;
	private boolean hatKrug;
	private boolean hatKorb;
	
	public Person(Feld feld) {
		aktuellesFeld = feld;
		hatSchatzkarte = false;
		hatFloss = false;
		hatKrug = false;
		hatKorb = false;
		wasser = 100;
		nahrung = 100;
		maxWasser = 100;
		maxNahrung = 100;
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
		return maxWasser;
	}
	
	public void setMaxWasser(int maxWasser) {
		this.maxWasser = maxWasser;
	}
	
	public int getMaxNahrung() {
		return maxNahrung;
	}
	
	public void setMaxNahrung(int maxNahrung) {
		this.maxNahrung = maxNahrung;
	}

	public boolean bewegeNachO() {
		Feld neuesFeld = aktuellesFeld.getNachbarO();		
		return setzePersonWeiter(neuesFeld);
	}

	public boolean bewegeNachW() {
		Feld neuesFeld = aktuellesFeld.getNachbarW();
		return setzePersonWeiter(neuesFeld);
	}

	public boolean bewegeNachN() {
		Feld neuesFeld = aktuellesFeld.getNachbarN();
		return setzePersonWeiter(neuesFeld);
	}

	public boolean bewegeNachS() {
		Feld neuesFeld = aktuellesFeld.getNachbarS();
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

}
