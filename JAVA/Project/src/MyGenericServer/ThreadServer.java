/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package MyGenericServer;

import genericRequest.Reponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadServer extends Thread
{
    /********************************/
    /*           Variables          */
    /********************************/
    private int _port;
    private SourceTaches _sourceTaches;
    private ConsoleServeur _console;
    private ServerSocket SSocket = null;
    private Reponse _errorResponse;
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
            this.AfficheServeur("Demarrage du serveur : "+ InetAddress.getLocalHost().getHostAddress() + ":" + _port);
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
                _sourceTaches.addTache(CSocket);
            }
            catch (IOException e)
            {
                System.out.println(isInterrupted());
            }
        }
        this.AfficheServeur("Le serveur se coupe");
    }

    private void AfficheServeur(String message)
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

    private void ReponseResourceOccupee(Socket sock, Reponse reponse)
    {
        try
        {
            if(this.is_javaObjectsCommunication())
                ReponseJavaObjects(sock, reponse);
            else
                ReponseBytes(sock, reponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ReponseJavaObjects(Socket sock, Reponse reponse) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
        oos.writeObject(reponse);
        oos.flush();
    }

    private void ReponseBytes(Socket sock, Reponse reponse) throws IOException {
        DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
        dos.write(reponse.toString().getBytes());
        dos.flush();
    }

}
