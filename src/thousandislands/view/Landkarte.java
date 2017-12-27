package thousandislands.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import thousandislands.model.Feld;
import thousandislands.model.Spielfeld;
import thousandislands.model.enums.Typ;

public class Landkarte extends JPanel{
	Feld[][] felder;
	Spielfeld spielfeld;

	public Landkarte(Spielfeld spielfeld) {
		this.spielfeld = spielfeld;
		felder = spielfeld.getFelder();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics = (Graphics2D)g;
		
		spielfeldZeichnen(graphics);
	}
	
	private void spielfeldZeichnen(Graphics g) {
		for (int i=0; i<100; i++) {
			for (int j=0; j<60; j++) {
				
				BufferedImage bild = getBildchen(felder[i][j]);
				if (bild != null) {
					g.drawImage(bild, i*10, j*10, null);
				} else {
					g.setColor(getFarbe(felder[i][j]));
					g.fillRect(i*10, j*10, 10, 10);					
				}
				
				
//				if (felder[i][j].getZweck() != null) {
//					BufferedImage bild = getBildchen(felder[i][j].getZweck());
//					g.drawImage(bild, i*10, j*10, null);
//				}				
			}
		}
	}

	private Color getFarbe(Feld feld) {

		if (spielfeld.getAktuellesFeldPerson().equals(feld)) {
			if (feld.equals(spielfeld.getFlossFeld())) {
				return Color.ORANGE;
			} else {
				return Color.MAGENTA;
			}
		}

		if (feld.equals(spielfeld.getFlaschenpostFeld())) {
			return Color.CYAN;
		}

		switch (feld.getTyp()) {
		case MEER: return Color.BLUE;
		case STRAND: return Color.YELLOW;
		case SCHIFFBAUSTRAND: return Color.YELLOW;
		case DSCHUNGEL: return Color.GREEN;
		case SCHATZ: return Color.GREEN;
		case ZWECK: return Color.BLACK;
		case WRACK: return Color.RED;
		case ROT: return Color.RED;
		default: return Color.GRAY;
		}
	}

	private BufferedImage getBildchen(Feld feld) {
		BufferedImage bild = null;
		URL datei;
		String dateiname = "";

		if (spielfeld.getAktuellesFeldPerson().equals(feld)) {
			dateiname = findeBildFuerPerson(feld);
		} else if (feld.getTyp() == Typ.ZWECK) {
			dateiname = feld.getZweck().getDateiname();
		} else if (spielfeld.getFlossFeld() != null && spielfeld.getFlossFeld().equals(feld)) {
			if (feld.getTyp() == Typ.STRAND) {
				dateiname = "floss_strand.png";
			} else {
				dateiname = "floss.png";
			}
		} else if (feld.equals(spielfeld.getFlaschenpostFeld())) {
			dateiname = "flasche1.png";
		} else if (feld.getTyp() == Typ.WRACK) {
			dateiname = "wrack.png";
		} else if (feld.getTyp() == Typ.SCHIFFBAUSTRAND 
				&& !feld.getLadung().isEmpty()) {
			dateiname = "haufen.png";
		} else if (feld.getTyp() == Typ.SCHATZ_GEFUNDEN) {
			dateiname = "schatz.png";
		}
		
		if (!dateiname.isEmpty()) {
	    	datei = getClass().getClassLoader().getResource("img/"+dateiname);
	    	
			try {
			    bild = ImageIO.read(datei);
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		
		return bild;
	}

	private String findeBildFuerPerson(Feld feld) {
		if (feld.getTyp() == Typ.MEER) {
			if (spielfeld.getAktuellesFeldPerson().equals(spielfeld.getFlossFeld())) {
				return "mann_floss.png";
			} else {
				return "schwimmer1.png";				
			}
		} else {
			if (feld.getTyp() == Typ.STRAND || feld.getTyp() == Typ.SCHIFFBAUSTRAND) {
				return "mann_strand.png";
			} else if (feld.getTyp() == Typ.WRACK){
				return "schwimmer1.png";		
			} else {
				return "mann_dschungel.png";				
			}
		}
	}
}
