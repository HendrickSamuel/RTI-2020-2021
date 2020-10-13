/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package MyGenericServer;

import genericRequest.Requete;
import MyGenericServer.ConsoleServeur;
import MyGenericServer.SourceTaches;

import java.beans.Beans;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ThreadServer extends Thread
{
    /********************************/
    /*           Variables          */
    /********************************/
    private int _port;
    private SourceTaches _sourceTaches;
    private ConsoleServeur _console; //todo: en faire une liste pour afficher dans le gui et dns un fichier en meme temps ?
    private ServerSocket SSocket = null;
    private int _nbMaxConnections = 3;
    private String _clientType;
    private String protocol;

    private LinkedList<ThreadClient> listeThreadsEnfants;
    //private ThreadGroup _threadGroup;

    //private volatile boolean manualStop = false;
    //todo: Ajouts d'evenements pour le plantage


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ThreadServer(int p, SourceTaches st, ConsoleServeur cs, boolean connecte, String protocol)
    {
        this._port = p;
        this._sourceTaches = st;
        this._console = cs;

        if(connecte)
            _clientType = "ThreadClientConnecte";
        else
            _clientType = "ThreadClientDeconnecte";

        this.protocol = protocol;

        this.listeThreadsEnfants = new LinkedList<>();
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    public void run()
    {
        try
        {
            SSocket = new ServerSocket(_port);
            _console.Affiche("Demarrage du serveur sur le port: "+_port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //_threadGroup = new ThreadGroup("Threads"+_port);

        for(int i = 0; i < _nbMaxConnections; i++)
        {
            try
            {
                String truc = this.getClass().getPackage().getName()+"."+_clientType;
                System.out.println(truc);
                ThreadClient o = (ThreadClient)Beans.instantiate(null, truc);
                listeThreadsEnfants.add(o);

                o.set_taches(_sourceTaches);
                o.setNom("Thread n° " + i);
                o.setTraitement(protocol);
                o.start();

            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }

        Socket CSocket = null;
        while(!isInterrupted())
        {
            try
            {
                CSocket = SSocket.accept();
                _console.Affiche("Réception d'un client: " + CSocket.getInetAddress());
                _sourceTaches.addTache(CSocket);
            }
            catch (IOException e)
            {
                //e.printStackTrace();
                //_console.Affiche("> test");
                System.out.println(isInterrupted());
                for(ThreadClient tc : listeThreadsEnfants)
                {
                    tc.interrupt();
                }
                //break;
            }
        }

        _console.Affiche("Le serveur se coupe");
    }

    public void StopServeur()
    {
        try {
            this.interrupt();
            SSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
