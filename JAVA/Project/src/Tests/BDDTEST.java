/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/
package Tests;

import lib.BeanDBAcces.BDMouvements;

import java.sql.SQLException;

public class BDDTEST
{
    public static void main (String[] args)
    {
        BDMouvements bdm = null;
        try {
            bdm = new BDMouvements("root","root","bd_mouvements");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        boolean ret = bdm.tryLogin("Samuel","superSecurePass123");

        if(ret)
        {
            System.out.println("TRUE");
        }
        else
        {
            System.out.println("FALSE");
        }
    }
}