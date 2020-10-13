//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 13/10/2020

package Mouvement.Serveur;

import MyGenericServer.ConsoleServeur;

import javax.swing.*;

public class ConsoleSwing implements ConsoleServeur {

    JTextArea jta;

    /********************************/
    /*           Variables          */
    /********************************/

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ConsoleSwing(JTextArea jta) {
        this.jta = jta;
    }
    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/

    /********************************/
    /*            Methodes          */
    /********************************/

    @Override
    public void Affiche(String message) {
        jta.append(message+"\n");
    }

}
