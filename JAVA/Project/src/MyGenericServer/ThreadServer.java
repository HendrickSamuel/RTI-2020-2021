/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package MyGenericServer;

import genericRequest.Reponse;

import java.io.*;
import java.net.*;

public class ThreadServer extends Thread
{
    /********************************/
    /*           Variables          */
    /********************************/
    protected int _port;
    protected SourceTaches _sourceTaches;
    protected ConsoleServeur _console;
    private ServerSocket SSocket = null;
    protected Reponse _errorResponse;
    protected boolean _javaObjectsCommunication = true;

    //todo: Ajouts d'evenements pour le plantage

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ThreadServer(int p, SourceTaches st, ConsoleServeur cs, Reponse errorResponse)
    {
        this._port = p;
        this._sourceTaches = st;
        this._console = cs;
        this._errorResponse = errorResponse;
    }

    /********************************/
    /*         Getters              */
    /********************************/

    public boolean is_javaObjectsCommunication() {
        return _javaObjectsCommunication;
    }

    /********************************/
    /*         Setters              */
    /********************************/

    public void set_javaObjectsCommunication(boolean _javaObjectsCommunication) {
        this._javaObjectsCommunication = _javaObjectsCommunication;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    public void run()
    {
        try
        {
            SSocket = new ServerSocket(_port);
            this.AfficheServeur("Demarrage du serveur sur l'adresse locale: "+ InetAddress.getLocalHost().getHostAddress() + ":" + _port);
            this.AfficheServeur("Demarrage du serveur sur l'adresse publique : "+ this.getPublicIp() + ":" + _port);

        }
        catch (IOException e)
        {
            this.AfficheServeur("Demarrage du serveur impossible : "+ e.getMessage());
            this.interrupt();
        }

        Socket CSocket = null;
        while(!isInterrupted())
        {
            try
            {
                CSocket = SSocket.accept();
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
        this.AfficheServeur("Le serveur se coupe");
    }

    public void CloseSocketAccept()
    {
        try {
            SSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void AfficheServeur(String message)
    {
        if(_console != null)
        {
            _console.Affiche(message);
        }
        else
        {
            System.err.println("-- Le serveur n'a pas de console dédiée pour ce message -- " + message);
        }
    }

    protected void ReponseResourceOccupee(Socket sock, Reponse reponse)
    {
        AfficheServeur("Un client tente de se connecter sans ressources disponibles");
        try
        {
            if(this.is_javaObjectsCommunication())
                ReponseJavaObjects(sock, reponse);
            else
                ReponseBytes(sock, reponse);
        } catch (IOException e) {
            AfficheServeur("[Erreur] - "+e.getLocalizedMessage());
        }
    }

    protected void ReponseJavaObjects(Socket sock, Reponse reponse) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
        oos.writeObject(reponse);
        oos.flush();
    }

    protected void ReponseBytes(Socket sock, Reponse reponse) throws IOException {
        DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
        dos.write(reponse.toString().getBytes());
        dos.flush();
    }

    protected String getPublicIp()
    {
        String systemipaddress;
        try
        {
            URL url_name = new URL("http://bot.whatismyipaddress.com");

            BufferedReader sc =
                    new BufferedReader(new InputStreamReader(url_name.openStream()));
            systemipaddress = sc.readLine().trim();
        }
        catch (Exception e)
        {
            systemipaddress = "Could not find Public IP";
        }
        return systemipaddress;
    }

}
