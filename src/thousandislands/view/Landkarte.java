package thousandislands.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import thousandislands.model.Feld;

public class Landkarte extends JPanel{
	Feld[][] felder;
	
	public Landkarte(Feld[][] felder) {
		this.felder = felder;
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
			}
		}
	}
	
	private Color getFarbe(Feld feld) {
		if (feld.istPersonDa()) {
			return Color.MAGENTA;
		}
		switch (feld.getTyp()) {
		case MEER: return Color.BLUE;
		case STRAND: return Color.YELLOW;	
		case DSCHUNGEL: return Color.GREEN;
		case ZWECK: return Color.BLACK;
		case ROT: return Color.RED;
		case QUELLE: return Color.CYAN;
		case FRUECHTE: return Color.ORANGE;
		default: return Color.GRAY;
		}
	}

	
}
