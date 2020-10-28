//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 22/10/2020

package Serveurs.Mouvement.Serveur;

import MyGenericServer.ConsoleServeur;
import MyGenericServer.ServeurGenerique;
import genericRequest.Reponse;
import genericRequest.Traitement;
import lib.BeanDBAcces.BDMouvements;
import protocol.TRAMAP.ReponseTRAMAP;
import protocol.TRAMAP.TraitementTRAMAP;

import java.sql.SQLException;

public class ServeurMouvement extends ServeurGenerique
{

    private BDMouvements _bdMouvements;

    /********************************/
    /*           Variables          */
    /********************************/

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ServeurMouvement(int port, boolean connecte, int NbThreads, BDMouvements _bdMouvements, ConsoleServeur cs)
    {
        super(port, connecte, NbThreads, cs, true);
        this._bdMouvements = _bdMouvements;
    }
    public ServeurMouvement(int port, boolean connecte, int NbThreads, BDMouvements _bdMouvements, ConsoleServeur cs, boolean isJavaCommunication)
    {
        super(port, connecte, NbThreads, cs, isJavaCommunication);
        this._bdMouvements = _bdMouvements;
    }

    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/
    public boolean CreateDataBase(String user, String password, String database)
    {
        try
        {
            _bdMouvements = new BDMouvements(user,password,database);
            return true;
        }
        catch (SQLException | ClassNotFoundException throwables)
        {
            throwables.printStackTrace();
            return false;
        }
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public Traitement CreationTraitement()
    {
        TraitementTRAMAP tt = new TraitementTRAMAP();
        tt.setConsole(this._console);
        tt.set_bd(this._bdMouvements);
        return tt;
    }

    @Override
    public Reponse CreateBusyResponse() {
        return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "Plus de ressource disponible");
    }
}
