//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 03/11/2020

package protocol.IOBREP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Week implements Serializable {
    private static final long serialVersionUID = -7757885350635873543L;

    /********************************/
    /*           Variables          */
    /********************************/
    private int weekNumber;
    private List<Integer> loadedContainers;
    private List<Integer> unloadedContainers;
    private List<String> destinations;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public Week(int weekNumber) {
        this.weekNumber = weekNumber;
        loadedContainers = new ArrayList<>();
        unloadedContainers = new ArrayList<>();
        destinations = new ArrayList<>();
    }

    public Week() {
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public int getWeekNumber() {
        return weekNumber;
    }

    public List<Integer> getLoadedContainers() {
        return loadedContainers;
    }

    public List<Integer> getUnloadedContainers() {
        return unloadedContainers;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public void setLoadedContainers(List<Integer> loadedContainers) {
        this.loadedContainers = loadedContainers;
    }

    public void setUnloadedContainers(List<Integer> unloadedContainers) {
        this.unloadedContainers = unloadedContainers;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }

    /********************************/
    /*            Methodes          */
    /********************************/

}
