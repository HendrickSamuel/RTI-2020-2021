//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 03/11/2020

package protocol.IOBREP;

import genericRequest.DonneeRequete;

import java.io.Serializable;
import java.util.List;

public class DonneeGetLoadUnloadStatsWeekly implements DonneeRequete, Serializable {
    private static final long serialVersionUID = 229090717396772374L;

    /********************************/
    /*           Variables          */
    /********************************/
    private List<Week> weeks;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeGetLoadUnloadStatsWeekly() {
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public List<Week> getWeeks() {
        return weeks;
    }
    /********************************/
    /*            Setters           */
    /********************************/
    public void setWeeks(List<Week> weeks) {
        this.weeks = weeks;
    }
    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields) {

    }
}
