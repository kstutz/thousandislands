package thousandislands.controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
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
	private JButton needankeknopf;
	private JPanel knopfzeile;
	private Schatzkarte schatzkarte;
	private Spielfeld spielfeld;
	private Person person;
	private Inventar inventar;
	private Fenster fenster;
	private Map<Ladung, Boolean> noetigeTeile;

	public GuiController(Spielfeld spielfeld, Person person, Inventar inventar, Map<Ladung, Boolean> noetigeTeile){
		this.spielfeld = spielfeld;
		this.person = person;
		this.inventar = inventar;
		this.noetigeTeile = noetigeTeile;

		fenster = new Fenster();
		erstelleGui();
	}

	public void resetGui(Spielfeld spielfeld, Person person, Inventar inventar, Map<Ladung, Boolean> noetigeTeile) {
		this.spielfeld = spielfeld;
		this.person = person;
		this.inventar = inventar;
		this.noetigeTeile = noetigeTeile;

		fenster.removeAll();
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

		nochmalknopf = new JButton("Au ja!");
		nochmalknopf.setActionCommand("NOCHMAL");

		needankeknopf = new JButton("Nee danke.");
		needankeknopf.setActionCommand("BEENDEN");

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

	public void aktualisiereListen() {
		rechteSpalte.listenpanelAktualisieren(noetigeTeile);
		rechteSpalte.inventarpanelAktualisieren(inventar);
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
		knopfFuerAlles.setText(aktion.getKnopftext());
		knopfFuerAlles.setActionCommand(aktion.toString());
		knopfFuerAlles.setVisible(true);
	}

	public void actionListenerHinzufuegen(Controller controller) {
		knopfFuerAlles.addActionListener(controller);
		speicherknopf.addActionListener(controller);
		ladeknopf.addActionListener(controller);
		neuknopf.addActionListener(controller);
		nochmalknopf.addActionListener(controller);
		needankeknopf.addActionListener(controller);
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

	public void fokusHolen() {
		fenster.requestFocus();
	}

	public void setzeBaumstumpf() {
		spielfeld.getAktuellesFeldPerson().setZweck(Zweck.BAUMSTUMPF);
	}
	
	public void spielende() {
		fenster.remove(landkarte);
		fenster.remove(rechteSpalte);
		fenster.remove(knopfzeile);
		
		//TODO: huebscher machen
		fenster.setLayout(new FlowLayout());
		fenster.add(new JLabel("<html>ENDE.<br>Nochmal spielen?</html>"));
		fenster.add(nochmalknopf);
		fenster.add(needankeknopf);
	}
}
