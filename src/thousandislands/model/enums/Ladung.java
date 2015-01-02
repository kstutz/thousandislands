package thousandislands.model.enums;

public enum Ladung {
	LIANE (1),
	HOLZ (5),
	TON (2),
	KOMPASS (1),
	KRUG_LEER (2),
	KRUG_VOLL (2),
	KORB_LEER (2),
	KORB_VOLL (2),
	MAST (5),
	PAPAYA (2),
	WERKZEUG (3), 
	WAFFEN (3), 
	SEGEL (0);

	private final int gewicht;

	Ladung (int gewicht) {
		this.gewicht = gewicht;
	}
	
	public int getGewicht() {
		return gewicht;
	}
}
