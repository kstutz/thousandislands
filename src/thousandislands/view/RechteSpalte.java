package thousandislands.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import thousandislands.model.Inventar;
import thousandislands.model.enums.Ladung;

public class RechteSpalte extends JPanel {
	private JLabel wasser = new JLabel();
	private JLabel nahrung = new JLabel();
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
//		add(new JLabel("Flossbeladung"), c);
		add(new JLabel(" "), c);
		c.gridy = 6;
		c.insets = new Insets(0,0,10,0);
		flossbeladung.setVisible(false);
		//TODO erst erscheinen lassen, wenn tatsächlich Floß da
		add(flossbeladung, c);

		//an Position 7 und 8 kommen zwei Knöpfe vom GuiController aus

		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0,0,0,0);
		c.gridy = 9;
		add(new JLabel("Was ich brauche:"), c);

		listenpanel.setLayout(new BoxLayout(listenpanel, BoxLayout.PAGE_AXIS));
		c.gridy = 10;
		c.insets = new Insets(10,0,10,0);
		listenpanel.setVisible(false);
		add(listenpanel, c);

		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0,0,0,0);
		c.gridy = 11;
		add(new JLabel("Mein Inventar:"), c);

		inventarpanel.setLayout(new BoxLayout(inventarpanel, BoxLayout.PAGE_AXIS));
		c.gridy = 12;
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

	public void listenpanelAktualisieren(Map<Ladung,Boolean> noetigeTeile) {
		listenpanel.removeAll();
		for (Ladung teil : noetigeTeile.keySet()){
			JLabel label = new JLabel(teil.toString());
			if (noetigeTeile.get(teil)) {
				label.setText("<html><strike>" + teil.toString() + "</strike></html>");
			}
			listenpanel.add(label);
		}

		if (listenpanel.getComponents().length == 0) {
			listenpanel.setVisible(false);
		} else {
			listenpanel.setVisible(true);
		}
	}

	public void inventarpanelAktualisieren(Inventar inventar) {
		inventarpanel.removeAll();
		for(Ladung ladung: inventar.getLadeliste()) {
			inventarpanel.add(new JLabel(ladung.toString()));
		}
		if (inventarpanel.getComponents().length == 0) {
			inventarpanel.setVisible(false);
		} else {
			inventarpanel.setVisible(true);
		}
	}

	public void setzeFlossBeladung(int ist, int max) {
		flossbeladung.setText(ist + " / " + max);
	}
}
