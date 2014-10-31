package thousandislands.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import thousandislands.model.Spieldaten;

public class GUI extends JFrame{
	private Landkarte spielfeld;
	private RechteSpalte rechteSpalte;
	private Spieldaten daten;
	

	public GUI(Spieldaten daten){
		setSize(new Dimension(1100, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Tausend Inseln");

		this.daten = daten;		
		macheStartklar();
		
		setVisible(true);
		setFocusable(true);
	    requestFocus();
	}
	
	private void macheStartklar() {
		//Spielfeld hinzufuegen
		spielfeld = new Landkarte(daten.getFelder());
		add(spielfeld, BorderLayout.CENTER);
		spielfeld.repaint();
		
		//rechte Spalte hinzufuegen
		rechteSpalte = new RechteSpalte();
		add(rechteSpalte, BorderLayout.EAST);		
	}
	
	public void aktualisiere() {
		spielfeld.repaint();
		rechteSpalte.setzeWasseranzeige(daten.getWasser());
		rechteSpalte.setzeNahrungsanzeige(daten.getNahrung());
	}
}
