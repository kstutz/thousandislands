package thousandislands.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JDialog;
import javax.swing.JPanel;

import thousandislands.model.Feld;

public class Schatzkarte extends JDialog{
	private Feld[][] felder;
	private int xDazu;
	private int yDazu;
	
	public Schatzkarte(Feld[][] felder, Feld startfeld) {
		this.felder = felder;
		xDazu = startfeld.getX();
		yDazu = startfeld.getY();

		KartenPanel kartenPanel = new KartenPanel();
		add(kartenPanel);

		setModal(true);
		setSize(new Dimension(240, 240));
		setLocationRelativeTo(null);
	}
	
	private class KartenPanel extends JPanel {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D graphics = (Graphics2D)g;
			
			karteZeichnen(graphics);
		}
		
		private void karteZeichnen(Graphics g) {
			for (int i=0; i<12; i++) {
				for (int j=0; j<12; j++) {
					g.setColor(getFarbe(felder[xDazu + i][yDazu + j]));
					g.fillRect(i*20, j*20, 20, 20);
				}
			}
		}
		
		private Color getFarbe(Feld feld) {
			switch (feld.getTyp()) {
			case MEER: return Color.LIGHT_GRAY;
			case STRAND: return Color.GRAY;	
			case DSCHUNGEL: return Color.DARK_GRAY;
//			case ZWECK: return Color.BLACK;
			case SCHATZ: return Color.RED;
			default: return Color.DARK_GRAY;
			}
		}
	}	
}
