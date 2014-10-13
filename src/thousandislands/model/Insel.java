package thousandislands.model;

import java.util.List;

public class Insel {
	private List<Feld> felder;
	
	public void addFeld(Feld feld) {
		felder.add(feld);
	}

	public List<Feld> getFelder() {
		return felder;
	}
	
	

//	public void setFelder(List<Feld> felder) {
//		this.felder = felder;
//	}

}
