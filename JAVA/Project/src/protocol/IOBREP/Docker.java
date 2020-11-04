//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 03/11/2020

package protocol.IOBREP;

import genericRequest.DonneeRequete;

import java.io.Serializable;

public class Docker implements DonneeRequete, Serializable {
    private static final long serialVersionUID = -5373542896499524189L;

    /********************************/
    /*           Variables          */
    /********************************/
    private String dockerName;
    private double SeccondsToLoad;
    private double SeccondsToUnload;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public Docker(String dockerName, double seccondsToLoad, double seccondsToUnload) {
        this.dockerName = dockerName;
        SeccondsToLoad = seccondsToLoad;
        SeccondsToUnload = seccondsToUnload;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public String getDockerName() {
        return dockerName;
    }

    public double getSeccondsToLoad() {
        return SeccondsToLoad;
    }

    public double getSeccondsToUnload() {
        return SeccondsToUnload;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setDockerName(String dockerName) {
        this.dockerName = dockerName;
    }

    public void setSeccondsToLoad(double seccondsToLoad) {
        SeccondsToLoad = seccondsToLoad;
    }

    public void setSeccondsToUnload(double seccondsToUnload) {
        SeccondsToUnload = seccondsToUnload;
    }
    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields) {

    }
}
