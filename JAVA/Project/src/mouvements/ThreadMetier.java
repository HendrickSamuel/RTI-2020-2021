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
        System.out.println("Thread n°" + _numero + " démarré \n");
        while(j < 5)
        {
            try {
                Socket z = _tache.prendreSocket();
                System.out.println("Thread n°" + _numero + " s'occupe de "+ z +" \n");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            j++;
        }
        return;
    }

}
