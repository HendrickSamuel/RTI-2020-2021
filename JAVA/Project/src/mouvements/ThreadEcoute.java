package mouvements;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadEcoute extends Thread{
    private int _port;
    private ServerSocket SSocket;
    private Tache_Mouvements _tache;

    public ThreadEcoute(int port ,Tache_Mouvements tache)
    {
        _port = port;
        _tache = tache;
    }

    public void run()
    {
        try
        {
            SSocket = new ServerSocket(_port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Socket CSocket = null;

        int j = 0;
        while(true) //todo: something psq moche
        {
            try
            {
                CSocket = SSocket.accept();
                boolean retour = _tache.deposerSocket(CSocket);
                if(!retour)
                {
                    //todo: dire au client qu'on a un problème de places libres.
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            j++;
        }
    }
}
