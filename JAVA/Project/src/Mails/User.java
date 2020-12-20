//Auteur : HENDRICK Samuel                                                                                              
//Projet : JAVA                               
//Date de la création : 05/12/2020

package Mails;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String email;
    private User secretSanta;
    private boolean hasBeenSent;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        hasBeenSent = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getSecretSanta() {
        return secretSanta;
    }

    public void setSecretSanta(User secretSanta) {
        this.secretSanta = secretSanta;
    }

    public boolean isHasBeenSent() {
        return hasBeenSent;
    }

    public void setHasBeenSent(boolean hasBeenSent) {
        this.hasBeenSent = hasBeenSent;
    }
}
