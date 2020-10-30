//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 13/10/2020

package Serveurs.Mouvement.Serveur;

import MyGenericServer.ConsoleServeur;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = Calendar.getInstance().getTime();
        jta.append(formatter.format(now) + "> "+ message+"\n");
    }

}
