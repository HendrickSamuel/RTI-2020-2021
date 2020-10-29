//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 29/10/2020

package Tests;

import lib.BeanDBAcces.BDMouvements;
import protocol.PLAMAP.DonneeGetXY;
import protocol.PLAMAP.ReponsePLAMAP;
import protocol.PLAMAP.TraitementPLAMAP;

import java.sql.SQLException;

public class TestTraitementPlamap {

    public static void main(String[] args)
    {
        try
        {
            BDMouvements bd = new BDMouvements("root","root","bd_mouvements");
            TraitementPLAMAP tp = new TraitementPLAMAP(bd);
            DonneeGetXY d = new DonneeGetXY("masociete","Trans1","blabla","paris");
            ReponsePLAMAP rp = (ReponsePLAMAP)tp.traiteRequete(d, null);
            System.out.println(rp.toString());
        }
        catch (SQLException | ClassNotFoundException throwables)
        {
            throwables.printStackTrace();
        }
    }

}
