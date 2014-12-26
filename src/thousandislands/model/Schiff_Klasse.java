package thousandislands.model;

public class Schiff_Klasse {
	private boolean rumpf;
	private boolean mast;
	private boolean kompass;
	private boolean wasser;
	private boolean nahrung;
	private boolean seile;
	private boolean segel;
	
	public boolean hasRumpf() {
		return rumpf;
	}
	public void setRumpf(boolean rumpf) {
		this.rumpf = rumpf;
	}
	public boolean hasMast() {
		return mast;
	}
	public void setMast(boolean mast) {
		this.mast = mast;
	}
	public boolean hasKompass() {
		return kompass;
	}
	public void setKompass(boolean kompass) {
		this.kompass = kompass;
	}
	public boolean hasWasser() {
		return wasser;
	}
	public void setWasser(boolean wasser) {
		this.wasser = wasser;
	}
	public boolean hasNahrung() {
		return nahrung;
	}
	public void setNahrung(boolean nahrung) {
		this.nahrung = nahrung;
	}
	public boolean hasSeile() {
		return seile;
	}
	public void setSeile(boolean seile) {
		this.seile = seile;
	}
	public boolean hasSegel() {
		return segel;
	}
	public void setSegel(boolean segel) {
		this.segel = segel;
	}

}
