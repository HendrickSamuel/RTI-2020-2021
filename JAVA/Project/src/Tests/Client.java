/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package Tests;

import genericRequest.DonneeRequete;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import protocol.BISAMAP.*;
import protocol.BISAMAP.DonneeLogin;
import protocol.TRAMAP.*;
import security.SecurityHelper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Date;

public class Client
{
    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException, IOException, CertificateException, KeyStoreException {
        Security.addProvider(new BouncyCastleProvider());
        SecurityHelper securityHelper = new SecurityHelper();
        securityHelper.initKeyStore("./Confs/ClientKeyVault","password");

        SecretKey secretKey = null;

        ObjectInputStream ois;
        ObjectOutputStream oos;
        Socket cliSock;

        MessageDigest md = MessageDigest.getInstance("SHA-256", "BC");

        //Création du message digest salé
        md.update("sam".getBytes());

        //Création du sel
        long temps = (new Date()).getTime();
        double alea = Math.random();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream bdos = new DataOutputStream(baos);
        bdos.writeLong(temps);
        bdos.writeDouble(alea);

        //Ajout du sel
        md.update(baos.toByteArray());


        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        SecurityHelper sc = new SecurityHelper();

        DonneeLogin dl = new DonneeLogin();
        dl.set_nom("Samuel");
        dl.set_pwdDigest(md.digest());
        dl.set_aleatoire(alea);
        dl.set_temps(temps);

        RequeteBISAMAP req = new RequeteBISAMAP(dl);


        // Connexion au serveur
        ois=null; oos=null; cliSock = null;
        try
        {
            cliSock = new Socket("192.168.1.197", 5001);
            System.out.println(cliSock.getInetAddress().toString());
        }
        catch (UnknownHostException e)
        {
            System.err.println("Erreur ! Host non trouvé [" + e + "]");
        }
        catch (IOException e)
        {
            System.err.println("Erreur ! Pas de connexion ? [" + e + "]");
        }
        // Envoie de la requête
        try
        {
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req); oos.flush();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
        // Lecture de la réponse
        ReponseBISAMAP rep = null;
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseBISAMAP)ois.readObject();
            System.out.println(" *** Reponse reçue : " + rep.getCode());
            if(rep.getMessage() != null)
            {
                System.out.println("Message reçu: " + rep.getMessage());
                return;
            }
            else
            {
                DonneeLogin donneel = (DonneeLogin)rep.getChargeUtile();
                secretKey = securityHelper.decipherSecretKey(donneel.get_sessionKey(), "ClientKeyVault","password");
                if(secretKey == null)
                    System.out.println("PROBLEME");
                else
                    System.out.println("OK");
            }
            reader.readLine();
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("--- erreur sur la classe = " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        DonneeGetNextBill dgnb = new DonneeGetNextBill();
        RequeteBISAMAP reqbis = new RequeteBISAMAP(dgnb);

        try
        {
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(reqbis); oos.flush();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
        // Lecture de la réponse
        ReponseBISAMAP rep2 = null;
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep2 = (ReponseBISAMAP)ois.readObject();
            System.out.println(" *** Reponse reçue : " + rep2.getCode());
            if(rep.getMessage() != null)
            {
                System.out.println("Message reçu: " + rep2.getMessage());
            }
            else
            {
                DonneeGetNextBill dd =  (DonneeGetNextBill)rep2.getChargeUtile();
                if(dd == null)
                    System.out.println("AUTRE PROBLEME");

                Facture facture = (Facture)securityHelper.decipherObject(dd.getFactureCryptee(), secretKey);
                System.out.println("ok");
            }
            reader.readLine();
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("--- erreur sur la classe = " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
