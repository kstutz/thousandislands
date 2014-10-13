package thousandislands.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import thousandislands.model.Feld;
import thousandislands.model.enums.Typ;

public class InselBauer {
	private Feld zentrum;
	private Feld[][] spielfeld;
	private List<Feld> inselfelder = new ArrayList<>();
	private List<Feld> ersterRing = new ArrayList<>();
	private List<Feld> zweiterRing = new ArrayList<>();
	private List<Feld> dritterRing = new ArrayList<>();
	private List<Feld> vierterRing = new ArrayList<>();
	
	private Random rand = new Random();

	
	
	public InselBauer(Feld zentrum, Feld[][] spielfeld) {
		this.zentrum = zentrum;
		this.spielfeld = spielfeld;
		
		erstelleInsel();
	}
	
	public void erstelleInsel(){
		erstelleErstenRing();
		erstelleZweitenRing();
		erstelleDrittenRing();
		erstelleViertenRing();
		erstelleStrand();
	}
	
	private void erstelleErstenRing() {
		ersterRing = zentrum.getNachbarn();
		for(Feld feld : ersterRing) {
			feld.setTyp(Typ.DSCHUNGEL);
			inselfelder.add(feld);
		}
	}
	
	private void erstelleZweitenRing() {
		//hier wuerfeln, ob große oder kleine Insel
		
		//kleine Insel: wieder auf 4 wuerfeln
		
		
		//so wir große Insel
		List<Feld> moeglicheFelder = findeZweiteFelder();
		for (Feld feld : moeglicheFelder) {
			zweiterRing.add(feld);
			feld.setTyp(Typ.DSCHUNGEL);
			inselfelder.add(feld);
		}
	}
	
	private void erstelleDrittenRing() {
		List<Feld> moeglicheFelder = findeDritteFelder();
		for (Feld feld : moeglicheFelder) {
			int zufall = rand.nextInt(4);
			if (zufall != 0) {
				zweiterRing.add(feld);
				feld.setTyp(Typ.DSCHUNGEL);
				inselfelder.add(feld);
			} else {
				feld.setTyp(Typ.MEER);
			}
		}
	}
	
	private void erstelleViertenRing() {
		List<Feld> direkteNachbarn = new ArrayList<>();
		for(Feld feld2 : zweiterRing) {
			if (feld2.getTyp() != Typ.MEER) {
				direkteNachbarn.addAll(feld2.getDirekteNachbarn());
			} else {
				List<Feld> bla = feld2.getDirekteNachbarn();
				for (Feld nachbar : bla) {
					if(nachbar.getTyp() == Typ.VOR_MEER) {
						nachbar.setTyp(Typ.MEER);
					}
				}
				
			}
		}
		for (Feld feld3 : direkteNachbarn) {
			if (feld3.getTyp() == Typ.VOR_MEER) {
				int zufall = rand.nextInt(2);
				if (zufall != 0) {
					dritterRing.add(feld3);
					feld3.setTyp(Typ.DSCHUNGEL);
					inselfelder.add(feld3);						
				} else {
					feld3.setTyp(Typ.MEER);
				}
			}
		}		
	}

	private void erstelleStrand() {
		// TODO Auto-generated method stub
		
	}
	
	
	private List<Feld> findeZweiteFelder(){
		int x = zentrum.getX() - 2;
		int y = zentrum.getY() - 2;
		
		List<Feld> liste = new ArrayList<>();
		for (int i=0; i<5; i++){
			liste.add(spielfeld[x+i][y]);
			liste.add(spielfeld[x+i][y+4]);
		}
		for (int i=1; i<4; i++){
			liste.add(spielfeld[x][y+i]);
			liste.add(spielfeld[x+4][y+i]);
		}
		return liste;
	}
	
	private List<Feld> findeDritteFelder(){
		int x = zentrum.getX() - 3;
		int y = zentrum.getY() - 3;
		
		List<Feld> liste = new ArrayList<>();
		for (int i=0; i<7; i++){
			liste.add(spielfeld[x+i][y]);
			liste.add(spielfeld[x+i][y+6]);
		}
		for (int i=1; i<6; i++){
			liste.add(spielfeld[x][y+i]);
			liste.add(spielfeld[x+6][y+i]);
		}
		return liste;
	}

}
