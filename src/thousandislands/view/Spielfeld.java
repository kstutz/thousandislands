package thousandislands.view;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import thousandislands.model.Feld;

public class Spielfeld extends JPanel{
	Feld[][] felder;
	
	public Spielfeld(Feld[][] felder) {
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
				g.setColor(felder[i][j].getFarbe());
				g.fillRect(i*10, j*10, 10, 10);
			}
		}
	}
}
