package mouvements;

import java.net.Socket;

public class Tache_Mouvements {

    private Socket _socket;
    private boolean disponible;
    private int nBThreadsLibres = 0;

    public Tache_Mouvements()
    {
        _socket = null;
        disponible = false;
    }

    public synchronized boolean deposerSocket(Socket sock) throws InterruptedException {
        //while (disponible)wait(); retiré pour que le serveur n'attende pas. si pas de place alors on s'en va
        if(!aThreadsDisponibles())
            return false;

        _socket = sock;
        disponible = true;
        notify();
        return true;
    }

    public synchronized Socket prendreSocket() throws InterruptedException{
        libereThread(); // je précise que mon thread est libre avant de me mettre en attente
        System.out.println("il y a " + nBThreadsLibres + " Threads libres en attente");
        while(!disponible)
        {
            wait();
        }
        disponible = false;
        occupeThread(); // je me retire du nbr de threads disponibles
        notify();
        return _socket;
    }

    public synchronized void libereThread()
    {
        nBThreadsLibres++;
    }

    public synchronized void occupeThread()
    {
        nBThreadsLibres--;
    }

    public synchronized boolean aThreadsDisponibles()
    {
        return (nBThreadsLibres > 0);
    }

}
