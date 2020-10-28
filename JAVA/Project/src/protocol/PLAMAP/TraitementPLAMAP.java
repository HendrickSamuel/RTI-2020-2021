//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/10/2020

package protocol.PLAMAP;

import MyGenericServer.Client;
import MyGenericServer.ConsoleServeur;
import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import genericRequest.Traitement;
import lib.BeanDBAcces.BDMouvements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TraitementPLAMAP implements Traitement
{
    /********************************/
    /*           Variables          */
    /********************************/
    private BDMouvements _bd;
    private ConsoleServeur _cs;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public TraitementPLAMAP()
    {

    }

    public TraitementPLAMAP(BDMouvements _bd)
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
        System.out.println(Requete);
        if(Requete instanceof DonneeLoginCont)
            return traiteLOGINCONT((DonneeLoginCont)Requete, client);
        else if(Requete instanceof DonneeGetXY)
            return traiteGETXY((DonneeGetXY)Requete, client);
        else if(Requete instanceof DonneeGetList)
            return traiteGETLISTE( (DonneeGetList)Requete, client);
        else if(Requete instanceof DonneeSendWeight)
            return traiteSENDWEIGHT( (DonneeSendWeight)Requete, client);
        else if(Requete instanceof DonneeSignalDep)
            return traiteSIGNALDEP( (DonneeSignalDep)Requete, client);
        else
            return traite404();
    }

    @Override
    public void AfficheTraitement(String message)
    {
        if (_cs != null)
        {
            _cs.Affiche(message);
        }
        else
        {
            System.err.println("-- Le serveur n'a pas de console dédiée pour ce message -- " + message);
        }
    }

    private Reponse traiteLOGINCONT(DonneeLoginCont chargeUtile, Client client)
    {
        System.out.println("traiteLOGINCONT");
        System.out.println(chargeUtile.toString());
        return null;
    }

    private Reponse traiteGETXY(DonneeGetXY chargeUtile, Client client)
    {
        System.out.println("traiteGETXY");
        System.out.println(chargeUtile.toString());
        return null;
    }

    private Reponse traiteGETLISTE(DonneeGetList chargeUtile, Client client)
    {
        System.out.println("traiteGETLISTE");
        System.out.println(chargeUtile.toString());
        return null;
    }

    private Reponse traiteSENDWEIGHT(DonneeSendWeight chargeUtile, Client client)
    {
        System.out.println("traiteSENDWEIGHT");
        System.out.println(chargeUtile.toString());
        return null;
    }

    private Reponse traiteSIGNALDEP(DonneeSignalDep chargeUtile, Client client)
    {
        System.out.println("traiteSIGNALDEP");
        System.out.println(chargeUtile.toString());
        return null;
    }

    private Reponse traite404()
    {
        System.out.println("traite404 Request not found");
        return new ReponsePLAMAP(ReponsePLAMAP.REQUEST_NOT_FOUND,  "request could not be exeuted due to unsopported version.",null);
    }
}