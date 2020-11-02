//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 01/11/2020

package protocol.PIDEP;

import MyGenericServer.Client;
import MyGenericServer.ConsoleServeur;
import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import genericRequest.Requete;
import genericRequest.Traitement;
import lib.BeanDBAcces.BDMouvements;

public class TraitementPIDEP implements Traitement
{
    /********************************/
    /*           Variables          */
    /********************************/
    private BDMouvements _bd;
    private ConsoleServeur _cs;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public TraitementPIDEP()
    {

    }

    public TraitementPIDEP(BDMouvements _bd)
    {
        this._bd = _bd;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public BDMouvements get_bd()
    {
        return _bd;
    }

    public ConsoleServeur get_cs()
    {
        return _cs;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_bd(BDMouvements _bd)
    {
        this._bd = _bd;
    }

    @Override
    public void setConsole(ConsoleServeur cs)
    {
        this._cs = cs;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public Reponse traiteRequete(DonneeRequete Requete, Client client) throws ClassCastException
    {
        return null;
    }

    @Override
    public void AfficheTraitement(String message)
    {

    }
}
