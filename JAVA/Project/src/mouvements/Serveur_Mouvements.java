package mouvements;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur_Mouvements extends Thread{
    private int _port;
    private ServerSocket SSocket;
    private int _nbClients = 3;
    private Tache_Mouvements _tache;

    public void run()
    {
        _tache = new Tache_Mouvements();
        try
        {
            SSocket = new ServerSocket(_port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < _nbClients; i++)
        {
            Serveur_Mouvements_Thread thr = new Serveur_Mouvements_Thread(i, _tache);
            thr.start();
        }

        Socket CSocket = null;
        int j = 0;

        while(true) //todo: something psq moche
        {
            try
            {
                //CSocket = SSocket.accept();
                sleep(500);
                _tache.donneSocket(j);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            j++;

        }
    }
}
