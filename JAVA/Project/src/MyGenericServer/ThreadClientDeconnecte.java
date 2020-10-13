/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package MyGenericServer;

import MyGenericServer.SourceTaches;

import java.net.Socket;

public class ThreadClientDeconnecte extends ThreadClient
{
    /********************************/
    /*           Variables          */
    /********************************/
    private SourceTaches _taches;
    private String nom;
    private Socket tacheEnCours;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ThreadClientDeconnecte()
    {
        System.out.println("DEMARRAGE ThreadClientDeconnecte");
    }


    /********************************/
    /*            Setters           */
    /********************************/
    @Override
    public void set_taches(SourceTaches _taches)
    {
        this._taches = _taches;
    }

    @Override
    public void setNom(String nom)
    {
        this.nom = nom;
    }

    @Override
    public void setTraitement(String nom)
    {

    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void run()
    {
        System.out.println("Je suis :" + nom + " et je demarre en tant que Thread");
    }

}
