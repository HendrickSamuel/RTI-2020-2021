/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package MyGenericServer;

import java.io.IOException;
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

    //todo: Ajouts d'evenements pour le plantage

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ThreadServer(int p, SourceTaches st, ConsoleServeur cs)
    {
        this._port = p;
        this._sourceTaches = st;
        this._console = cs;
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
            e.printStackTrace();
            //todo: arreter
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

}
