package thousandislands.view;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RechteSpalte extends JPanel {
	private JLabel wasser = new JLabel();
	private JLabel nahrung = new JLabel();
	
	RechteSpalte() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		add(new JLabel("Wasser"));
		add(wasser);
		add(Box.createRigidArea(new Dimension(0,10)));
		add(new JLabel("Nahrung"));
		add(nahrung);
	}

	public void setzeWasseranzeige(String s) {
		wasser.setText(s);
	}

	public void setzeNahrungsanzeige(String s) {
		nahrung.setText(s);
	}
	

}
