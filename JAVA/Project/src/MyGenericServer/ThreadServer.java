/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package MyGenericServer;

import lib.BeanDBAcces.DataSource;

import java.beans.Beans;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ThreadServer extends Thread
{
    /********************************/
    /*           Variables          */
    /********************************/
    private int _port;
    private SourceTaches _sourceTaches;
    private DataSource _dataSource;
    private ConsoleServeur _console; //todo: en faire une liste pour afficher dans le gui et dns un fichier en meme temps ?
    private ServerSocket SSocket = null;
    private int _nbMaxConnections = 3;
    private String _clientType;
    private String protocol;

    private LinkedList<ThreadClient> listeThreadsEnfants;

    //todo: Ajouts d'evenements pour le plantage


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ThreadServer(int p, SourceTaches st, ConsoleServeur cs, boolean connecte, String protocol, DataSource ds)
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
        this._dataSource = ds;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    public void run()
    {
        try
        {
            SSocket = new ServerSocket(_port);
            this.AfficheServeur("Demarrage du serveur sur le port: "+_port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

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
                o.setTraitement(protocol, this._dataSource);
                o.set_console(this._console);
                o.set_dataSource(this._dataSource);
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
                this.AfficheServeur("Réception d'un client: " + CSocket.getInetAddress());
                _sourceTaches.addTache(CSocket);
            }
            catch (IOException e)
            {
                //e.printStackTrace();
                //this.AfficheServeur("> test");
                System.out.println(isInterrupted());
                for(ThreadClient tc : listeThreadsEnfants)
                {
                    tc.interrupt();
                }
                //break;
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
