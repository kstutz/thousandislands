package thousandislands.view;

import java.awt.Dimension;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Fenster extends JFrame {
	
	public Fenster(){
		setSize(new Dimension(1150, 640));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Tausend Inseln");

		setVisible(true);
		setFocusable(true);
	    requestFocus();
	}
}
