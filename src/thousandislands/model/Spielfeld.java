package thousandislands.model;

import thousandislands.model.enums.Richtung;
import thousandislands.model.enums.Typ;
import thousandislands.model.enums.Zweck;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Spielfeld {
    @XmlElement
    private Feld[][] felder;

    private Feld aktuellesFeldPerson;
    private Feld flossFeld;
    private Feld flaschenpostFeld;
    private Feld SCHATZKARTENANFANG;

    private List<Zweck> zweckliste;

    public Spielfeld() {}

    public Spielfeld(Feld[][] felder, Feld schatzkartenAnfang, List<Zweck> zweckliste) {
        this.felder = felder;
        SCHATZKARTENANFANG = schatzkartenAnfang;
        this.zweckliste = zweckliste;
    }

    public Feld[][] getFelder() {
        return felder;
    }

    public Optional<Feld> getNachbar(Feld feld, Richtung richtung) {
        int x = feld.getX();
        int y = feld.getY();
        Optional<Feld> nachbarFeld = Optional.empty();
        switch (richtung) {
            case NORDEN:
                if (y > 0) {
                    nachbarFeld = Optional.of(felder[x][y-1]);
                }
                break;
            case OSTEN:
                if (x < felder.length-1) {
                    nachbarFeld = Optional.of(felder[x+1][y]);
                }
                break;
            case SUEDEN:
                if (y < felder[0].length-1) {
                    nachbarFeld = Optional.of(felder[x][y+1]);
                }
                break;
            case WESTEN:
                if (x > 0) {
                    nachbarFeld = Optional.of(felder[x-1][y]);
                }
                break;
        }
        return nachbarFeld;
    }

    public Set<Feld> getNachbarn(Feld feld) {
        Set<Feld> direkteNachbarn = new HashSet<>();
        List<Richtung> richtungen = new ArrayList<>(Arrays.asList(Richtung.class.getEnumConstants()));
        for (Richtung richtung : richtungen) {
            getNachbar(feld, richtung).ifPresent(nachbar -> direkteNachbarn.add(nachbar));
        }

        Set<Feld> alleNachbarn = new HashSet<>();

        for (Feld direkterNachbar: direkteNachbarn) {
            for (Richtung richtung : richtungen) {
                getNachbar(direkterNachbar, richtung).ifPresent(nachbar -> alleNachbarn.add(nachbar));
            }
        }
        alleNachbarn.addAll(direkteNachbarn);

        return alleNachbarn;
    }

    public Feld getZufaelligesMeerfeld() {
        Random wuerfel = new Random();
        while(true) {
            int x = wuerfel.nextInt(felder.length);
            int y = wuerfel.nextInt(felder[0].length);
            if (felder[x][y].getTyp() == Typ.MEER) {
                return felder[x][y];
            }
        }
    }

    public boolean setzePersonWeiter(Richtung richtung) {

        Optional<Feld> nachbarfeld = getNachbar(aktuellesFeldPerson, richtung);

        if (!nachbarfeld.isPresent()) {
            return false;
        }

        Feld neuesFeld = nachbarfeld.get();

//        if (person.getLevel == 0 && aktuellesFeldPerson.getTyp() == Typ.STRAND && neuesFeld.getTyp() == Typ.MEER) {
//        }

        if (aktuellesFeldPerson.getTyp() == Typ.MEER
                && (neuesFeld.getTyp() == Typ.DSCHUNGEL || neuesFeld.getTyp() == Typ.ZWECK)) {
            return false;
        }
        if ((aktuellesFeldPerson.getTyp() == Typ.DSCHUNGEL || aktuellesFeldPerson.getTyp() == Typ.ZWECK)
                && neuesFeld.getTyp() == Typ.MEER) {
            return false;
        }

        //Floss mitbewegen
        if (aktuellesFeldPerson.equals(flossFeld) && neuesFeld.getTyp() == Typ.MEER) {
            flossFeld = neuesFeld;
        }

        //Person bewegen
        aktuellesFeldPerson = neuesFeld;
        return true;
    }

    public Feld getAktuellesFeldPerson() {
        return aktuellesFeldPerson;
    }

    public void setAktuellesFeldPerson(Feld aktuellesFeldPerson) {
        this.aktuellesFeldPerson = aktuellesFeldPerson;
    }

    public Feld getFlossFeld() {
        return flossFeld;
    }

    public void setFlossFeld(Feld feld) {
        flossFeld = feld;
    }

    public Feld getFlaschenpostFeld() {
        return flaschenpostFeld;
    }

    public void setFlaschenpostFeld(Feld feld) {
        flaschenpostFeld = feld;
    }

    public Feld getSchatzkartenanfang() {
        return SCHATZKARTENANFANG;
    }

    public Zweck getNaechsterZweck() {
        return zweckliste.remove(0);
    }
}
