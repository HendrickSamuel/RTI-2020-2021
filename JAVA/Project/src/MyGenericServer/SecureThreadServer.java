//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/12/2020

package MyGenericServer;

import genericRequest.Reponse;
import javax.net.ssl.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;

public class SecureThreadServer extends ThreadServer
{
    /********************************/
    /*           Variables          */
    /********************************/
    private String fichierKeystore = "./Confs/ComptaKeyVault";
    private String mdpKeystore = "password";

    private SSLServerSocket SSLSocket = null;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public SecureThreadServer(int p, SourceTaches st, ConsoleServeur cs, Reponse errorResponse)
    {
        super(p ,st , cs, errorResponse);
    }


    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void run()
    {
        try
        {
            //Keystore
            KeyStore ServerKs = KeyStore.getInstance("JKS");
            ServerKs.load(new FileInputStream(fichierKeystore), mdpKeystore.toCharArray());

            //Contexte
            SSLContext SslC = SSLContext.getInstance("SSLv3");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ServerKs, mdpKeystore.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ServerKs);
            SslC.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            //Factory
            SSLServerSocketFactory SslSFac= SslC.getServerSocketFactory();

            //SSLSocket
            SSLSocket = (SSLServerSocket) SslSFac.createServerSocket(_port);

            this.AfficheServeur("Demarrage du secure serveur sur l'adresse locale: "+ InetAddress.getLocalHost().getHostAddress() + ":" + _port);
            this.AfficheServeur("Demarrage du secure serveur sur l'adresse publique : "+ this.getPublicIp() + ":" + _port);

        }
        catch (IOException e)
        {
            this.AfficheServeur("Demarrage du secure serveur impossible : "+ e.getMessage());
            this.interrupt();
        }
        catch (KeyStoreException e)
        {
            this.AfficheServeur("Erreur de KeyStore : "+ e.getMessage());
            this.interrupt();
        }
        catch (NoSuchAlgorithmException e)
        {
            this.AfficheServeur("Erreur d'algorithme au chargement du KeyStore : "+ e.getMessage());
            this.interrupt();
        }
        catch (CertificateException e)
        {
            this.AfficheServeur("Erreur de certificat au chargement du KeyStore : "+ e.getMessage());
            this.interrupt();
        }
        catch (KeyManagementException e)
        {
            this.AfficheServeur("Erreur pour accéder aux Key managers : "+ e.getMessage());
            this.interrupt();
        }
        catch (UnrecoverableKeyException e)
        {
            this.AfficheServeur("Erreur d'initialisation du KeyManagerFactory : "+ e.getMessage());
            this.interrupt();
        }

        SSLSocket CSocket = null;
        while(!isInterrupted())
        {
            try
            {
                CSocket = (SSLSocket)SSLSocket.accept();
                this.AfficheServeur("Réception d'un client: " + CSocket.getInetAddress());
                if(!_sourceTaches.addTache(CSocket))
                    ReponseResourceOccupee(CSocket, this._errorResponse);
            }
            catch (IOException e)
            {
                if(this.isInterrupted())
                    this.interrupt();
            }
        }
        this.AfficheServeur("Le secure serveur se coupe");
    }

    @Override
    public void CloseSocketAccept()
    {
        try
        {
            SSLSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}