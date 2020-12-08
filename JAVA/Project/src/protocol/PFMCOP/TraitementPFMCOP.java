//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 08/12/2020

package protocol.PFMCOP;

import MyGenericServer.Client;
import MyGenericServer.ConsoleServeur;
import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import genericRequest.Traitement;
import lib.BeanDBAcces.BDCompta;
import protocol.TRAMAP.ReponseTRAMAP;
import security.SecurityHelper;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TraitementPFMCOP  implements Traitement
{
    /********************************/
    /*           Variables          */
    /********************************/
    private BDCompta _bd;
    private ConsoleServeur _cs;
    private Socket _chat;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public TraitementPFMCOP()
    {

    }

    public TraitementPFMCOP(ConsoleServeur _cs, Socket _chat)
    {
        this._cs = _cs;
        this._chat = _chat;
    }

    public TraitementPFMCOP(ConsoleServeur _cs, Socket _chat, BDCompta _bd)
    {
        this._cs = _cs;
        this._chat = _chat;
        this._bd = _bd;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public BDCompta get_bd()
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
    public void set_bd(BDCompta _bd)
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

        if(Requete instanceof DonneeLoginGroup)
            return traiteLOGINGROUP((DonneeLoginGroup)Requete, client);
        else if(Requete instanceof DonneePostQuestion)
            return traitePOSTQUESTION((DonneePostQuestion)Requete, client);
        else if(Requete instanceof DonneeAnswerQuestion)
            return traiteANSWERQUESTION( (DonneeAnswerQuestion)Requete, client);
        else if(Requete instanceof DonneePostEvent)
            return traitePOSTEVENT( (DonneePostEvent)Requete, client);
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

    private Reponse traiteLOGINGROUP(DonneeLoginGroup chargeUtile, Client client)
    {
        System.out.println("traiteLOGINGROUP");
        System.out.println(chargeUtile.toString());

        String username = chargeUtile.get_username();
        byte[] pwdDigest = chargeUtile.get_pwdDigest();
        long time = chargeUtile.get_temps();
        double alea = chargeUtile.get_aleatoire();

        try
        {
            PreparedStatement ps = _bd.getPreparedStatement("SELECT userpassword FROM personnel WHERE UPPER(username) = UPPER(?) ;");
            ps.setString(1, username);
            ResultSet rs = _bd.ExecuteQuery(ps);
            if(rs!=null && rs.next())
            {
                String bddpass = rs.getString("userpassword");

                SecurityHelper sh = new SecurityHelper();

                if(sh.CompareDigests(pwdDigest, sh.createSaltedDigest(bddpass, time, alea), time, alea))
                {
                    client.set_loggedIn(true);
                    return new ReponsePFMCOP(ReponsePFMCOP.OK, null, null);
                }
                else
                {
                    return new ReponsePFMCOP(ReponsePFMCOP.NOK, "Mot de passe ou nom d'utilisateur erroné", null);
                }
            }
        }
        catch (SQLException | NoSuchProviderException | NoSuchAlgorithmException | IOException throwables)
        {
            throwables.printStackTrace();
        }
        return new ReponsePFMCOP(ReponsePFMCOP.NOK, "ERREUR lors du traitement de la requete", null);
    }

    private Reponse traitePOSTQUESTION(DonneePostQuestion chargeUtile, Client client)
    {
        System.out.println("traitePOSTQUESTION");
        System.out.println(chargeUtile.toString());

        return null;
    }

    private Reponse traiteANSWERQUESTION(DonneeAnswerQuestion chargeUtile, Client client)
    {
        System.out.println("traiteANSWERQUESTION");
        System.out.println(chargeUtile.toString());

        return null;
    }

    private Reponse traitePOSTEVENT(DonneePostEvent chargeUtile, Client client)
    {
        System.out.println("traitePOSTEVENT");
        System.out.println(chargeUtile.toString());

        return null;
    }

    private Reponse traite404()
    {
        AfficheTraitement("Un client à essayer d'acceder à une mauvaise ressource");
        return new ReponsePFMCOP(ReponsePFMCOP.REQUEST_NOT_FOUND,  "request could not be exeuted due to unsopported version.",null);
    }
}