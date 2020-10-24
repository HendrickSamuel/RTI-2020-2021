//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 24/10/2020

package Tests;

import Serveurs.Bateaux.ServeurBateaux;

public class TestServeur {
    public static void main(String[] args)
    {
        ServeurBateaux sb = new ServeurBateaux(5000,true, 5, null);
        sb.StartServeur();
    }

}
