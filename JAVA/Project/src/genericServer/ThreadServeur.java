package genericServer;

import genericRequest.Requete;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadServeur extends Thread{
    private int _port;
    private SourceTaches _sourceTaches;
    private ConsoleServeur _console; //todo: en faire une liste pour afficher dans le gui et dns un fichier en meme temps ?
    private ServerSocket SSocket = null;
    private int _nbMaxConnections = 3;

    public ThreadServeur(int p, SourceTaches st, ConsoleServeur cs)
    {
        _port = p;
        _sourceTaches = st;
        _console = cs;
    }

    public void run()
    {
        try {
            SSocket = new ServerSocket(_port);
        } catch (IOException e) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }

            ObjectInputStream ois = null;
            Requete req = null;

            try
            {
                ois = new ObjectInputStream(CSocket.getInputStream());
                req = (Requete)ois.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Runnable travail = req.createRunnable(CSocket, _console);
            if(travail != null)
            {
                _sourceTaches.addTache(travail);
            }
        }
    }

}
