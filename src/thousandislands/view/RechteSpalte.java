package thousandislands.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import thousandislands.model.enums.Flossteile;
import thousandislands.model.enums.Schiffsteile;

public class RechteSpalte extends JPanel {
	private JLabel wasser = new JLabel();
	private JLabel nahrung = new JLabel();
	private List<JLabel> labelListe = new ArrayList<>();
	
	public RechteSpalte() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		add(new JLabel("Wasser"));
		add(wasser);
		add(Box.createRigidArea(new Dimension(0,10)));
		add(new JLabel("Nahrung"));
		add(nahrung);
		add(Box.createRigidArea(new Dimension(0,10)));		
	}

	public void setzeWasseranzeige(String s) {
		wasser.setText(s);
	}

	public void setzeNahrungsanzeige(String s) {
		nahrung.setText(s);
	}
	
	public void teilAlsGefundenMarkieren(String teil) {
		for (JLabel label : labelListe) {
			if (label.getText().equals(teil)) {
				label.setForeground(Color.BLACK);
			}
		}
	}
	
	public void flossteileHinzufuegen () {
		for (Flossteile teil : Flossteile.values()) {
			JLabel label = new JLabel(teil.toString());
			label.setForeground(Color.GRAY);
			add(label);
			labelListe.add(label);
		}
	}

	public void schiffsteileHinzufuegen() {
		for (JLabel label: labelListe) {
			remove(label);
		}
		labelListe.clear();
		for (Schiffsteile teil : Schiffsteile.values()) {
			JLabel label = new JLabel(teil.toString());
			label.setForeground(Color.GRAY);
			add(label);
			labelListe.add(label);
		}
	}
}
