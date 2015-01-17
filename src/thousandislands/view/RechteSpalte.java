package thousandislands.view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import thousandislands.model.enums.Teile;

public class RechteSpalte extends JPanel {
	private JLabel wasser = new JLabel();
	private JLabel nahrung = new JLabel();
	private List<JLabel> labelliste = new ArrayList<>();
	
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
		for (JLabel label : labelliste) {
			if (label.getText().equals(teil)) {
				label.setForeground(Color.BLACK);
			}
		}
	}
	
	public void teileHinzufuegen(Set<Teile> teile) {
		
		//ggf. Teile aus erstem Level loeschen
		if (!labelliste.isEmpty()) {
			for (JLabel label : labelliste) {
				remove(label);
			}
			labelliste.clear();
		}

		//neue Teile hinzufuegen
		for (Teile teil : teile) {
			JLabel label = new JLabel(teil.toString());
			label.setForeground(Color.GRAY);
			add(label);
			labelliste.add(label);
		}
	}
}
