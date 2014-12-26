package thousandislands.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import thousandislands.model.enums.Schiffsteile;

public class RechteSpalte extends JPanel {
	private JLabel wasser = new JLabel();
	private JLabel nahrung = new JLabel();
	private List<JLabel> labelListe = new ArrayList<>();
	private JButton knopfFuerAlles;
	private JButton kartenknopf;
	
	RechteSpalte() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		add(new JLabel("Wasser"));
		add(wasser);
		add(Box.createRigidArea(new Dimension(0,10)));
		add(new JLabel("Nahrung"));
		add(nahrung);
		add(Box.createRigidArea(new Dimension(0,10)));
		
		schiffsteileErstellen();
		add(Box.createRigidArea(new Dimension(0,10)));
		
		knopfFuerAlles = new JButton();
		knopfFuerAlles.setVisible(false);
		add(knopfFuerAlles);
		
		kartenknopf = new JButton("Schatzkarte");
		kartenknopf.setVisible(false);
		add(kartenknopf);
	}

	public void setzeWasseranzeige(String s) {
		wasser.setText(s);
	}

	public void setzeNahrungsanzeige(String s) {
		nahrung.setText(s);
	}

	public void schiffsteilHinzufuegen(Schiffsteile teil) {
		for (JLabel label : labelListe) {
			if (label.getText().equals(teil.toString())) {
				label.setForeground(Color.BLACK);
			}
		}
	}
	
	public void knopfFuerAllesText(String s) {
		knopfFuerAlles.setText(s);
		knopfFuerAlles.setVisible(true);
	}

	public void knopfFuerAllesSichtbar(boolean bool) {
		knopfFuerAlles.setVisible(bool);
	}

	public void kartenknopfSichtbar(boolean bool) {
		kartenknopf.setVisible(bool);
	}
	
	private void schiffsteileErstellen() {
		for (Schiffsteile teil : Schiffsteile.values()) {
			JLabel label = new JLabel(teil.toString());
			label.setForeground(Color.GRAY);
			add(label);
			labelListe.add(label);
		}
	}
}
