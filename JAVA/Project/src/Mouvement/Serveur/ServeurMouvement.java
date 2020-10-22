//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 22/10/2020

package Mouvement.Serveur;

import MyGenericServer.ConsoleServeur;
import MyGenericServer.ServeurGenerique;
import genericRequest.Traitement;
import lib.BeanDBAcces.BDMouvements;
import protocolTRAMAP.TraitementTRAMAP;

import java.sql.SQLException;

public class ServeurMouvement extends ServeurGenerique {

    private BDMouvements _bdMouvements;

    /********************************/
    /*           Variables          */
    /********************************/

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ServeurMouvement(int port, boolean connecte, int NbThreads, BDMouvements _bdMouvements, ConsoleServeur cs) {
        super(port, connecte, NbThreads, cs);
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
        try {
            _bdMouvements = new BDMouvements(user,password,database);
            return true;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public Traitement CreationTraitement() {
        TraitementTRAMAP tt = new TraitementTRAMAP();
        tt.setConsole(this._console);
        tt.setDataSource(this._bdMouvements);
        return tt;
    }

}
