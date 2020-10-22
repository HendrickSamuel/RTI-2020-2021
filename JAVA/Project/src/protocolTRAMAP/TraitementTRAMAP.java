/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package protocolTRAMAP;

import MyGenericServer.Client;
import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import genericRequest.Traitement;
import MyGenericServer.ConsoleServeur;
import lib.BeanDBAcces.BDMouvements;
import lib.BeanDBAcces.DataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Override
    public void setDataSource(DataSource ds) throws ClassCastException {
        _bd = (BDMouvements)ds;
        if(_bd == null)
            throw new ClassCastException();
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public Reponse traiteRequete(DonneeRequete Requete, Client client) throws ClassCastException
    {
        if(Requete instanceof DonneeLogin)
            return traiteLOGIN((DonneeLogin)Requete, client);
        else if(Requete instanceof DonneeInputLory)
            return traiteINPUTLORY((DonneeInputLory)Requete, client);
        else if(Requete instanceof DonneeInputLoryWithoutReservation)
            return traiteINPUTLORYWITHOUTRESERVATION( (DonneeInputLoryWithoutReservation)Requete, client);
        else if(Requete instanceof DonneeListOperations)
            traiteListe( (DonneeListOperations)Requete, client);
        else if(Requete instanceof  DonneeLogout)
            traiteLOGOUT( (DonneeLogout)Requete, client);
        else
            return traite404();

        return null;
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

        ResultSet rs = _bd.getLogin(username, password);
        try
        {
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
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "ERREUR lors du traitement de la requete");
    }

    private Reponse traiteINPUTLORY(DonneeInputLory chargeUtile, Client client)
    {
        System.out.println("traiteINPUTLORY");
        String ret = _bd.getReservation(chargeUtile.getNumeroReservation(), chargeUtile.getIdContainer());
        if(ret != null)
        {
            return new ReponseTRAMAP(ReponseTRAMAP.OK, null, null);
        }
        else
        {
            return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "Aucune réservation ne correspond à ce container");
            //todo: verifier si on attent + si les 2 vont ensemble ?
        }
    }

    private Reponse traiteINPUTLORYWITHOUTRESERVATION(DonneeInputLoryWithoutReservation chargeUtile, Client client)
    {
        System.out.println("traiteINPUTLORYWITHOUTRESERVATION");
        String ret = _bd.getInputWithoutRes(chargeUtile.getImmatriculation(), chargeUtile.getIdContainer());
        if(ret != null)
        {
            return new ReponseTRAMAP(ReponseTRAMAP.OK, null, null);
        }
        else
        {
            return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "Problème avec l'input");
            //todo: verifier si on attent + si les 2 vont ensemble ?
        }
    }

    private void traiteListe(DonneeListOperations chargeUtile, Client client)
    {
        System.out.println("traiteListe");

    }

    private Reponse traiteListeSociete(DonneeListOperations chargeUtile)
    {
        List<String> ret = _bd.getListOperationsSociete(chargeUtile.getDateDebut(), chargeUtile.getDateFin(), chargeUtile.getNomSociete());
        if(ret != null)
        {
            return new ReponseTRAMAP(ReponseTRAMAP.OK, null, null);
        }
        else
        {
            return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "Problème avec l'input");
        }
    }

    private Reponse traiteListeDestination(DonneeListOperations chargeUtile)
    {
        List<String> ret = _bd.getListOperationsDestiination(chargeUtile.getDateDebut(), chargeUtile.getDateFin(), chargeUtile.getNomDestination());
        if(ret != null)
        {
            return new ReponseTRAMAP(ReponseTRAMAP.OK, null, null);
        }
        else
        {
            return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "Problème avec l'input");
        }
    }

    private Reponse traite404()
    {
        System.out.println("traite404 Request not found");
        return new ReponseTRAMAP(ReponseTRAMAP.REQUEST_NOT_FOUND, null, "request could not be exeuted due to unsopported version.");
    }

    private Reponse traiteLOGOUT(DonneeLogout chargeUtile, Client client)
    {
        String username = chargeUtile.getUsername();
        String password = chargeUtile.getPassword();

        if(!client.is_loggedIn())
        {
            return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "Le client n'est pas connecte dans le serveur");
        }

        ResultSet rs = _bd.getLogin(username, password);
        try
        {
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
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return new ReponseTRAMAP(ReponseTRAMAP.NOK, null, "ERREUR lors du traitement de la requete");
    }
}
