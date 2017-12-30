package thousandislands.model.enums;

public enum Aktion {
	HOLZ_MITNEHMEN ("Holz mitnehmen"),
	LIANEN_MITNEHMEN ("Lianen mitnehmen"),
	KRUG_FUELLEN ("Krug füllen"),
	KORB_FUELLEN ("Korb füllen"),
	KRUG_FORMEN ("Krug formen"),
	FEUER_MACHEN ("Feuer machen"),
	KRUG_BRENNEN ("Krug brennen"),
	KORB_FLECHTEN ("Korb flechten"),
	PAPAYA_MITNEHMEN ("Papaya mitnehmen"),
	TROTZDEM_MITNEHMEN ("<html>Trotzdem<br/>mitnehmen</html>"),
	BAUM_FAELLEN ("Baum fällen"),
	FLOSS_BAUEN ("Floß bauen"),
	RUINEN_DURCHSUCHEN ("Ruinen durchsuchen"),
	KOMPASS_MITNEHMEN ("Kompass mitnehmen"),
	WRACK_DURCHSUCHEN ("Wrack durchsuchen"),
	WERKZEUG_MITNEHMEN ("<html>Werkzeug<br/>mitnehmen</html>"),
	OEFFNEN ("Kiste öffnen"),
	SEGEL_MITNEHMEN ("Segel mitnehmen"),
	ABLADEN ("Abladen"),
	SCHIFF_BAUEN ("Schiff bauen");

	private final String knopftext;

	Aktion (String knopftext) {
		this.knopftext = knopftext;
	}

	public String getKnopftext() {
		return knopftext;
	}
}
