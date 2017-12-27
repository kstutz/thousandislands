package thousandislands.model.enums;

public enum Zweck {
	
	WASSER ("quelle.png"),
	NAHRUNG ("nahrung.png"),
	HOLZ ("holz.png"),
	LIANEN ("lianen.png"),
	TON ("ton.png"),
	FEUER ("feuer.png"),
	SCHILF ("schilf.png"),
	HUETTE ("huette.png"),
	PAPAYA ("papaya.png"),
	RUINE ("ruine.png"),
	GROSSER_BAUM ("grosserbaum.png"),
	BAUMSTUMPF ("baumstumpf2.png"),
	SCHIFFBAU ("leer.png"),
	OFFEN ("fragezeichen.png"),
	LEER ("leer.png");

	private final String dateiname;

	Zweck (String dateiname) {
		this.dateiname = dateiname;
	}

	public String getDateiname() {
		return dateiname;
	}
}
