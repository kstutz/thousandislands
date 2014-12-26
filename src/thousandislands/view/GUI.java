package thousandislands.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;

import thousandislands.model.Spieldaten;

public class GUI extends JFrame{
	private Landkarte spielfeld;
	private RechteSpalte rechteSpalte;
	private JLabel nachrichtenzeile;
	private Spieldaten daten;
	
	public GUI(Spieldaten daten){
		setSize(new Dimension(1100, 630));
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
		
		//Nachrichtenzeile hinzufuegen
		nachrichtenzeile = new JLabel();
		add(nachrichtenzeile, BorderLayout.SOUTH);
	}
	
	public void aktualisiere() {
		spielfeld.repaint();
		rechteSpalte.setzeWasseranzeige(daten.getWasser());
		rechteSpalte.setzeNahrungsanzeige(daten.getNahrung());
//		rechteSpalte.knopfFuerAllesSichtbar(false);
	}
	
	public void zeigeNachricht(String s) {
		nachrichtenzeile.setText(s);
	}
	
	public void beschrifteKnopf(String text) {
		rechteSpalte.knopfFuerAllesText(text);
	}
}
