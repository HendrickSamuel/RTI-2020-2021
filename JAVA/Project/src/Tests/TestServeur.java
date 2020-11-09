//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 24/10/2020

package Tests;

import Serveurs.Bateaux.ServeurBateaux;
import lib.BeanDBAcces.BDMouvements;

import java.sql.SQLException;

public class TestServeur {
    public static void main(String[] args)
    {
        ServeurBateaux sb = new ServeurBateaux(5000,true, 5, null);
        try
        {
            BDMouvements bd = new BDMouvements("root","","bd_mouvements");
            sb.set_bdMouvement(bd);
        }
        catch (SQLException | ClassNotFoundException throwables)
        {
            throwables.printStackTrace();
        }
        sb.StartServeur();
    }

}
