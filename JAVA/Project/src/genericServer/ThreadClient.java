package genericServer;

public class ThreadClient extends Thread
{
    private SourceTaches _taches;
    private String nom;
    private Runnable tacheEnCours;

    public ThreadClient(SourceTaches st, String n)
    {
        _taches = st;
        nom = n;

    }

    public void run()
    {
        System.out.println("<Start> " + nom);
        while(!isInterrupted())
        {
            try {
                tacheEnCours = _taches.getTache();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            tacheEnCours.run();
        }
    }
}
