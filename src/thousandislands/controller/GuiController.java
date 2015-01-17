package thousandislands.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import thousandislands.model.Feld;
import thousandislands.model.Spieldaten;
import thousandislands.model.enums.Aktion;
import thousandislands.view.Fenster;
import thousandislands.view.Landkarte;
import thousandislands.view.RechteSpalte;
import thousandislands.view.Schatzkarte;

public class GuiController implements ActionListener {
	private Landkarte spielfeld;
	private RechteSpalte rechteSpalte;
	private JButton knopfFuerAlles;
	private JButton kartenknopf;
	private JLabel nachrichtenzeile;
	private Schatzkarte schatzkarte;
	private Spieldaten daten;
	private Fenster fenster;

	public GuiController(Spieldaten daten){
		this.daten = daten;
		erstelleGui();
	}
	
	private void erstelleGui() {
		//Fenster erstellen
		fenster = new Fenster();		
		
		//Spielfeld hinzufuegen
		spielfeld = new Landkarte(daten);
		fenster.add(spielfeld, BorderLayout.CENTER);
		spielfeld.repaint();
		
		//rechte Spalte und Knoepfe hinzufuegen
		rechteSpalte = new RechteSpalte();

		knopfFuerAlles = new JButton();
//		knopfFuerAlles.setVisible(false);
		rechteSpalte.add(knopfFuerAlles);

		rechteSpalte.add(Box.createRigidArea(new Dimension(0,10)));

		kartenknopf = new JButton("Schatzkarte");
		kartenknopf.setVisible(false);
		kartenknopf.setActionCommand("KARTE");
		kartenknopf.addActionListener(this);
		rechteSpalte.add(kartenknopf);
		
		fenster.add(rechteSpalte, BorderLayout.EAST);
		
		//Nachrichtenzeile hinzufuegen
		nachrichtenzeile = new JLabel();
		fenster.add(nachrichtenzeile, BorderLayout.SOUTH);
	}

	public void erstelleSchatzkarte(Feld startfeld) {
		schatzkarte = new Schatzkarte(daten.getFelder(), startfeld);
	}
	
	public void aktualisiere() {
		spielfeld.repaint();
		rechteSpalte.setzeWasseranzeige(daten.getWasser());
		rechteSpalte.setzeNahrungsanzeige(daten.getNahrung());
		nachrichtenzeile.setText(" ");
//		knopfFuerAlles.setText(" ");
		knopfFuerAlles.setVisible(false);
	}
	
	public void zeigeNachricht(String s) {
		nachrichtenzeile.setText(s);
	}
	
	public void kartenknopfSichtbar(boolean bool) {
		kartenknopf.setVisible(bool);
	}	
	
	public void setzeKnopf(Aktion aktion) {
		knopfFuerAlles.setVisible(true);
		
		switch(aktion) {
		
		case HOLZ_MITNEHMEN:
			knopfFuerAlles.setText("Holz mitnehmen");
			break;
		case LIANEN_MITNEHMEN:
			knopfFuerAlles.setText("Lianen mitnehmen");
			break;
		case KRUG_FUELLEN:
			knopfFuerAlles.setText("Krug fuellen");
			break;
		case KORB_FUELLEN:
			knopfFuerAlles.setText("Korb fuellen");
			break;
		case KRUG_FORMEN:
			knopfFuerAlles.setText("Krug formen");
			break;			
		case FEUER_MACHEN:
			knopfFuerAlles.setText("Feuer machen");
			break;
		case KRUG_BRENNEN:
			knopfFuerAlles.setText("Krug brennen");
			break;
		case KORB_FLECHTEN:
			knopfFuerAlles.setText("Korb flechten");
			break;
		case PAPAYA_MITNEHMEN:
			knopfFuerAlles.setText("Papaya mitnehmen");
			break;
		case TROTZDEM_MITNEHMEN:
			knopfFuerAlles.setText("Trotzdem mitnehmen");
			break;
		case BAUM_FAELLEN:
			knopfFuerAlles.setText("Baum fällen");
			break;
		case SEGEL_MITNEHMEN:
			knopfFuerAlles.setText("Segel mitnehmen");
			break;
		case FLOSS_BAUEN:
			knopfFuerAlles.setText("Floß bauen");
			break;
		case RUINEN_DURCHSUCHEN:
			knopfFuerAlles.setText("Ruinen durchsuchen");
			break;
		case KOMPASS_MITNEHMEN:
			knopfFuerAlles.setText("Kompass mitnehmen");
			break;
			
		default:
			break;
		
		}
		knopfFuerAlles.setActionCommand(aktion.toString());

	}

	public void actionListenerHinzufuegen(Controller controller) {
		knopfFuerAlles.addActionListener(controller);
	}
	
	public void keyListenerHinzufuegen(Controller controller) {
		fenster.addKeyListener(controller);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		schatzkarte.setVisible(true);
		fenster.requestFocus();
	}

	public void knopfFuerAllesSichtbar(boolean bool) {
		knopfFuerAlles.setVisible(bool);
	}
	
	public void setzeTeileliste(int level) {
		if (level == 1) {
			rechteSpalte.flossteileHinzufuegen();
		} else {
			rechteSpalte.schiffsteileHinzufuegen();
		}
	}	

	public void fokusHolen() {
		fenster.requestFocus();		
	}

	public void markiereTeil(String teil) {
		rechteSpalte.teilAlsGefundenMarkieren(teil);
	}
}
