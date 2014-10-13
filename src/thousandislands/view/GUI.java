package thousandislands.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import thousandislands.model.Feld;

public class GUI extends JFrame{

	public GUI(Feld[][] felder){
		setSize(new Dimension(1000, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Tausend Inseln");
		Spielfeld spielfeld = new Spielfeld(felder);
		add(spielfeld, BorderLayout.CENTER);
		spielfeld.repaint();
		setVisible(true);
	}
	
	

	

}
