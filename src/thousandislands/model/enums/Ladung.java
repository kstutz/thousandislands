package thousandislands.model.enums;

public enum Ladung {
	LIANE (1),
	SEILE (1),
	HOLZ (5),
	RUMPF (5),
	KRUG_UNGEBRANNT (2),
	KOMPASS (1),
	KRUG (2),
	KORB (2),
	WASSER (1),
	NAHRUNG (1),
//	KRUG_LEER (2),
//	KRUG_VOLL (2),
//	KORB_LEER (2),
//	KORB_VOLL (2),
	MAST (5),
	PAPAYA (2),
	WERKZEUG (3), 
	SEGEL (1);

	private final int gewicht;

	Ladung (int gewicht) {
		this.gewicht = gewicht;
	}
	
	public int getGewicht() {
		return gewicht;
	}
	
	@Override
	public String toString() {
		
		if (this == KRUG_UNGEBRANNT) {
			return "KRUG (UNGEBRANNT)";
		} else {
			return this.name();
		}
	}
}
