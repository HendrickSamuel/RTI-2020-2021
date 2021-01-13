//Auteur : HENDRICK Samuel                                                                                              
//Projet : JAVA                               
//Date de la création : 05/12/2020

package Mails;

import java.io.Serializable;

public class User implements Serializable
{
    /********************************/
    /*           Variables          */
    /********************************/
    private String name;
    private String email;
    private User secretSanta;
    private boolean hasBeenSent;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public User(String name, String email)
    {
        this.name = name;
        this.email = email;
        hasBeenSent = false;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public User getSecretSanta()
    {
        return secretSanta;
    }

    public boolean isHasBeenSent()
    {
        return hasBeenSent;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setName(String name)
    {
        this.name = name;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setSecretSanta(User secretSanta)
    {
        this.secretSanta = secretSanta;
    }

    public void setHasBeenSent(boolean hasBeenSent)
    {
        this.hasBeenSent = hasBeenSent;
    }
}
