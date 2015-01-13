package thousandislands.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import thousandislands.model.Feld;
import thousandislands.model.Person;
import thousandislands.model.Spieldaten;
import thousandislands.model.enums.Typ;
import thousandislands.model.enums.Zweck;

public class Landkarte extends JPanel{
	Feld[][] felder;
	Spieldaten spieldaten;
	
	public Landkarte(Spieldaten spieldaten) {
		this.felder = spieldaten.getFelder();
		this.spieldaten = spieldaten;
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

		if (feld.istPersonDa()) {
			if (spieldaten.hatFloss()) {
				return Color.ORANGE;
			} else {
				return Color.MAGENTA;				
			}
		}
		
		if (feld.hatFlaschenpost()) {
			return Color.CYAN;
		}

		switch (feld.getTyp()) {
		case MEER: return Color.BLUE;
		case STRAND: return Color.YELLOW;	
		case DSCHUNGEL: return Color.GREEN;
		case SCHATZ: return Color.GREEN;
		case ZWECK: return Color.BLACK;
		case ROT: return Color.RED;
		default: return Color.GRAY;
		}
		
	}
	
	private BufferedImage getBildchen(Feld feld) {
		BufferedImage bild = null;
		URL datei;
		String dateiname = "";
//		Person person = spieldaten.getPerson();

		if (feld.istPersonDa()) {
			dateiname = findeBildFuerPerson(spieldaten.getPerson(), feld);
		} else if (feld.getTyp() == Typ.ZWECK) {
			dateiname = findeBildFuerZweck(feld);			
		} else if (feld.istFlossDa()) {
			dateiname = "floss.png";
		} else if (feld.hatFlaschenpost()) {
			dateiname = "flasche1.png";
		}
		
		
		if (!dateiname.isEmpty()) {
	    	datei = getClass().getClassLoader().getResource(dateiname);
	    	
			try {
			    bild = ImageIO.read(datei);
			} catch (IOException e) {
				System.out.println(e);
			}			
		}
		
		return bild;
	}

	private String findeBildFuerZweck(Feld feld) {
		String dateiname = "";
		
    	switch(feld.getZweck()) {
		case WASSER: 
			dateiname = "quelle4.png";
			break;
		case NAHRUNG: 
			dateiname = "nahrung.png";
			break;
		case HOLZ: 
			dateiname = "holz.png";
			break;
		case LIANEN: 
			dateiname = "lianen.png";
			break;
		case TON: 
			dateiname = "ton.png";
			break;
		case FEUER: 
			dateiname = "feuer.png";
			break;
		case SCHILF: 
			dateiname = "schilf.png";
			break;
		case GROSSER_BAUM: 
			dateiname = "grosserbaum.png";
			break;
		case HUETTE: 
			dateiname = "huette.png";
			break;
		case PAPAYA: 
			dateiname = "papaya.png";
			break;
		case RUINE: 
			dateiname = "ruine.png";
			break;
		default: 
			dateiname = "leer.png";
			break;
		}			
		return dateiname;
	}

	private String findeBildFuerPerson(Person person, Feld feld) {
		String dateiname = "";
		
		switch (person.getFortbewegung()) {
		case SCHWIMMEN:
			dateiname = "schwimmer.png";
			break;
		case LAUFEN:
			if (feld.getTyp() == Typ.STRAND) {
				dateiname = "mann_strand.png";
			} else {
				dateiname = "mann_dschungel.png";				
			}
			break;
		case FLOSSFAHREN:
			//TODO: Bildchen fuer Mann auf Floss erstellen
			dateiname = "floss.png";
			break;
		}
		
		return dateiname;
	}
}
