//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 24/10/2020

package protocol.IOBREP;

import genericRequest.DonneeRequete;

import java.io.Serializable;

public class DonneeLogin implements DonneeRequete, Serializable {

    /********************************/
    /*           Variables          */
    /********************************/
    private String username;
    private String password;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    /********************************/
    /*            Setters           */
    /********************************/

    /********************************/
    /*            Methodes          */
    /********************************/

}
