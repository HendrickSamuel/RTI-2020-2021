/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package genericServer;

import genericRequest.Requete;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadServeur extends Thread
{
    /********************************/
    /*           Variables          */
    /********************************/
    private int _port;
    private SourceTaches _sourceTaches;
    private ConsoleServeur _console; //todo: en faire une liste pour afficher dans le gui et dns un fichier en meme temps ?
    private ServerSocket SSocket = null;
    private int _nbMaxConnections = 3;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ThreadServeur(int p, SourceTaches st, ConsoleServeur cs)
    {
        _port = p;
        _sourceTaches = st;
        _console = cs;
    }


    /********************************/
    /*          Méthodes            */
    /********************************/
    public void run()
    {
        try
        {
            SSocket = new ServerSocket(_port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        for(int i = 0; i < _nbMaxConnections; i++)
        {
            ThreadClient thr = new ThreadClient(_sourceTaches, "Thread n° " + i);
            thr.start();
        }

        Socket CSocket = null;
        while(!isInterrupted())
        {
            try
            {
                CSocket = SSocket.accept();
                //_console.printLine(""); //todo: ajouter quelque chose
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            _sourceTaches.addTache(CSocket);
        }
    }
}
