package thousandislands.model;

import thousandislands.model.enums.Typ;

public class Person {
	private int wasser = 100;
	private int nahrung = 100;
	private int maxWasser = 100;
	private int maxNahrung = 100;
	private Feld aktuellesFeld;
	
	public Person(Feld feld) {
		aktuellesFeld = feld;
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

}
