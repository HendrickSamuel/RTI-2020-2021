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
import protocol.CHAMAP.DonneeMakeBill;
import protocol.CHAMAP.ReponseCHAMAP;
import protocol.CHAMAP.RequeteCHAMAP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TraitementPLAMAP implements Traitement
{

    /********************************/
    /*           Variables          */
    /********************************/
    private BDMouvements _bd;
    private ConsoleServeur _cs;
    private Socket _compta;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public TraitementPLAMAP(BDMouvements _bd, ConsoleServeur _cs, Socket _compta) {
        this._bd = _bd;
        this._cs = _cs;
        this._compta = _compta;
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
        if(Requete == null)
            return traite404();

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

        String username = chargeUtile.getUsername();
        String password = chargeUtile.getPassword();
        if(client.is_loggedIn())
        {
            return new ReponsePLAMAP(ReponsePLAMAP.NOK, "Le client est deja connecte dans le serveur", null);
        }

        try {
            PreparedStatement ps = _bd.getPreparedStatement("SELECT userpassword FROM logins WHERE UPPER(username) = UPPER(?) ;");
            ps.setString(1, username);
            ResultSet rs = _bd.ExecuteQuery(ps);
            if(rs!=null && rs.next())
            {
                String bddpass = rs.getString("userpassword");
                if(password.compareTo(bddpass) == 0)
                {
                    client.set_loggedIn(true);
                    return new ReponsePLAMAP(ReponsePLAMAP.OK, null, chargeUtile);
                }
                else
                {
                    return new ReponsePLAMAP(ReponsePLAMAP.NOK, "Mot de passe ou nom d'utilisateur erroné", null);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ReponsePLAMAP(ReponsePLAMAP.NOK, "ERREUR lors du traitement de la requete", null);
    }

    private Reponse traiteGETXY(DonneeGetXY chargeUtile, Client client)
    {
        System.out.println("traiteGETXY");

        try {
            PreparedStatement ps = _bd.getPreparedStatement("SELECT * " +
                    "FROM parc p " +
                    "INNER JOIN containers c on p.idContainer = c.idContainer " +
                    "INNER JOIN mouvements m on c.idContainer = m.idContainer " +
                    "WHERE UPPER(c.idContainer) = UPPER(?) " +
                    "AND p.etat = 1 " +
                    "AND UPPER(c.idSociete) = UPPER(?) " +
                    "AND UPPER(m.transporteurEntrant) = UPPER(?) " +
                    "AND UPPER(m.destination) = UPPER(?);");
            ps.setString(1, chargeUtile.getIdContainer());
            ps.setString(2, chargeUtile.getSociete());
            ps.setString(3, chargeUtile.getImmatriculationCamion());
            ps.setString(4, chargeUtile.getDestination());

            ResultSet rs = _bd.ExecuteQuery(ps);

            if(rs != null && rs.next())
            {
                chargeUtile.setX(rs.getInt("x"));
                chargeUtile.setY(rs.getInt("y"));
                chargeUtile.setNumReservation(rs.getString("numeroReservation"));
                return new ReponsePLAMAP(ReponsePLAMAP.OK, null, chargeUtile);
            }
            else
            {
                return new ReponsePLAMAP(ReponsePLAMAP.NOK, "un des renseignements envoyés ne correspond pas", null);
            }

        } catch (SQLException throwables) {

            throwables.printStackTrace();
        }

        return new ReponsePLAMAP(ReponsePLAMAP.NOK, "ERREUR lors du traitement de la requete", null);
    }

    private Reponse traiteGETLISTE(DonneeGetList chargeUtile, Client client)
    {
        System.out.println("traiteGETLISTE");
        System.out.println(chargeUtile.toString());

        try{
            PreparedStatement ps = _bd.getPreparedStatement("SELECT * FROM parc WHERE etat = 2 " +
                    "AND upper(destination) = upper(?) " +
                    "ORDER BY dateArrivee DESC LIMIT ?");
            ps.setString(1, chargeUtile.getDestination());
            ps.setInt(2, chargeUtile.getNombreMax());
            ResultSet rs = _bd.ExecuteQuery(ps);
            ArrayList<Container> containers = new ArrayList<>();
            while(rs.next())
            {
                Container container = new Container(rs.getString("idContainer"),
                        rs.getInt("x"), rs.getInt("y"));
                containers.add(container);
            }
            if(containers.size() > 0)
            {
                chargeUtile.setListCont(containers);
                return new ReponsePLAMAP(ReponsePLAMAP.OK, null, chargeUtile);
            }
            else
            {
                return new ReponsePLAMAP(ReponsePLAMAP.NOK, "Aucun container en départ", null);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new ReponsePLAMAP(ReponsePLAMAP.NOK, "Erreur: " + throwables.getMessage(), null);
        }
    }

    private Reponse traiteSENDWEIGHT(DonneeSendWeight chargeUtile, Client client)
    {
        System.out.println("traiteSENDWEIGHT");
        System.out.println(chargeUtile.toString());

        try {
            PreparedStatement ps = _bd.getPreparedStatement("SELECT * " +
                    "FROM parc p " +
                    "INNER JOIN mouvements m on p.idContainer = m.idContainer " +
                    "WHERE p.x = ? " +
                    "AND p.y = ? " +
                    "AND UPPER(p.idContainer) = UPPER(?) " +
                    "AND m.dateDepart IS NULL;");
            ps.setInt(1, chargeUtile.getX());
            ps.setInt(2, chargeUtile.getY());
            ps.setString(3, chargeUtile.getIdContainer());

            ResultSet rs = _bd.ExecuteQuery(ps);
            if(rs != null && rs.next())
            {
                PreparedStatement ps1 = _bd.getPreparedStatement("UPDATE mouvements SET poidsTotal = ? WHERE id = ?");
                ps1.setFloat(1, chargeUtile.getPoids());
                ps1.setInt(2, rs.getInt("m.id"));
                _bd.Execute(ps1);
                PreparedStatement ps2 = _bd.getPreparedStatement("UPDATE parc SET etat = ? WHERE idContainer = ?");
                ps2.setInt(1, 2);
                ps2.setString(2, chargeUtile.getIdContainer());
                _bd.Execute(ps2);
                return new ReponsePLAMAP(ReponsePLAMAP.OK, null, chargeUtile);
            }
            else
            {
                return new ReponsePLAMAP(ReponsePLAMAP.NOK, "ERREUR lors de l'encodage des données", null);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ReponsePLAMAP(ReponsePLAMAP.NOK, "ERREUR lors du traitement de la requete", null);
    }

    private Reponse traiteSIGNALDEP(DonneeSignalDep chargeUtile, Client client)
    {
        System.out.println("traiteSIGNALDEP");
        System.out.println(chargeUtile.toString());
        ArrayList<Integer> mouvements = new ArrayList<>();
        String destination = "";
        String societe = "";

        try{
            PreparedStatement societeps = _bd.getPreparedStatement("SELECT idSociete FROM transporteurs WHERE upper(idTransporteur) = upper(?);");
            societeps.setString(1, chargeUtile.getIdTransporteur());
            ResultSet resultSet = _bd.ExecuteQuery(societeps);
            if(resultSet.next())
            {
                societe = resultSet.getString("idSociete");
            }
            else
            {
                return new ReponsePLAMAP(ReponsePLAMAP.NOK, "TRANSPORTEUR innexistant", null);
            }

            for(String container : chargeUtile.getListIdCont())
            {
                System.out.println("id container = " + container);
                PreparedStatement ps = _bd.getPreparedStatement("SELECT * FROM parc WHERE upper(idContainer) = upper(?) AND etat = 2");
                ps.setString(1, container);
                ResultSet rs = _bd.ExecuteQuery(ps);
                if(rs.next())
                {
                    rs.updateInt("etat",0);
                    rs.updateNull("idContainer");
                    rs.updateNull("dateReservation");
                    rs.updateNull("numeroReservation");
                    rs.updateNull("dateArrivee");

                    destination = rs.getString("destination");
                    _bd.UpdateResult(rs);

                    PreparedStatement insertMouv = _bd.getPreparedStatement("SELECT * FROM mouvements " +
                            "WHERE dateDepart = null AND upper(idContainer) = upper(?);");
                    insertMouv.setString(1, container);
                    ResultSet res = _bd.ExecuteQuery(ps);
                    if(res.next())
                    {
                        mouvements.add(res.getInt("id"));
                        res.updateDate("dateDepart", new Date(Calendar.getInstance().getTime().getTime()));
                        res.updateString("transporteurSortant", chargeUtile.getIdTransporteur());
                    }
                }
                else
                {
                    return new ReponsePLAMAP(ReponsePLAMAP.NOK, "Nous n'avons pas pu retrouver le container dans le parc", null);
                }
            }

            //threading de
            String finalDestination = destination;
            String finalSociete = societe;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SendMakeBill(chargeUtile.getListIdCont(), mouvements, chargeUtile.getIdTransporteur(), finalSociete, finalDestination);
                }
            }).start();

            return new ReponsePLAMAP(ReponsePLAMAP.OK,null, chargeUtile);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new ReponsePLAMAP(ReponsePLAMAP.NOK, "Une erreur s'est produite durant le traitement de votre requete", null);
    }

    private void SendMakeBill(List<String> containers, List<Integer> mouvements, String transporteur, String societe, String destination)
    {
        DonneeMakeBill dmb = new DonneeMakeBill();
        dmb.set_containers(containers);
        dmb.set_mouvements(mouvements);
        dmb.set_transporteur(transporteur);
        dmb.set_societe(societe);
        dmb.set_destination(destination);

        RequeteCHAMAP requeteCHAMAP = new RequeteCHAMAP(dmb);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(_compta.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(_compta.getInputStream());

            oos.writeObject(requeteCHAMAP);
            oos.flush();

            ReponseCHAMAP reponseCHAMAP = (ReponseCHAMAP) ois.readObject();
            if(reponseCHAMAP.getCode() != 200)
            {
                AfficheTraitement(reponseCHAMAP.getMessage()); //todo: prevoir un systeme de renvoi
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Reponse traite404()
    {
        AfficheTraitement("Un client à essayer d'acceder à une mauvaise ressource");
        return new ReponsePLAMAP(ReponsePLAMAP.REQUEST_NOT_FOUND,  "request could not be exeuted due to unsopported version.",null);
    }
}