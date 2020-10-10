/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package Tests;

import genericServer.ListeTaches;
import genericServer.ThreadServeur;

public class Main
{

    public static void main (String[] args)
    {
        MyProperties mp = new MyProperties("./Serveur_Mouvement.conf");
        System.out.println(mp.getContent("PORT"));
        ListeTaches lt = new ListeTaches();
        ThreadServeur ts = new ThreadServeur(50001, lt, null);
        ts.start();
    }
}
