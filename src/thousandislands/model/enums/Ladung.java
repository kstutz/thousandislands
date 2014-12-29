package thousandislands.model.enums;

public enum Ladung {
	LIANE (1),
	HOLZ (5),
	TON (2),
	KOMPASS (1),
	KRUG (2),
	KORB (2),
	MAST (5),
	PAPAYA (2),
	WERKZEUG (3);
	
	private final int gewicht;

	Ladung (int gewicht) {
		this.gewicht = gewicht;
	}
	
	public int getGewicht() {
		return gewicht;
	}
}
