package thousandislands.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import thousandislands.controller.Controller;
import thousandislands.model.Spieldaten;

public class GUI extends JFrame{
	private Landkarte spielfeld;
	private RechteSpalte rechteSpalte;
	private JButton knopfFuerAlles;
	private JButton kartenknopf;	
	private JLabel nachrichtenzeile;
	private Spieldaten daten;
	
	public GUI(Spieldaten daten){
		setSize(new Dimension(1150, 640));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Tausend Inseln");

		this.daten = daten;		
		macheStartklar();
		
		setVisible(true);
		setFocusable(true);
	    requestFocus();
	}
	
	private void macheStartklar() {
		//Spielfeld hinzufuegen
		spielfeld = new Landkarte(daten);
		add(spielfeld, BorderLayout.CENTER);
		spielfeld.repaint();
		
		//rechte Spalte und Knoepfe hinzufuegen
		rechteSpalte = new RechteSpalte();

		knopfFuerAlles = new JButton();
//		knopfFuerAlles.setVisible(false);
		rechteSpalte.add(knopfFuerAlles);

		rechteSpalte.add(Box.createRigidArea(new Dimension(0,10)));

		kartenknopf = new JButton("Schatzkarte");
//		kartenknopf.setVisible(false);
		kartenknopf.setActionCommand("KARTE");
		rechteSpalte.add(kartenknopf);
		
		add(rechteSpalte, BorderLayout.EAST);
		
		//Nachrichtenzeile hinzufuegen
		nachrichtenzeile = new JLabel();
		add(nachrichtenzeile, BorderLayout.SOUTH);
	}
	
	public void aktualisiere() {
		spielfeld.repaint();
		rechteSpalte.setzeWasseranzeige(daten.getWasser());
		rechteSpalte.setzeNahrungsanzeige(daten.getNahrung());
		nachrichtenzeile.setText(" ");
		knopfFuerAlles.setText(" ");
	}
	
	public void zeigeNachricht(String s) {
		nachrichtenzeile.setText(s);
	}
	
	
	public void setzeKnopf(String befehl) {
		
		switch(befehl) {
		
		case "HOLZ_MITNEHMEN":
			knopfFuerAlles.setText("Holz mitnehmen");
			knopfFuerAlles.setActionCommand(befehl);
			break;
		case "LIANEN_MITNEHMEN":
			knopfFuerAlles.setText("Lianen mitnehmen");
			knopfFuerAlles.setActionCommand(befehl);
			break;
		case "KRUG_FUELLEN":
			knopfFuerAlles.setText("Krug fuellen");
			knopfFuerAlles.setActionCommand(befehl);
			break;
		case "KORB_FUELLEN":
			knopfFuerAlles.setText("Korb fuellen");
			knopfFuerAlles.setActionCommand(befehl);
			break;
		case "KRUG_FORMEN":
			knopfFuerAlles.setText("Krug formen");
			knopfFuerAlles.setActionCommand(befehl);
			break;			
		case "FEUER_MACHEN":
			knopfFuerAlles.setText("Feuer machen");
			knopfFuerAlles.setActionCommand(befehl);
			break;
		case "KRUG_BRENNEN":
			knopfFuerAlles.setText("Krug brennen");
			knopfFuerAlles.setActionCommand(befehl);
			break;
		case "KORB_FLECHTEN":
			knopfFuerAlles.setText("Korb flechten");
			knopfFuerAlles.setActionCommand(befehl);
			break;
		case "PAPAYA_MITNEHMEN":
			knopfFuerAlles.setText("Papaya mitnehmen");
			knopfFuerAlles.setActionCommand(befehl);
			break;
		case "TROTZDEM_MITNEHMEN":
			knopfFuerAlles.setText("Trotzdem mitnehmen");
			knopfFuerAlles.setActionCommand(befehl);
			break;

		case "FLOSS_BAUEN":
			knopfFuerAlles.setText("Floß bauen");
			knopfFuerAlles.setActionCommand(befehl);
			break;
			
		default:
			break;
		
		}
	}

	public void actionListenerHinzufuegen(Controller controller) {
		knopfFuerAlles.addActionListener(controller);
		kartenknopf.addActionListener(controller);
	}
}
