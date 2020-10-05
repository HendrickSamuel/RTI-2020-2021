package genericServer;

import genericRequest.Requete;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ThreadClient extends Thread
{
    private SourceTaches _taches;
    private String nom;
    private Socket tacheEnCours;

    public ThreadClient(SourceTaches st, String n)
    {
        _taches = st;
        nom = n;

    }

    public void run() {
        boolean inCommunication = false;
        System.out.println("<Start> " + nom);
        while(!isInterrupted())
        {
            try {
                tacheEnCours = _taches.getTache();
                inCommunication = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ObjectInputStream ois=null;
            Requete req = null;
            try {
                ois = new ObjectInputStream(tacheEnCours.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }


            while(!isInterrupted() && inCommunication)
            {
                try
                {
                    req = (Requete)ois.readObject();
                    System.out.println("Requete lue par le serveur, instance de " +
                            req.getClass().getName());
                }
                catch (ClassNotFoundException e)
                {
                    System.err.println("Erreur de def de classe [" + e.getMessage() + "]");
                } catch (IOException e) {
                    inCommunication = false;
                    System.out.println("Le client s'est deconnecte");
                }

                Runnable travail = req.createRunnable(tacheEnCours, null); //todo: ajouter une console
                if(travail != null)
                {
                    travail.run();
                }
            }
        }
    }
}


