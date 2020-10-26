/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package protocol.TRAMAP;

import MyGenericServer.Client;
import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import genericRequest.Traitement;
import MyGenericServer.ConsoleServeur;
import lib.BeanDBAcces.BDMouvements;
import lib.BeanDBAcces.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TraitementTRAMAP implements Traitement
{
    BDMouvements _bd;
    ConsoleServeur _cs;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public TraitementTRAMAP()
    {

    }

    public TraitementTRAMAP(BDMouvements _bd) {
        this._bd = _bd;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public BDMouvements get_bd() {
        return _bd;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_bd(BDMouvements _bd) {
        this._bd = _bd;
    }

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
        if(Requete instanceof DonneeLogin)
            return traiteLOGIN((DonneeLogin)Requete, client);
        else if(Requete instanceof DonneeInputLory)
            return traiteINPUTLORY((DonneeInputLory)Requete, client);
        else if(Requete instanceof DonneeInputLoryWithoutReservation)
            return traiteINPUTLORYWITHOUTRESERVATION( (DonneeInputLoryWithoutReservation)Requete, client);
        else if(Requete instanceof DonneeListOperations)
            return traiteListe( (DonneeListOperations)Requete, client);
        else
            return traite404();
    }

    @Override
    public void AfficheTraitement(String message) {
        if (_cs != null)
        {
            _cs.Affiche(message);
        }
        else
        {
            System.err.println("-- Le serveur n'a pas de console dédiée pour ce message -- " + message);
        }
    }

    private Reponse traiteLOGIN(DonneeLogin chargeUtile, Client client)
    {
        String username = chargeUtile.getUsername();
        String password = chargeUtile.getPassword();
        if(client.is_loggedIn())
        {
            return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "Le client est deja connecte dans le serveur");
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
                    return new ReponseTRAMAP(ReponseTRAMAP.OK, null, null);
                }
                else
                {
                    return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "Mot de passe ou nom d'utilisateur erroné");
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "ERREUR lors du traitement de la requete");

    }

    private Reponse traiteINPUTLORY(DonneeInputLory chargeUtile, Client client)
    {
        System.out.println("traiteINPUTLORY");
        if(!client.is_loggedIn())
        {
            return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "Le client n'est pas connecte dans le serveur");
        }

        try {
            PreparedStatement ps = _bd.getPreparedStatement("SELECT * FROM parc WHERE UPPER(idContainer) = UPPER(?) AND UPPER(numeroReservation) = UPPER(?);");
            ps.setString(1, chargeUtile.getIdContainer());
            ps.setString(2, chargeUtile.getNumeroReservation());
            System.out.println(ps);
            ResultSet ret = _bd.ExecuteQuery(ps);
            if(ret != null && ret.next())
            {
                chargeUtile.setX(ret.getInt("x"));
                chargeUtile.setY(ret.getInt("y"));
                return new ReponseTRAMAP(ReponseTRAMAP.OK, chargeUtile, null);
            }
            else
            {
                return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "Aucune réservation ne correspond à ce container");
                //todo: verifier si on attent + si les 2 vont ensemble ?
            }
        } catch (SQLException throwables) {
            return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "Erreur lors de la connection à la base de données");
        }
    }

    private boolean HasRecords(ResultSet rs)
    {
        try {
            return rs.next();
        } catch (SQLException throwables) {
            return false;
        }
    }

    private Reponse traiteINPUTLORYWITHOUTRESERVATION(DonneeInputLoryWithoutReservation chargeUtile, Client client)
    {
        System.out.println("traiteINPUTLORYWITHOUTRESERVATION");
        if(!client.is_loggedIn())
        {
            return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "Le client n'est pas connecte dans le serveur");
        }

        if(!HasRecords(_bd.getSocieteById(chargeUtile.getSociete())))
        {
            _bd.insertSociete(chargeUtile.getSociete(), "","","");
        }

        if(!HasRecords(_bd.getContainerById(chargeUtile.getIdContainer())))
        {
            _bd.insertContainer(chargeUtile.getIdContainer(),
                    chargeUtile.getSociete(),
                    "Inconnu",
                    0F,
                    "Inconnu",
                    0F);
        }

        try {
            ResultSet ret = _bd.ExecuteQuery(_bd.CreateUpdatableStatement(), "SELECT * FROM parc WHERE etat = 0");
            if(ret != null && ret.next())
            {
                ret.updateString("idContainer", chargeUtile.getIdContainer());
                ret.updateInt("etat", 1);
                ret.updateString("destination",chargeUtile.getDestination());
                ret.updateRow();
                chargeUtile.setX(ret.getInt("x"));
                chargeUtile.setY(ret.getInt("y"));
                return new ReponseTRAMAP(ReponseTRAMAP.OK, chargeUtile, null);
            }
            else
            {
                return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "Problème avec l'input");
            }
        } catch (SQLException throwables) {
            System.out.println("Erreur" + throwables.getSQLState() + " | " + throwables.getErrorCode());
            return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "Erreur de la requete");
        }
    }

    private Reponse traiteListe(DonneeListOperations chargeUtile, Client client)
    {
        try {
            ResultSet resultat;
            if(chargeUtile.getNomSociete() != null && chargeUtile.getNomDestination() == null)
                    resultat = traiteListeSociete(chargeUtile);
            else
                    resultat = traiteListeDestination(chargeUtile);

            if(resultat != null)
            {
                ArrayList<Operation> liste = new ArrayList<Operation>();
                while(resultat.next())
                {
                    liste.add(CreateOperation(resultat));
                }

                if(liste.size() > 0)
                {
                    chargeUtile.setOperations(liste);
                    return new ReponseTRAMAP(ReponseTRAMAP.OK, chargeUtile, null);
                }
                else
                    return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "Aucune correspondance");
            }
            else
                return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "Problème avec l'input");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "Problème avec la base de données");
        }

    }

    private Operation CreateOperation(ResultSet res) throws SQLException {
        Operation op = new Operation();
        op.set_id(res.getInt("id"));
        op.set_container(res.getString("idContainer"));
        op.set_transporteurEntrant(res.getString("transporteurEntrant"));
        op.set_dateArrivee(res.getDate("dateArrivee"));
        op.set_transporteurSortant(res.getString("transporteurSortant"));
        op.set_poidsTotal(res.getFloat("poidsTotal"));
        op.set_dateDepart(res.getDate("dateDepart"));
        op.set_destination(res.getString("destination"));
        return op;
    }

    private ResultSet traiteListeSociete(DonneeListOperations chargeUtile) throws SQLException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        PreparedStatement ps = _bd.getPreparedStatement("SELECT * FROM mouvements " +
                "INNER JOIN containers c on mouvements.idContainer = c.idContainer " +
                "INNER JOIN societes s on c.idSociete = s.nom " +
                "WHERE UPPER(s.nom) = UPPER(?) " +
                "AND (dateArrivee BETWEEN ? AND ? OR (dateDepart BETWEEN ? AND ?));");
        ps.setString(1, chargeUtile.getNomSociete());
        ps.setString(2, formatter.format(chargeUtile.getDateDebut()));
        ps.setString(3, formatter.format(chargeUtile.getDateFin()));
        ps.setString(4, formatter.format(chargeUtile.getDateDebut()));
        ps.setString(5, formatter.format(chargeUtile.getDateFin()));

        return _bd.ExecuteQuery(ps);
    }

    private ResultSet traiteListeDestination(DonneeListOperations chargeUtile) throws SQLException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        PreparedStatement ps = _bd.getPreparedStatement("SELECT * FROM mouvements " +
                "WHERE UPPER(destination) = UPPER(?)" +
                "AND (dateArrivee BETWEEN ? AND ? OR (dateDepart BETWEEN ? AND ?));");
        ps.setString(1, chargeUtile.getNomDestination());
        ps.setString(2, formatter.format(chargeUtile.getDateDebut()));
        ps.setString(3, formatter.format(chargeUtile.getDateFin()));
        ps.setString(4, formatter.format(chargeUtile.getDateDebut()));
        ps.setString(5, formatter.format(chargeUtile.getDateFin()));

        System.out.println(ps);

        return _bd.ExecuteQuery(ps);
    }

    private Reponse traite404()
    {
        System.out.println("traite404 Request not found");
        return new ReponseTRAMAP(ReponseTRAMAP.REQUEST_NOT_FOUND, null, "request could not be exeuted due to unsopported version.");
    }
}
