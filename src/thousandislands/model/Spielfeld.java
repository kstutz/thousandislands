package thousandislands.model;

import thousandislands.model.enums.Richtung;
import thousandislands.model.enums.Typ;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Random;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Spielfeld {
    @XmlElement
    private Feld[][] felder;

    private Feld aktuellesFeldPerson;
    private Random wuerfel = new Random();

    public Spielfeld() {}

    public Spielfeld(Feld[][] felder) {
        this.felder = felder;
    }

    public Feld[][] getFelder() {
        return felder;
    }

    public Feld getNachbar(Feld feld, Richtung richtung) {
        int x = feld.getX();
        int y = feld.getY();
        Feld nachbarFeld = null;
        switch (richtung) {
            case NORDEN:
                if (y > 0) {
                    nachbarFeld = felder[x][y-1];
                }
                break;
            case OSTEN:
                if (x < felder.length-1) {
                    nachbarFeld = felder[x+1][y];
                }
                break;
            case SUEDEN:
                if (y < felder[0].length-1) {
                    nachbarFeld = felder[x][y+1];
                }
                break;
            case WESTEN:
                if (x > 0) {
                    nachbarFeld = felder[x-1][y];
                }
                break;
        }
        return nachbarFeld;
    }

    public Feld getStartfeldFuerFlaschenpost() {
        while(true) {
            int x = wuerfel.nextInt(felder.length);
            int y = wuerfel.nextInt(felder[0].length);
            if (felder[x][y].getTyp() == Typ.MEER) {
                return felder[x][y];
            }
        }
    }

    public boolean setzePersonWeiter(Richtung richtung) {

        Feld neuesFeld = getNachbar(aktuellesFeldPerson, richtung);

        if (neuesFeld == null) {
            return false;
        }
        if (aktuellesFeldPerson.getTyp() == Typ.MEER && neuesFeld.getTyp() == Typ.DSCHUNGEL) {
            return false;
        }
        if (aktuellesFeldPerson.getTyp() == Typ.DSCHUNGEL && neuesFeld.getTyp() == Typ.MEER) {
            return false;
        }

        //Floss mitbewegen
        if (aktuellesFeldPerson.istFlossDa() && neuesFeld.getTyp() == Typ.MEER) {
            aktuellesFeldPerson.setFlossDa(false);
            neuesFeld.setFlossDa(true);
        }

        //Person bewegen
        aktuellesFeldPerson.setPersonDa(false);
        neuesFeld.setPersonDa(true);
//        vorigesFeld = aktuellesFeldPerson;
        aktuellesFeldPerson = neuesFeld;
        return true;
    }

    public Feld getAktuellesFeldPerson() {
        return aktuellesFeldPerson;
    }

    public void setAktuellesFeldPerson(Feld aktuellesFeldPerson) {
        this.aktuellesFeldPerson = aktuellesFeldPerson;
    }
}
