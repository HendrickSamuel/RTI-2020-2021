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

import javax.print.Doc;
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
    private ArrayList<String> containersListIn;
    private ArrayList<String> treatedContainersListIn;

    private String sortingAlgo;
    private String dockerName;

    private int dockersid;

    private boolean startedLoading = false;
    private boolean startedUnloading = false;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public TraitementIOBREP(BDMouvements _bdMouvement, BDCompta _bdCompta)
    {
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
    public void setConsole(ConsoleServeur cs)
    {

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

    private void NewBoatStart()
    {
        containersList = new ArrayList<>();
        treatedContainersList = new ArrayList<>();
        containersListIn = new ArrayList<>();
        treatedContainersListIn = new ArrayList<>();
        startedLoading = false;
        startedUnloading = false;
    }

    @Override
    public Reponse traiteRequete(DonneeRequete donnee, Client client) throws ClassCastException {
        if(donnee instanceof protocol.IOBREP.DonneeLogin)
            return traiteLOGIN((protocol.IOBREP.DonneeLogin) donnee, client);
        if(donnee instanceof protocol.IOBREP.DonneeGetContainers)
            if(((DonneeGetContainers) donnee).getMode().equals("IN"))
                return traiteGetContainersIN((protocol.IOBREP.DonneeGetContainers) donnee, client);
            else
                return traiteGetContainersOUT((protocol.IOBREP.DonneeGetContainers) donnee, client);
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
        if(donnee instanceof protocol.IOBREP.DonneeGetLoadUnloadStats)
            return traiteDonneeGetLoadUnloadStats((protocol.IOBREP.DonneeGetLoadUnloadStats) donnee, client);
        if(donnee instanceof protocol.IOBREP.DonneeGetLoadUnloadTime)
            return traiteDonneeGetLoadUnloadTime((protocol.IOBREP.DonneeGetLoadUnloadTime) donnee, client);
        if(donnee instanceof protocol.IOBREP.DonneeGetLoadUnloadStatsWeekly)
            return traiteGetLoadUnloadStatsWeeklty((protocol.IOBREP.DonneeGetLoadUnloadStatsWeekly) donnee, client);

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
                    dockerName = username;
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

    private Reponse traiteGetContainersIN(DonneeGetContainers chargeUtile, Client client)
    {
        try{
            PreparedStatement ps = _bdMouvement.getPreparedStatement(
                    "SELECT * FROM mouvements " +
                            "WHERE upper(transporteurEntrant) = upper(?) " +
                            "AND dateArrivee IS NULL " +
                            "AND etape = 1 " +
                            "ORDER BY RAND();");

            ps.setString(1, chargeUtile.getIdBateau());

            System.out.println(ps);

            ResultSet rs = _bdMouvement.ExecuteQuery(ps);

            ArrayList<Container> containers = new ArrayList<>();
            while (rs.next())
            {
                Container cont = new Container();
                cont.setId(rs.getString("idContainer"));
                containersListIn.add(cont.getId());
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

    private Reponse traiteGetContainersOUT(DonneeGetContainers chargeUtile, Client client)
    {
        try{
            PreparedStatement pse = _bdMouvement.getPreparedStatement("UPDATE dockers set destination = ? WHERE id = ? ");
            pse.setString(1, chargeUtile.getDestination());
            pse.setInt(2, this.dockersid);
            _bdMouvement.Execute(pse);

            String ordre;
            if(chargeUtile.getSelection().equals("FIRST"))
                ordre = "dateArrivee";
            else
                ordre = "RAND()";
            this.sortingAlgo = chargeUtile.getSelection();

            PreparedStatement ps = _bdMouvement.getPreparedStatement(
                    "SELECT * " +
                    "FROM parc " +
                    "WHERE upper(moyenTransport) = upper(?) " +
                    "AND idContainer IS NOT NULL " +
                    "AND upper(destination) = upper(?) " +
                    "ORDER BY "+ ordre +";");

            ps.setString(1, "Bateau");
            ps.setString(2, chargeUtile.getDestination());

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
        //Linitialisation des listes des containers
        NewBoatStart();

        try {
            PreparedStatement ps = _bdMouvement.getPreparedStatement("SELECT * FROM transporteurs WHERE upper(idTransporteur) = upper(?) AND types='B';");
            ps.setString(1, chargeUtile.getIdContainer());
            ResultSet rs = _bdMouvement.ExecuteQuery(ps);
            if(rs != null && rs.next())
            {
                rs.updateBoolean("present",true);
                _bdMouvement.UpdateResult(rs);

                PreparedStatement psDocker = _bdMouvement.getPreparedStatement("INSERT INTO dockers (idDocker, idBateau) VALUES (?,?);");
                psDocker.setString(1, dockerName);
                psDocker.setString(2, chargeUtile.getIdContainer());
                _bdMouvement.Execute(psDocker);

                PreparedStatement psid = _bdMouvement.getPreparedStatement("SELECT * from dockers ORDER BY id DESC LIMIT 1");
                ResultSet res = _bdMouvement.ExecuteQuery(psid);
                if(res!= null && res.next())
                {
                    dockersid = res.getInt("id");
                }

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
            PreparedStatement ps = _bdMouvement.getPreparedStatement("SELECT * FROM transporteurs WHERE upper(idTransporteur) = upper(?) AND types ='B';");
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
        if(!startedUnloading)
        {
            try {
                PreparedStatement ps = _bdMouvement.getPreparedStatement("UPDATE dockers SET startBoatUnload = ? WHERE id = ?");
                ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                ps.setInt(2, this.dockersid);
                _bdMouvement.Execute(ps);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            startedUnloading = true;
        }

        if(containersListIn.contains(chargeUtile.getIdContainer()))
        {
            treatedContainersListIn.add(containersListIn.remove(containersListIn.indexOf(chargeUtile.getIdContainer())));
            try {
                PreparedStatement ps = _bdMouvement.getPreparedStatement("SELECT * FROM parc " +
                        "WHERE etat = 0 " +
                        "OR ( upper(idContainer) = upper(?) " +
                        "AND etat = 1)");

                ps.setString(1, chargeUtile.getIdContainer());

                ResultSet rs = _bdMouvement.ExecuteQuery(ps);
                if(rs!= null && rs.next())
                {
                    rs.updateInt("etat", 1);
                    rs.updateString("idContainer", chargeUtile.getIdContainer());
                    _bdMouvement.UpdateResult(rs);

                }
                else
                {
                    return new ReponseIOBREP(ReponseIOBREP.NOK, null, "No place to unload");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return new ReponseIOBREP(ReponseIOBREP.NOK, null, "Erreur sur la base de donnée");
            }
            return new ReponseIOBREP(ReponseIOBREP.OK, chargeUtile, null);
        }
        else
            return new ReponseIOBREP(ReponseIOBREP.NOK, null, "Container not in the list");
    }

    private Reponse traiteEndContainerIn(protocol.IOBREP.DonneeEndContainerIn chargeUtile, Client client)
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if(!startedUnloading)
        {
            try {
                PreparedStatement ps = _bdMouvement.getPreparedStatement("UPDATE dockers SET startBoatUnload = ? , endBoatUnload = ? , containersDecharges = ?  WHERE id = ?");
                ps.setTimestamp(1, now);
                ps.setTimestamp(2, now);
                ps.setInt(3, treatedContainersListIn.size());
                ps.setInt(4, dockersid);
                _bdMouvement.Execute(ps);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else
        {
            try {
                PreparedStatement ps = _bdMouvement.getPreparedStatement("UPDATE dockers SET endBoatUnload = ? , containersDecharges = ?  WHERE id = ?");
                ps.setTimestamp(1, now);
                ps.setInt(2, treatedContainersListIn.size());
                ps.setInt(3, dockersid);
                _bdMouvement.Execute(ps);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        for(String container : treatedContainersListIn)
        {
            try {
                PreparedStatement ps = _bdMouvement.getPreparedStatement("SELECT * FROM mouvements WHERE UPPER(idContainer) = UPPER(?) AND dateArrivee IS NULL;");
                ps.setString(1, container);
                ResultSet rs = _bdMouvement.ExecuteQuery(ps);
                if(rs != null && rs.next())
                {
                    rs.updateString("transporteurEntrant",chargeUtile.getIdBateau());
                    rs.updateDate("dateArrivee", new java.sql.Date(Calendar.getInstance().getTime().getTime()));
                    _bdMouvement.UpdateResult(rs);

                    PreparedStatement pre = _bdMouvement.getPreparedStatement("SELECT * FROM parc WHERE UPPER(idContainer) = UPPER(?);");
                    pre.setString(1, container);
                    ResultSet res = _bdMouvement.ExecuteQuery(pre);
                    if(res != null && res.next())
                    {
                        res.updateInt("etat",2); //etat à 0
                        _bdMouvement.UpdateResult(res);
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return new ReponseIOBREP(ReponseIOBREP.NOK, null, "Erreur de validation du container: " + container);
            }
        }
        return new ReponseIOBREP(ReponseIOBREP.OK, chargeUtile, null);
    }

    private Reponse traiteHandleContainerOut(protocol.IOBREP.DonneeHandleContainerOut chargeUtile, Client client)
    {
        if(!startedLoading)
        {
            try {
                PreparedStatement ps = _bdMouvement.getPreparedStatement("UPDATE dockers SET startBoatLoad = ? WHERE id = ? ");
                ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                ps.setInt(2, this.dockersid);
                _bdMouvement.Execute(ps);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            startedLoading = true;
        }

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
        //si on a rien chargé alors le début = fin
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if(!startedLoading)
        {
            try {
                PreparedStatement ps = _bdMouvement.getPreparedStatement("UPDATE dockers SET startBoatLoad = ? , endBoatLoad = ? , containersCharges = ?  WHERE id = ?");
                ps.setTimestamp(1, now);
                ps.setTimestamp(2, now);
                ps.setInt(3, treatedContainersList.size());
                ps.setInt(4, dockersid);
                _bdMouvement.Execute(ps);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else
        {
            try {
                PreparedStatement ps = _bdMouvement.getPreparedStatement("UPDATE dockers SET endBoatLoad = ? , containersCharges = ? WHERE id = ?");
                ps.setTimestamp(1, now);
                ps.setInt(2, treatedContainersList.size());
                ps.setInt(3, dockersid);
                _bdMouvement.Execute(ps);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

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

                    PreparedStatement pre = _bdMouvement.getPreparedStatement("SELECT * FROM parc WHERE UPPER(idContainer) = UPPER(?);");
                    pre.setString(1, container);
                    ResultSet res = _bdMouvement.ExecuteQuery(pre);
                    if(res != null && res.next())
                    {
                        res.updateNull("idContainer"); //idcontainer à null
                        res.updateNull("moyenTransport"); //idcontainer à null
                        res.updateInt("etat",0); //etat à 0
                        _bdMouvement.UpdateResult(res);
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return new ReponseIOBREP(ReponseIOBREP.NOK, null, "Erreur de validation du container: " + container);
            }
        }
        return new ReponseIOBREP(ReponseIOBREP.OK, chargeUtile, null);
    }

    private Reponse traiteDonneeGetLoadUnloadStats(protocol.IOBREP.DonneeGetLoadUnloadStats chargeUtile, Client client)
    {
        try {
            ResultSet rs = _bdMouvement.ExecuteQuery(_bdMouvement.CreateUpdatableStatement(),
                    "SELECT SUM(containersCharges) AS cc, SUM(containersDecharges) AS cd, DATE(startBoatLoad) AS datemouvement " +
                            "FROM dockers " +
                            "WHERE destination IS NOT NULL " +
                            "GROUP BY DATE(startBoatLoad)");
            ArrayList<Day> liste = new ArrayList<>();
            while(rs.next())
            {
                Day day = new Day(rs.getTimestamp("datemouvement").toString(),
                        rs.getInt("cc"),
                        rs.getInt("cd"));
                liste.add(day);
            }
            chargeUtile.setJours(liste);
            return new ReponseIOBREP(ReponseIOBREP.OK, chargeUtile, null);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ReponseIOBREP(ReponseIOBREP.NOK, null, "Erreur d'execution");
    }

    private Reponse traiteDonneeGetLoadUnloadTime(protocol.IOBREP.DonneeGetLoadUnloadTime chargeUtile, Client clietn)
    {
        try{
            PreparedStatement ps = _bdMouvement.getPreparedStatement(
                    "SELECT AVG(endBoatLoad - startBoatLoad) AS loadsecc, AVG(endBoatUnload - startBoatUnload) AS unloadsecc, idDocker " +
                            "FROM dockers " +
                            "GROUP BY idDocker;");
            ResultSet rs = _bdMouvement.ExecuteQuery(ps);
            ArrayList<Docker> dockers = new ArrayList<>();
            while(rs.next())
            {
                Docker dock = new Docker(rs.getString("idDocker"),
                        rs.getDouble("loadsecc"),
                        rs.getDouble("unloadsecc"));
                dockers.add(dock);
            }

            chargeUtile.setDockers(dockers);
            return new ReponseIOBREP(ReponseIOBREP.OK, chargeUtile, null);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ReponseIOBREP(ReponseIOBREP.NOK, null, "Erreur d'execution");
    }

    private Reponse traiteGetLoadUnloadStatsWeeklty(DonneeGetLoadUnloadStatsWeekly chargeUtile, Client client)
    {
        try{
            PreparedStatement ps = _bdMouvement.getPreparedStatement(
                    "SELECT SUM(containersCharges) AS cc, SUM(containersDecharges) AS cd, WEEK(DATE(startBoatLoad)) AS semaine, destination " +
                            "FROM dockers " +
                            "WHERE destination IS NOT NULL " +
                            "GROUP BY WEEK(DATE(startBoatLoad)), destination " +
                            "ORDER by semaine");
            ResultSet rs = _bdMouvement.ExecuteQuery(ps);

            ArrayList<Week> weeks = new ArrayList<>();
            Week week = new Week(-1);
            int weeknNumber = -1;
            while(rs.next())
            {
                if(weeknNumber == -1 || weeknNumber != rs.getInt("semaine"))
                {
                    week = new Week(rs.getInt("semaine"));
                    weeks.add(week);
                }

                weeknNumber = rs.getInt("semaine");

                week.getDestinations().add(rs.getString("destination"));
                week.getLoadedContainers().add(rs.getInt("cc"));
                week.getUnloadedContainers().add(rs.getInt("cd"));
            }

            chargeUtile.setWeeks(weeks);
            return new ReponseIOBREP(ReponseIOBREP.OK, chargeUtile, null);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ReponseIOBREP(ReponseIOBREP.NOK, null, "Erreur d'execution");
    }

    private Reponse traite404()
    {
        System.out.println("traite404 Request not found");
        return new ReponseIOBREP(ReponseIOBREP.REQUEST_NOT_FOUND, null, "request could not be exeuted due to unsopported version.");
    }
}
