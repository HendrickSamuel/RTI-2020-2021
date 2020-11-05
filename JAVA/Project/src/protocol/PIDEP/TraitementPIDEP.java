//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 01/11/2020

package protocol.PIDEP;

import java.sql.ResultSet;
import java.io.IOException;
import java.sql.SQLException;
import MyGenericServer.Client;
import genericRequest.*;

import java.io.ByteArrayOutputStream;
import java.security.Security;
import java.io.DataOutputStream;
import java.sql.PreparedStatement;
import java.security.MessageDigest;
import lib.BeanDBAcces.BDMouvements;

import java.io.ByteArrayOutputStream;
import MyGenericServer.ConsoleServeur;
import java.security.NoSuchProviderException;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class TraitementPIDEP implements Traitement
{
    /********************************/
    /*           Variables          */
    /********************************/
    private String _codeProvider;
    private String _hash;
    private BDMouvements _bd;
    private ConsoleServeur _cs;
    private RServe _r;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public TraitementPIDEP()
    {
        Security.addProvider(new BouncyCastleProvider());
        MyProperties mp = new MyProperties("./Serveur_Analysis.conf");
        set_codeProvider(mp.getContent("PROVIDER"));
        set_hash(mp.getContent("HASH"));
    }

    public TraitementPIDEP(BDMouvements _bd)
    {
        this._bd = _bd;
        Security.addProvider(new BouncyCastleProvider());
        MyProperties mp = new MyProperties("./Serveur_Analysis.conf");
        set_codeProvider(mp.getContent("PROVIDER"));
        set_hash(mp.getContent("HASH"));
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

    public String get_codeProvider()
    {
        return _codeProvider;
    }

    public String get_hash()
    {
        return _hash;
    }

    private RServe getRServe()
    {
        return _r;
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

    public void set_codeProvider(String codeProvider)
    {
        this._codeProvider = codeProvider;
    }

    public void set_hash(String hash)
    {
        this._hash = hash;
    }

    private void setRServe(RServe r)
    {
        _r = r;
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
        else if(Requete instanceof DonneeGetStatDescrCont)
            return traiteGET_STAT_DESCR_CONT((DonneeGetStatDescrCont)Requete, client);
        else if(Requete instanceof DonneeGetGrCouleurRep)
            return traiteGET_GR_COULEUR_REP( (DonneeGetGrCouleurRep)Requete, client);
        else if(Requete instanceof DonneeGetGrCouleurComp)
            return traiteGET_GR_COULEUR_COMP( (DonneeGetGrCouleurComp)Requete, client);
        else if(Requete instanceof DonneeGetStatInferTestConf)
            return traiteGET_STAT_INFER_TEST_CONF( (DonneeGetStatInferTestConf)Requete, client);
        else if(Requete instanceof DonneeGetStatInferTestHomog)
            return traiteGET_STAT_INFER_TEST_HOMOG( (DonneeGetStatInferTestHomog)Requete, client);
        else if(Requete instanceof DonneeGetStatInferANOVA)
            return traiteGET_STAT_INFER_ANOVA( (DonneeGetStatInferANOVA)Requete, client);
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

    private Reponse traiteLOGIN(DonneeLogin chargeUtile, Client client)
    {
        String username = chargeUtile.getUsername();
        long temps = chargeUtile.getTemps();
        double alea = chargeUtile.getAlea();
        byte[] msgD = chargeUtile.getMsgD();

        if(client.is_loggedIn())
        {
            return new ReponsePIDEP(ReponsePIDEP.NOK, "Le client est deja connecte dans le serveur", null);
        }

        try
        {
            PreparedStatement ps = _bd.getPreparedStatement("SELECT userpassword FROM logins WHERE UPPER(username) = UPPER(?) ;");
            ps.setString(1, username);
            ResultSet rs = _bd.ExecuteQuery(ps);
            if(rs!=null && rs.next())
            {
                String bddpass = rs.getString("userpassword");

                // confection d'un digest local
                MessageDigest md = MessageDigest.getInstance(get_hash(), get_codeProvider());
                md.update(bddpass.getBytes());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream bdos = new DataOutputStream(baos);
                bdos.writeLong(temps);
                bdos.writeDouble(alea);
                md.update(baos.toByteArray());

                byte[] msgDLocal = md.digest();

                if(MessageDigest.isEqual(msgD, msgDLocal))
                {
                    client.set_loggedIn(true);
                    return new ReponsePIDEP(ReponsePIDEP.OK, null, null);
                }
                else
                {
                    return new ReponsePIDEP(ReponsePIDEP.NOK, "Mot de passe ou nom d'utilisateur erroné", null);
                }
            }
        }
        catch (SQLException | NoSuchAlgorithmException | NoSuchProviderException | IOException e)
        {
            e.printStackTrace();
        }

        return new ReponsePIDEP(ReponsePIDEP.NOK, "ERREUR lors du traitement de la requete", null);
    }

    private Reponse traiteGET_STAT_DESCR_CONT(DonneeGetStatDescrCont chargeUtile, Client client)
    {
        int tailEch = chargeUtile.get_tailleEch();
        String quand;

        if(chargeUtile.is_entree())
        {
            quand = "dateArrivee";
        }
        else
        {
            quand = "dateDepart";
        }

        PreparedStatement ps = null;
        try
        {
            ps = _bd.getPreparedStatement("SELECT poidsTotal \n" +
                    "FROM mouvements \n" +
                    "WHERE YEAR("+quand+") = YEAR(SYSDATE())\n" +
                    "ORDER BY RAND()\n" +
                    "LIMIT ?;");

            ps.setInt(1, tailEch);

            ResultSet rs = _bd.ExecuteQuery(ps);
            if(rs!=null && rs.next())
            {
                connectionRserve();

                Vector vec = new Vector();

                do
                {
                    vec.add(rs.getDouble("poidsTotal"));
                }while(rs.next());

                chargeUtile.set_moyenne(getRServe().getMoyenneVector(vec));
                chargeUtile.set_mediane(getRServe().getMedianeVector(vec));
                chargeUtile.set_ecartType(getRServe().getEcartTypeVector(vec));
                chargeUtile.set_mode(getRServe().getModeVector(vec));

                getRServe().RserveClose();

                return new ReponsePIDEP(ReponsePIDEP.OK,null, chargeUtile);
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return new ReponsePIDEP(ReponsePIDEP.NOK,"ERREUR lors du traitement de la requete", null);
    }

    private Reponse traiteGET_GR_COULEUR_REP(DonneeGetGrCouleurRep chargeUtile, Client client)
    {
        int donnee = chargeUtile.get_donnee();
        String requete;

        if(chargeUtile.is_annee())
        {
            requete = "SELECT destination, COUNT(*)as nombre\n" +
                    "FROM mouvements\n" +
                    "WHERE YEAR(dateArrivee) = ? OR YEAR(dateDepart) = ?\n" +
                    "GROUP BY destination;";
        }
        else
        {
            requete = "SELECT destination, COUNT(*) as nombre\n" +
                    "FROM mouvements\n" +
                    "WHERE (YEAR(dateArrivee) = YEAR(SYSDATE()) AND MONTH(dateArrivee) = ?)\n" +
                    "OR (YEAR(dateDepart) = YEAR(SYSDATE()) AND MONTH(dateDepart) = ?)\n" +
                    "GROUP BY destination;";
        }

        PreparedStatement ps = null;
        try
        {
            ps = _bd.getPreparedStatement(requete);
            ps.setInt(1, donnee);
            ps.setInt(2, donnee);

            ResultSet rs = _bd.ExecuteQuery(ps);
            if(rs!=null && rs.next())
            {
                Vector vec = new Vector();

                do
                {
                    GrCouleur cel = new GrCouleur();

                    cel.set_destination(rs.getString("destination"));
                    cel.set_nombre(rs.getInt("nombre"));

                    vec.add(cel);
                }while(rs.next());

                chargeUtile.set_retour(vec);

                return new ReponsePIDEP(ReponsePIDEP.OK,null, chargeUtile);
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return new ReponsePIDEP(ReponsePIDEP.NOK,"ERREUR lors du traitement de la requete", null);
    }

    private Reponse traiteGET_GR_COULEUR_COMP(DonneeGetGrCouleurComp chargeUtile, Client client)
    {
        int annee = chargeUtile.get_annee();
        String debut;
        String fin;

        try
        {
            debut = annee + "-01-01";
            fin = annee + "-03-31";
            chargeUtile.setTrim1(getTrimestre(debut, fin));

            debut = annee + "-04-01";
            fin = annee + "-06-30";
            chargeUtile.setTrim2(getTrimestre(debut, fin));

            debut = annee + "-07-01";
            fin = annee + "-09-30";
            chargeUtile.setTrim3(getTrimestre(debut, fin));

            debut = annee + "-10-01";
            fin = annee + "-12-31";
            chargeUtile.setTrim4(getTrimestre(debut, fin));

            return new ReponsePIDEP(ReponsePIDEP.OK,null, chargeUtile);
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return new ReponsePIDEP(ReponsePIDEP.NOK,"ERREUR lors du traitement de la requete", null);
    }

    private Reponse traiteGET_STAT_INFER_TEST_CONF(DonneeGetStatInferTestConf chargeUtile, Client client)
    {
        int tailEch = chargeUtile.get_taillEch();

        PreparedStatement ps = null;
        try
        {
            ps = _bd.getPreparedStatement("SELECT DATEDIFF(dateDepart, dateArrivee) as difference \n" +
                    "FROM mouvements \n" +
                    "WHERE dateArrivee IS NOT NULL \n" +
                    "AND dateDepart IS NOT NULL \n" +
                    "ORDER BY RAND()\n" +
                    "LIMIT ?;");

            ps.setInt(1, tailEch);

            ResultSet rs = _bd.ExecuteQuery(ps);
            if(rs!=null && rs.next())
            {

                Vector vec = new Vector();

                do
                {
                    vec.add(rs.getInt("difference"));
                }while(rs.next());

                if(vec.size() < tailEch || vec.size() < 3)
                {
                    return new ReponsePIDEP(ReponsePIDEP.NOK,"ERREUR trop peu d'éléments", null);
                }

                connectionRserve();

                chargeUtile.setP_value(getRServe().getTestConfVector(vec));

                getRServe().RserveClose();

                return new ReponsePIDEP(ReponsePIDEP.OK,null, chargeUtile);
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

        return new ReponsePIDEP(ReponsePIDEP.NOK,"ERREUR lors du traitement de la requete", null);
    }

    private Reponse traiteGET_STAT_INFER_TEST_HOMOG(DonneeGetStatInferTestHomog chargeUtile, Client client)
    {
        int tailEch = chargeUtile.get_tailleEch();

        try
        {
            Vector ech1 = null, ech2 = null;

            ech1 = getEchHomog(tailEch, "Strasbourg");

            if(ech1 == null || ech1.size() < tailEch || ech1.size() < 3)
            {
                return new ReponsePIDEP(ReponsePIDEP.NOK,"ERREUR trop peu d'éléments", null);
            }

            ech2 = getEchHomog(tailEch, "Duisbourg");

            if(ech2 == null || ech2.size() < tailEch || ech2.size() < 3)
            {
                return new ReponsePIDEP(ReponsePIDEP.NOK,"ERREUR trop peu d'éléments", null);
            }

            connectionRserve();

            getRServe().getTestHomogVector(ech1, ech2, chargeUtile);

            getRServe().RserveClose();

            return new ReponsePIDEP(ReponsePIDEP.OK,null, chargeUtile);
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return new ReponsePIDEP(ReponsePIDEP.NOK,"ERREUR lors du traitement de la requete", null);
    }

    private Reponse traiteGET_STAT_INFER_ANOVA(DonneeGetStatInferANOVA chargeUtile, Client client)
    {
        return new ReponsePIDEP(ReponsePIDEP.NOK,"ERREUR lors du traitement de la requete", null);
    }

    private Reponse traite404()
    {
        System.out.println("traite404 Request not found");
        return new ReponsePIDEP(ReponsePIDEP.REQUEST_NOT_FOUND, "request could not be exeuted due to unsopported version.", null);
    }

    private void connectionRserve()
    {
        MyProperties mp = new MyProperties("./Serveur_Analysis.conf");

        setRServe(new RServe());
        getRServe().connectionRserve(mp.getContent("RSERVE"));
    }

    private Vector getTrimestre(String debut, String fin) throws SQLException
    {
        PreparedStatement ps = null;

        ps = _bd.getPreparedStatement("SELECT destination, COUNT(*) as nombre \n" +
                "FROM mouvements \n" +
                "WHERE (dateArrivee BETWEEN ? AND ? ) \n" +
                "OR (dateDepart BETWEEN ? AND ? ) \n" +
                "GROUP BY destination;");
        ps.setString(1, debut);
        ps.setString(2, fin);
        ps.setString(3, debut);
        ps.setString(4, fin);

        ResultSet rs = _bd.ExecuteQuery(ps);

        if(rs!=null && rs.next())
        {
            Vector vec = new Vector();

            do
            {
                GrCouleur cel = new GrCouleur();

                cel.set_destination(rs.getString("destination"));
                cel.set_nombre(rs.getInt("nombre"));

                vec.add(cel);
            } while (rs.next());

            return vec;
        }
        return null;
    }

    private Vector getEchHomog(int taille, String ville) throws SQLException
    {
        PreparedStatement ps = null;

        ps = _bd.getPreparedStatement("SELECT DATEDIFF(dateDepart, dateArrivee) as difference\n" +
                                                "FROM mouvements\n" +
                                                "WHERE dateArrivee IS NOT NULL\n" +
                                                "AND dateDepart IS NOT NULL\n" +
                                                "AND UPPER(destination) = UPPER(?) \n" +
                                                "ORDER BY RAND()\n" +
                                                "LIMIT ?;");

        ps.setString(1, ville);
        ps.setInt(2, taille);

        ResultSet rs = _bd.ExecuteQuery(ps);
        if(rs!=null && rs.next())
        {

            Vector vec = new Vector();

            do {
                vec.add(rs.getInt("difference"));
            } while (rs.next());

            return vec;
        }
        return null;
    }
}
