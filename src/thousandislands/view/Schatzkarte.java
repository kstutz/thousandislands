package thousandislands.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JDialog;
import javax.swing.JPanel;

import thousandislands.model.Feld;

public class Schatzkarte extends JDialog{
	Feld[][] felder;
	
	public Schatzkarte(Feld[][] felder) {
		this.felder = felder;
		KartenPanel kartenPanel = new KartenPanel();
		this.add(kartenPanel);
		
//		setSize(new Dimension(400, 400));
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
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
					g.setColor(getFarbe(felder[i][j]));
					g.fillRect(i*20, j*20, 20, 20);
				}
			}
		}
		
		private Color getFarbe(Feld feld) {
			switch (feld.getTyp()) {
			case MEER: return Color.WHITE;
			case STRAND: return Color.LIGHT_GRAY;	
			case DSCHUNGEL: return Color.GRAY;
			case ZWECK: return Color.DARK_GRAY;
			case SCHATZ: return Color.RED;
			default: return Color.GRAY;
			}
		}
	}	
}
