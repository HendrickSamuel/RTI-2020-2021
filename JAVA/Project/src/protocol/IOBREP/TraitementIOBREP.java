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

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class TraitementIOBREP implements Traitement {

    /********************************/
    /*           Variables          */
    /********************************/

    private BDMouvements _bdMouvement;
    private BDCompta _bdCompta;

    private ArrayList<String> containersList;
    private ArrayList<String> treatedContainersList;
    private String sortingAlgo;

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
        if(donnee instanceof protocol.IOBREP.DonneeBoatArrived)
            return traiteBoatArrived((protocol.IOBREP.DonneeBoatArrived) donnee, client);
        if(donnee instanceof protocol.IOBREP.DoneeBoatLeft)
            return traiteBoatLeft((protocol.IOBREP.DoneeBoatLeft) donnee, client);
        if(donnee instanceof protocol.IOBREP.DonneeHandleContainerIn)
            return traiteHandleContainerIn((protocol.IOBREP.DonneeHandleContainerIn) donnee, client);
        if(donnee instanceof protocol.IOBREP.DonneeEndContainerIn)
            return traiteEndContainerIn((protocol.IOBREP.DonneeEndContainerIn) donnee, client);
        if(donnee instanceof protocol.IOBREP.DonneeHandleContainerOut)
            return traiteHandleContainerOut((protocol.IOBREP.DonneeHandleContainerOut) donnee, client);
        if(donnee instanceof protocol.IOBREP.DonneeEndContainerOut)
            return traiteEndContainerOut((protocol.IOBREP.DonneeEndContainerOut) donnee, client);
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

        try
        {
            PreparedStatement ps = _bdMouvement.getPreparedStatement("SELECT userpassword FROM logins WHERE UPPER(username) = UPPER(?) ;");
            ps.setString(1, username);
            ResultSet rs = _bdMouvement.ExecuteQuery(ps);
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
        containersList = new ArrayList<>();
        treatedContainersList = new ArrayList<>();
        try{
            PreparedStatement ps = _bdMouvement.getPreparedStatement("SELECT * FROM parc WHERE upper(moyenTransport) = upper(?) AND upper(destination) = upper(?) ORDER BY ?;");
            ps.setString(1, "Bateau");
            ps.setString(2, chargeUtile.getDestination());

            if(chargeUtile.getSelection().equals("FIRST"))
                ps.setString(3, "dateArrivee");
            else
                ps.setString(3, "RAND()");

            this.sortingAlgo = chargeUtile.getSelection();

            ResultSet rs = _bdMouvement.ExecuteQuery(ps);

            ArrayList<Container> containers = new ArrayList<>();
            while (rs.next())
            {
                Container cont = new Container();
                cont.setId(rs.getString("idContainer"));
                containersList.add(cont.getId());
                cont.setX(rs.getInt("x"));
                cont.setY(rs.getInt("y"));
                containers.add(cont);
            }
            chargeUtile.set_containers(containers);
            return new ReponseIOBREP(ReponseIOBREP.OK, chargeUtile, null);
        }
         catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ReponseIOBREP(ReponseIOBREP.NOK, null, "ERREUR lors du traitement de la requete");

    }

    private Reponse traiteBoatArrived(DonneeBoatArrived chargeUtile, Client client)
    {
        try {
            PreparedStatement ps = _bdMouvement.getPreparedStatement("SELECT * FROM transporteurs WHERE upper(idTransporteur) = upper(?);");
            ps.setString(1, chargeUtile.getIdContainer());
            ResultSet rs = _bdMouvement.ExecuteQuery(ps);
            if(rs != null && rs.next())
            {
                rs.updateBoolean("present",true);
                _bdMouvement.UpdateResult(rs);
                return new ReponseIOBREP(ReponseIOBREP.OK ,chargeUtile, null);
            }
            else
            {
                return new ReponseIOBREP(ReponseIOBREP.NOK ,null, "Aucun bateau ne correspond à cet identifiant");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new ReponseIOBREP(ReponseIOBREP.NOK ,null, "Erreur lors de l'execution de la requete");
        }
    }

    private Reponse traiteBoatLeft(DoneeBoatLeft chargeUtile, Client client)
    {
        try {
            PreparedStatement ps = _bdMouvement.getPreparedStatement("SELECT * FROM transporteurs WHERE upper(idTransporteur) = upper(?);");
            ps.setString(1, chargeUtile.getIdContainer());
            ResultSet rs = _bdMouvement.ExecuteQuery(ps);
            if(rs != null && rs.next())
            {
                rs.updateBoolean("present",false);
                _bdMouvement.UpdateResult(rs);
                return new ReponseIOBREP(ReponseIOBREP.OK ,chargeUtile, null);
            }
            else
            {
                return new ReponseIOBREP(ReponseIOBREP.NOK ,null, "Aucun bateau ne correspond à cet identifiant");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new ReponseIOBREP(ReponseIOBREP.NOK ,null, "Erreur lors de l'execution de la requete");
        }
    }

    private Reponse traiteHandleContainerIn(protocol.IOBREP.DonneeHandleContainerIn chargeUtile, Client client)
    {
        //todo: mettre le container dans le parc + oui ou non ?
        return null;
    }

    private Reponse traiteEndContainerIn(protocol.IOBREP.DonneeEndContainerIn chargeUtile, Client client)
    {
        //todo: finir le dechargement ?
        return null;
    }

    private Reponse traiteHandleContainerOut(protocol.IOBREP.DonneeHandleContainerOut chargeUtile, Client client)
    {
        //todo: verifier qu'on ne depasse pas la capacite max ?
        if(sortingAlgo.equals("FIRST"))
        {
            if(chargeUtile.getIdContainer().toUpperCase().equals(containersList.get(0))){
                treatedContainersList.add(containersList.remove(0));
                return new ReponseIOBREP(ReponseIOBREP.OK, chargeUtile, null);
            }
            else
                return new ReponseIOBREP(ReponseIOBREP.NOK, null, "Not first container in list");
        }
        else
        {
            if(containersList.contains(chargeUtile.getIdContainer().toUpperCase()))
            {
                treatedContainersList.add(containersList.remove(containersList.indexOf(chargeUtile.getIdContainer())));
                return new ReponseIOBREP(ReponseIOBREP.OK, chargeUtile, null);
            }
            else
                return new ReponseIOBREP(ReponseIOBREP.NOK, null, "Container not in the list");
        }

    }

    private Reponse traiteEndContainerOut(protocol.IOBREP.DonneeEndContainerOut chargeUtile, Client client)
    {
        for(String container : treatedContainersList)
        {
            try {
                PreparedStatement ps = _bdMouvement.getPreparedStatement("SELECT * FROM mouvements WHERE UPPER(idContainer) = UPPER(?) AND dateDepart IS NULL;");
                ps.setString(1, container);
                ResultSet rs = _bdMouvement.ExecuteQuery(ps);
                if(rs != null && rs.next())
                {
                    rs.updateString("transporteurSortant",chargeUtile.getIdBateau());
                    rs.updateDate("dateDepart", new java.sql.Date(Calendar.getInstance().getTime().getTime()));
                    _bdMouvement.UpdateResult(rs);
                    //todo: utiliser un update plutot q'un select ?
                    PreparedStatement pre = _bdMouvement.getPreparedStatement("SELECT * FROM parc WHERE UPPER(idContainer) = UPPER(?);");
                    pre.setString(1, container);
                    ResultSet res = _bdMouvement.ExecuteQuery(pre);
                    if(res != null && res.next())
                    {
                        res.updateNull("idContainer"); //idcontainer à null
                        res.updateInt("etat",0); //etat à 0
                        _bdMouvement.UpdateResult(res);

                        return new ReponseIOBREP(ReponseIOBREP.OK, chargeUtile, null);
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return new ReponseIOBREP(ReponseIOBREP.NOK, null, "Message d'erreur");
        }
        return null;
    }

    private Reponse traite404()
    {
        System.out.println("traite404 Request not found");
        return new ReponseIOBREP(ReponseIOBREP.REQUEST_NOT_FOUND, null, "request could not be exeuted due to unsopported version.");
    }

    public void ClientLeft()
    {
        //todo: reinitialiser tout les traitements et remettre le bateau en parti ?
    }
}
