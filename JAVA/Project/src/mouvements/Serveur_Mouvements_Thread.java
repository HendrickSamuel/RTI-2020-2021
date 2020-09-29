package mouvements;

public class Serveur_Mouvements_Thread extends Thread
{
    private int _numero;
    private Tache_Mouvements _tache;

    public Serveur_Mouvements_Thread(int i, Tache_Mouvements tache)
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
                int z = _tache.getSocket();
                System.out.println("Thread n°" + _numero + " s'occupe de "+z+" \n");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            j++;
        }
        return;
    }

}
