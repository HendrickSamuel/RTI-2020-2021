package mouvements;

import java.net.Socket;

public class ThreadMetier extends Thread
{
    private int _numero;
    private Tache_Mouvements _tache;

    public ThreadMetier(int i, Tache_Mouvements tache)
    {
        _numero = i;
        _tache = tache;
    }

    public void run()
    {
        int j = 0;
        System.out.println("Thread n°" + _numero + " démarré");
        while(j < 5)
        {
            try {
                Socket z = _tache.prendreSocket();
                //todo: traiter la demande du client et tout
                System.out.println("Thread n° " + _numero + "s'occupe d'une commande");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            j++;
        }
        return;
    }

}
