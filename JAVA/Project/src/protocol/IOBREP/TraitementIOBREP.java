//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 22/10/2020

package protocol.IOBREP;

import MyGenericServer.Client;
import MyGenericServer.ConsoleServeur;
import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import genericRequest.Traitement;
import lib.BeanDBAcces.BDCompta;
import lib.BeanDBAcces.BDMouvements;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TraitementIOBREP implements Traitement {

    /********************************/
    /*           Variables          */
    /********************************/

    private BDMouvements _bdMouvement;
    private BDCompta _bdCompta;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public TraitementIOBREP(BDMouvements _bdMouvement, BDCompta _bdCompta) {
        this._bdMouvement = _bdMouvement;
        this._bdCompta = _bdCompta;
    }

    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/

    @Override
    public void setConsole(ConsoleServeur cs) {

    }

    public void set_bdMouvement(BDMouvements _bdMouvement) {
        this._bdMouvement = _bdMouvement;
    }

    public void set_bdCompta(BDCompta _bdCompta) {
        this._bdCompta = _bdCompta;
    }

    /********************************/
    /*            Methodes          */
    /********************************/

    @Override
    public Reponse traiteRequete(DonneeRequete donnee, Client client) throws ClassCastException {
        if(donnee instanceof protocol.IOBREP.DonneeLogin)
            return traiteLOGIN((protocol.IOBREP.DonneeLogin) donnee, client);
        if(donnee instanceof protocol.IOBREP.DonneeGetContainers)
            return traiteGetContainers((protocol.IOBREP.DonneeGetContainers) donnee, client);
        else
            return traite404();
    }

    @Override
    public void AfficheTraitement(String message) {

    }

    private Reponse traiteLOGIN(protocol.IOBREP.DonneeLogin chargeUtile, Client client)
    {
        String username = chargeUtile.getUsername();
        String password = chargeUtile.getPassword();
        if(client.is_loggedIn())
        {
            return new ReponseIOBREP(ReponseIOBREP.NOK, null, "Le client est deja connecte dans le serveur");
        }

        ResultSet rs = _bdMouvement.getLogin(username, password); //todo: changer de base de donnée
        try
        {
            if(rs!=null && rs.next())
            {
                String bddpass = rs.getString("userpassword");
                if(password.compareTo(bddpass) == 0)
                {
                    client.set_loggedIn(true);
                    return new ReponseIOBREP(ReponseIOBREP.OK, null, null);
                }
                else
                {
                    return new ReponseIOBREP(ReponseIOBREP.NOK, null, "Mot de passe ou nom d'utilisateur erroné");
                }
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return new ReponseIOBREP(ReponseIOBREP.NOK, null, "ERREUR lors du traitement de la requete");
    }

    private Reponse traiteGetContainers(DonneeGetContainers chargeUtile, Client client)
    {
        ResultSet rs = _bdMouvement.getContainers("Bateau","Paris",true);
        try{
            while (rs.next())
            {
                System.out.println(rs.getString("idContainer"));
            }
        }
         catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ReponseIOBREP(ReponseIOBREP.NOK, null, "ERREUR lors du traitement de la requete");

    }

    private Reponse traite404()
    {
        System.out.println("traite404 Request not found");
        return new ReponseIOBREP(ReponseIOBREP.REQUEST_NOT_FOUND, null, "request could not be exeuted due to unsopported version.");
    }

}
