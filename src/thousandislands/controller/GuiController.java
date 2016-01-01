package thousandislands.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import thousandislands.model.Feld;
import thousandislands.model.Spieldaten;
import thousandislands.model.enums.Aktion;
import thousandislands.model.enums.Ladung;
import thousandislands.model.enums.Zweck;
import thousandislands.view.Fenster;
import thousandislands.view.Landkarte;
import thousandislands.view.RechteSpalte;
import thousandislands.view.Schatzkarte;

public class GuiController implements ActionListener {
	private Landkarte spielfeld;
	private RechteSpalte rechteSpalte;
	private JButton knopfFuerAlles;
	private JButton kartenknopf;
	private JButton speicherknopf;
	private JButton ladeknopf;
	private JButton nochmalknopf;
	private JPanel knopfzeile;
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
		GridBagConstraints c = new GridBagConstraints();

		knopfFuerAlles = new JButton();
		knopfFuerAlles.setVisible(false);
		c.gridy = 7;
		c.insets = new Insets(5,0,5,0);
		rechteSpalte.add(knopfFuerAlles, c);

		kartenknopf = new JButton("<html>Schatzkarte<br />anschauen</html>");
		kartenknopf.setVisible(false);
		kartenknopf.setActionCommand("KARTE");
		kartenknopf.addActionListener(this);
		c.gridy = 8;
		c.insets = new Insets(5,0,5,0);
		rechteSpalte.add(kartenknopf, c);
		
		fenster.add(rechteSpalte, BorderLayout.EAST);
		
		speicherknopf = new JButton("Speichern");
		speicherknopf.setActionCommand("SPEICHERN");

		ladeknopf = new JButton("Laden");
		ladeknopf.setActionCommand("LADEN");
		
		nochmalknopf = new JButton("Nochmal spielen!");
		nochmalknopf.setActionCommand("NOCHMAL");

		knopfzeile = new JPanel();
		knopfzeile.add(speicherknopf);
		knopfzeile.add(ladeknopf);
		
		fenster.add(knopfzeile, BorderLayout.NORTH);
	}

	public void erstelleSchatzkarte(Feld startfeld) {
		schatzkarte = new Schatzkarte(daten.getFelder(), startfeld);
	}
	
	public void aktualisiere() {
		spielfeld.repaint();
		rechteSpalte.setzeWasseranzeige(daten.getWasser());
		rechteSpalte.setzeNahrungsanzeige(daten.getNahrung());
		rechteSpalte.setzeFlossBeladung(daten.getInventar().getGesamtgewicht(), daten.getPerson().getTragfaehigkeit());
	}
	
	public void zeigeNachricht(String s) {
		rechteSpalte.zeigeNachricht(s);
	}
	
	public void loescheNachrichten() {
		rechteSpalte.zeigeNachricht(" ");		
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
			knopfFuerAlles.setText("<html>Trotzdem<br/>mitnehmen</html>");
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
		case WRACK_DURCHSUCHEN:
			knopfFuerAlles.setText("Wrack durchsuchen");
			break;
		case WERKZEUG_MITNEHMEN:
			knopfFuerAlles.setText("<html>Werkzeug<br/>mitnehmen</html>");
			break;
		case ABLADEN:
			knopfFuerAlles.setText("Abladen");
			break;
		case SCHIFF_BAUEN:
			knopfFuerAlles.setText("Schiff bauen");
			break;
		default:
			break;
		
		}
		knopfFuerAlles.setActionCommand(aktion.toString());
	}

	public void actionListenerHinzufuegen(Controller controller) {
		knopfFuerAlles.addActionListener(controller);
		speicherknopf.addActionListener(controller);
		ladeknopf.addActionListener(controller);
		nochmalknopf.addActionListener(controller);
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
	
	public void setzeTeileliste(Set<Ladung> noetigeTeile) {
		rechteSpalte.teileHinzufuegen(noetigeTeile);
	}
	
	public void hosentascheHinzufuegen(Ladung teil) {
		rechteSpalte.zuHosentascheHinzufügen(teil);
	}
	
	public void hosentascheEntfernen(Ladung teil) {
		rechteSpalte.ausHosentascheEntfernen(teil);
	}
	
	public void fokusHolen() {
		fenster.requestFocus();		
	}

	public void markiereTeil(String teil) {
		rechteSpalte.teilAlsGefundenMarkieren(teil);
	}
	
	public void setzeBaumstumpf() {
		daten.getPerson().getAktuellesFeld().setZweck(Zweck.BAUMSTUMPF);
	}
	
	public void spielende() {
		fenster.remove(spielfeld);
		fenster.remove(rechteSpalte);
		fenster.remove(knopfzeile);
		
		//TODO: huebscher machen
		fenster.add(new JLabel("ENDE"));
		fenster.add(nochmalknopf);
	}
}