//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 09/11/2020

package Serveurs.Bateaux;

import genericRequest.MyProperties;
import lib.BeanDBAcces.BDMouvements;

import java.sql.SQLException;

public class MainBateau {

    public static void main(String[] args)
    {
        MyProperties mp = new MyProperties("./Confs/Serveur_Bateau.conf");
        int port = Integer.parseInt(mp.getContent("PORT"));
        int nbThreads = Integer.parseInt(mp.getContent("NBTHREADS"));
        String user = mp.getContent("BDUSER");
        String password = mp.getContent("BDPWD");
        String database = mp.getContent("DATABASE");


        ServeurBateaux sb = new ServeurBateaux(port,true, nbThreads, null);
        try
        {
            BDMouvements bd = new BDMouvements(user,password,database);
            sb.set_bdMouvement(bd);
            sb.StartServeur();
        }
        catch (SQLException | ClassNotFoundException throwables)
        {
            throwables.printStackTrace();
        }
    }
}
