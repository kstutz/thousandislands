package thousandislands.controller;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import thousandislands.model.*;
import thousandislands.model.enums.Aktion;
import thousandislands.model.enums.Ladung;
import thousandislands.model.enums.Zweck;
import thousandislands.view.Fenster;
import thousandislands.view.Landkarte;
import thousandislands.view.RechteSpalte;
import thousandislands.view.Schatzkarte;

public class GuiController implements ActionListener {
	private Landkarte landkarte;
	private RechteSpalte rechteSpalte;
	private JButton knopfFuerAlles;
	private JButton kartenknopf;
	private JButton speicherknopf;
	private JButton ladeknopf;
	private JButton neuknopf;
	private JButton nochmalknopf;
	private JPanel knopfzeile;
	private Schatzkarte schatzkarte;
	private Spielfeld spielfeld;
	private Person person;
	private Inventar inventar;
	private Fenster fenster;

	public GuiController(Spielfeld spielfeld, Person person, Inventar inventar){
		this.spielfeld = spielfeld;
		this.person = person;
		this.inventar = inventar;

		fenster = new Fenster();
		erstelleGui();
	}

	public void setSpielfeld(Spielfeld spielfeld) {
		this.spielfeld = spielfeld;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public void setInventar(Inventar inventar) {
		this.inventar = inventar;
	}

	public void resetGui(Spielfeld spielfeld, Person person, Inventar inventar) {
		this.spielfeld = spielfeld;
		this.person = person;
		this.inventar = inventar;

		erstelleGui();
	}

	private void erstelleGui() {

		//Spielfeld hinzufuegen
		landkarte = new Landkarte(spielfeld);
		fenster.add(landkarte, BorderLayout.CENTER);
		landkarte.repaint();
		
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

		neuknopf = new JButton("Neu starten");
		neuknopf.setActionCommand("NOCHMAL");

		nochmalknopf = new JButton("Nochmal spielen!");
		nochmalknopf.setActionCommand("NOCHMAL");

		knopfzeile = new JPanel();
		knopfzeile.add(speicherknopf);
		knopfzeile.add(ladeknopf);
		knopfzeile.add(neuknopf);
		
		fenster.add(knopfzeile, BorderLayout.NORTH);
	}

	public void erstelleSchatzkarte(Feld startfeld) {
		schatzkarte = new Schatzkarte(spielfeld.getFelder(), startfeld);
	}
	
	public void aktualisiere() {
		landkarte.repaint();
		rechteSpalte.setzeWasseranzeige(person.getWasser());
		rechteSpalte.setzeNahrungsanzeige(person.getNahrung());
		rechteSpalte.setzeFlossBeladung(inventar.getGesamtgewicht(), person.getTragfaehigkeit());
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
		neuknopf.addActionListener(controller);
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
		rechteSpalte.listenpanelBefuellen(noetigeTeile);
	}
	
	public void hosentascheHinzufuegen(Ladung teil) {
		rechteSpalte.zuInventarlisteHinzufügen(teil);
	}
	
	public void hosentascheEntfernen(Ladung teil) {
		rechteSpalte.ausInventarlisteEntfernen(teil);
	}
	
	public void fokusHolen() {
		fenster.requestFocus();		
	}

	public void markiereTeil(String teil) {
		rechteSpalte.teilAlsGefundenMarkieren(teil);
	}
	
	public void setzeBaumstumpf() {
		spielfeld.getAktuellesFeldPerson().setZweck(Zweck.BAUMSTUMPF);
	}
	
	public void spielende() {
		fenster.remove(landkarte);
		fenster.remove(rechteSpalte);
		fenster.remove(knopfzeile);
		
		//TODO: huebscher machen
		fenster.add(new JLabel("ENDE"));
		fenster.add(nochmalknopf);
	}
}
