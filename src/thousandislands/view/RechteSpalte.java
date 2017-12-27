package thousandislands.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import thousandislands.model.enums.Ladung;

public class RechteSpalte extends JPanel {
	private JLabel wasser = new JLabel();
	private JLabel nahrung = new JLabel();
	private List<JLabel> labelliste = new ArrayList<>();
	private List<JLabel> inventarliste = new ArrayList<>();
	private JTextArea nachrichtenfeld;
	private JLabel flossbeladung = new JLabel();
	private JPanel listenpanel = new JPanel();
	private JPanel inventarpanel = new JPanel();
	
	public RechteSpalte() {		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//Wasseranzeige
		JLabel wasserlabel = new JLabel("Wasser");
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.PAGE_START;
		add(wasserlabel, c);

		c.gridy = 1;
		c.insets = new Insets(0,0,10,0);
		add(wasser, c);

		//Nahrungsanzeige
		c.gridy = 2;
		c.insets = new Insets(0,0,0,0);
		add(new JLabel("Nahrung"), c);
		c.gridy = 3;
		c.insets = new Insets(0,0,10,0);
		add(nahrung, c);

		//nachrichtenfeld
		nachrichtenfeld = new JTextArea();
//		nachrichtenfeld.setMinimumSize(new Dimension(120, 100));
		nachrichtenfeld.setPreferredSize(new Dimension(170, 170));
		nachrichtenfeld.setMaximumSize(new Dimension(200, 170));
		nachrichtenfeld.setEditable(false);
		nachrichtenfeld.setLineWrap(true);
		nachrichtenfeld.setWrapStyleWord(true);
		c.gridy = 4;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,0,10,0);
		add(nachrichtenfeld, c);

		//Flossladeanzeige
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0,0,0,0);
		c.gridy = 5;
		add(new JLabel("Flossbeladung"), c);
		c.gridy = 6;
		c.insets = new Insets(0,0,10,0);
		add(flossbeladung, c);

		listenpanel.setLayout(new BoxLayout(listenpanel, BoxLayout.PAGE_AXIS));
		listenpanel.add(new JLabel("Was ich brauche:"));
		c.gridy = 9;
		c.insets = new Insets(10,0,10,0);
		listenpanel.setVisible(false);
		add(listenpanel, c);

		inventarpanel.setLayout(new BoxLayout(inventarpanel, BoxLayout.PAGE_AXIS));
		inventarpanel.add(new JLabel("Inventar:"));
		c.gridy = 10;
		c.insets = new Insets(10,0,10,0);
		inventarpanel.setVisible(false);
		add(inventarpanel, c);
	}

	public void setzeWasseranzeige(int i) {
		wasser.setText(String.valueOf(i));
	}

	public void setzeNahrungsanzeige(int i) {
		nahrung.setText(String.valueOf(i));
	}
	
	public void zeigeNachricht(String s) {
		nachrichtenfeld.setText(s);
	}
		
	public void teilAlsGefundenMarkieren(String teil) {
		for (JLabel label : labelliste) {
			if (label.getText().equals(teil)) {
				label.setText("<html><strike>" + teil + "</strike></html>");
			}
		}
	}
	
	public void listenpanelBefuellen(Set<Ladung> noetigeTeile) {
		GridBagConstraints c = new GridBagConstraints();
		
		if (!labelliste.isEmpty()) {
			//Teile aus erstem Level loeschen
			for (JLabel label : labelliste) {
				listenpanel.remove(label);
			}
			labelliste.clear();			
		}

		//neue Teile hinzufuegen
		for (Ladung teil : noetigeTeile) {
			JLabel label = new JLabel(teil.toString());
			listenpanel.add(label);
			labelliste.add(label);
		}
	}
	
	public void zuInventarlisteHinzufügen(Ladung ladungsteil) {
		JLabel label = new JLabel(ladungsteil.toString());
		inventarliste.add(label);
		inventarpanel.add(label);
		inventarpanel.setVisible(true);
	}
	
	public void ausInventarlisteEntfernen(Ladung ladungsteil) {
		JLabel sollWeg = new JLabel();	
		for (JLabel label : inventarliste) {
			if (label.getText().equals(ladungsteil.toString())) {
				sollWeg = label;
			}
		}
		
		inventarliste.remove(sollWeg);
		inventarpanel.remove(sollWeg);
		inventarpanel.revalidate();
		inventarpanel.repaint();
		
		if (inventarliste.isEmpty()) {
			inventarpanel.setVisible(false);
		}
	}
	
	public void setzeFlossBeladung(int ist, int max) {
		flossbeladung.setText(ist + " / " + max);
	}
}
