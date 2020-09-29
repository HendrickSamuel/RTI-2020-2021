package mouvements;

import java.net.Socket;

public class Tache_Mouvements {

    private Socket _socket; //todo: changer en liste à taille maximum ?
    private boolean disponible;
    private int _i;

    public Tache_Mouvements()
    {
        _socket = null;
        disponible = false;
    }

    public synchronized void donneSocket(Socket sock) throws InterruptedException {
        while (disponible)
        {
            wait();
        }
        _socket = sock;
        disponible = true;
        notify();
    }

    public synchronized void donneSocket(int sock) throws InterruptedException {
        while (disponible)
        {
            wait();
        }
        _i = sock;
        //_socket = sock;
        disponible = true;
        notify();
    }

    /*
    public synchronized Socket getSocket() throws InterruptedException{
        while(!disponible)
        {
            wait();
        }
        disponible = false;
        notify();
        return _socket;
    }
     */

    public synchronized int getSocket() throws InterruptedException{
        while(!disponible)
        {
            wait();
        }
        disponible = false;
        notify();
        return _i;
    }
}
