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
				g.setColor(getFarbe(felder[i][j]));
				g.fillRect(i*10, j*10, 10, 10);
				
				if (felder[i][j].getZweck() != null) {
					BufferedImage bild = getBildchen(felder[i][j].getZweck());
					g.drawImage(bild, i*10, j*10, null);
				}				
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
	
	private BufferedImage getBildchen(Zweck zweck) {
		BufferedImage bild = null;
		String pfad = "../../img/";
		URL datei;
		
//		try {
//			System.out.println(getClass().getResource("../../img/quelle2.png").toURI());
//		} catch (URISyntaxException ex) {} 

    	switch(zweck) {
		case WASSER: 
			datei = getClass().getResource(pfad + "quelle4.png");
			break;
		case NAHRUNG: 
			datei = getClass().getResource(pfad + "nahrung.png");
			break;
		case HOLZ: 
			datei = getClass().getResource(pfad + "holz.png");
			break;
		case LIANEN: 
			datei = getClass().getResource(pfad + "lianen.png");
			break;
		case TON: 
			datei = getClass().getResource(pfad + "ton.png");
			break;
		case FEUER: 
			datei = getClass().getResource(pfad + "feuer.png");
			break;
		case SCHILF: 
			datei = getClass().getResource(pfad + "schilf.png");
			break;
		case GROSSER_BAUM: 
			datei = getClass().getResource(pfad + "grosserbaum.png");
			break;
		case HUETTE: 
			datei = getClass().getResource(pfad + "huette.png");
			break;
		case PAPAYA: 
			datei = getClass().getResource(pfad + "papaya.png");
			break;
		case RUINE: 
			datei = getClass().getResource(pfad + "ruine.png");
			break;
		default: 
			datei = getClass().getResource(pfad + "leer.png");
			break;
		}
    	
		try {
		    bild = ImageIO.read(datei);
		} catch (IOException e) {
			System.out.println(e);
		}
		
		return bild;
	}
}
