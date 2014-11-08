package thousandislands.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import thousandislands.model.Feld;
import thousandislands.model.Person;
import thousandislands.model.Spieldaten;
import thousandislands.model.enums.Typ;
import thousandislands.view.GUI;


public class Controller extends KeyAdapter {
//	private static final int FELDANZAHL_WAAGERECHT = 100;
//	private static final int FELDANZAHL_SENKRECHT = 60;
	private GUI gui;
	private Person person;
	
	public static void main (String[] args) {
		new Controller();
	}
	
	Controller() {
		Feld[][] spielfeld = new SpielfeldErsteller().getSpielfeld();
		person = new Person(spielfeld[0][0]);
		spielfeld[0][0].setPersonDa(true);
		Spieldaten spieldaten = new Spieldaten(spielfeld, person);
		
		gui = new GUI(spieldaten);
		gui.aktualisiere();
		gui.addKeyListener(this);
	}
	
	
	@Override
	public void keyReleased(KeyEvent event) {
		boolean bewegt = false;
		
		switch (event.getKeyCode()) {
		case KeyEvent.VK_LEFT: 
			bewegt = person.bewegeNachW();
			break;
		case KeyEvent.VK_RIGHT:
			bewegt = person.bewegeNachO();
			break;
		case KeyEvent.VK_UP:
			bewegt = person.bewegeNachN();
			break;
		case KeyEvent.VK_DOWN:
			bewegt = person.bewegeNachS();
			break;
		}
		if (bewegt) {
			person.wasserAbziehen();
			person.nahrungAbziehen();
			gui.aktualisiere();
		}
		
		Feld aktuellesFeld = person.getAktuellesFeld();
		if (aktuellesFeld.getTyp() == Typ.ZWECK) {
			behandleZweckfeld();
		}
		if (person.hatSchatzkarte() && aktuellesFeld.getTyp() == Typ.SCHATZ) {
			behandleSchatzfund();
		}
		if (aktuellesFeld.hatFlaschenpost()) {
			person.kriegtSchatzkarte();
		}
	}

	private void behandleZweckfeld() {
		// TODO Auto-generated method stub
	}	
	
	private void behandleSchatzfund() {
		// TODO Auto-generated method stub		
	}

}
