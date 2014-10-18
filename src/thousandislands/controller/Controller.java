package thousandislands.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import thousandislands.model.Feld;
import thousandislands.model.Person;
import thousandislands.model.Spieldaten;
import thousandislands.view.GUI;


public class Controller extends KeyAdapter {
//	private static final int FELDANZAHL_WAAGERECHT = 100;
//	private static final int FELDANZAHL_SENKRECHT = 60;
	private GUI gui;
	
	public static void main (String[] args) {
		
		Feld[][] spielfeld = new SpielfeldErsteller().getSpielfeld();
		Person person = new Person();
		Spieldaten spieldaten = new Spieldaten(spielfeld, person);
		
		GUI gui = new GUI(spieldaten);
		gui.aktualisiere();
	}
	
	@Override
	public void keyTyped(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
		behandleTastendruck(event);
		}		
	}
	
	private void behandleTastendruck(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
		}		
		
	}
	

}
