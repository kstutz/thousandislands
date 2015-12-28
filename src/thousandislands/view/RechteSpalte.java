package thousandislands.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import thousandislands.model.enums.Ladung;

public class RechteSpalte extends JPanel {
	private JLabel wasser = new JLabel();
	private JLabel nahrung = new JLabel();
	private List<JLabel> labelliste = new ArrayList<>();
	private List<JLabel> inventarListe = new ArrayList<>();
	private JTextArea nachrichtenfeld;
	private JLabel flossbeladung = new JLabel();
	private JPanel listenpanel = new JPanel();
	
	public RechteSpalte() {
//		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
//		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
//		setPreferredSize(new Dimension(200, 0));
		
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
		
	}

	public void setzeWasseranzeige(String s) {
		wasser.setText(s);
	}

	public void setzeNahrungsanzeige(String s) {
		nahrung.setText(s);
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
	
	public void teileHinzufuegen(Set<Ladung> noetigeTeile) {
		GridBagConstraints c = new GridBagConstraints();
		
		if (labelliste.isEmpty()) {
			listenpanel.setLayout(new BoxLayout(listenpanel, BoxLayout.PAGE_AXIS));
			c.gridy = 9;
			c.insets = new Insets(10,0,10,0);
			add(listenpanel, c);
		} else { //Teile aus erstem Level loeschen
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
	
	public void inventarHinzufügen(Ladung ladungsteil) {
		//TODO: panel dafuer
		JLabel label = new JLabel(ladungsteil.toString());
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 10;
		inventarListe.add(label);
		add(label, c);
	}
	
	public void inventarEntfernen(Ladung ladungsteil) {
		JLabel sollWeg = new JLabel();
		
		for (JLabel label : inventarListe) {
			if (label.getText().equals(ladungsteil.toString())) {
				sollWeg = label;
			}
		}
		
		inventarListe.remove(sollWeg);
		remove(sollWeg);
	}
	
	public void setzeFlossBeladung(int ist, int max) {
		flossbeladung.setText(ist + " / " + max);
	}
}
