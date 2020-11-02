//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 01/11/2020

package protocol.PIDEP;

import MyGenericServer.Client;
import MyGenericServer.ConsoleServeur;
import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import genericRequest.Traitement;
import lib.BeanDBAcces.BDMouvements;
import protocol.PLAMAP.DonneeLoginCont;


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

    private Reponse traiteLOGIN(DonneeLoginCont chargeUtile, Client client)
    {
        return null;
    }

    private Reponse traiteGET_STAT_DESCR_CONT(DonneeLoginCont chargeUtile, Client client)
    {
        return null;
    }

    private Reponse traiteGET_GR_COULEUR_REP(DonneeLoginCont chargeUtile, Client client)
    {
        return null;
    }

    private Reponse traiteGET_GR_COULEUR_COMP(DonneeLoginCont chargeUtile, Client client)
    {
        return null;
    }

    private Reponse traiteGET_STAT_INFER_TEST_CONF(DonneeLoginCont chargeUtile, Client client)
    {
        return null;
    }

    private Reponse traiteGET_STAT_INFER_TEST_HOMOG(DonneeLoginCont chargeUtile, Client client)
    {
        return null;
    }
}
