package MyGenericServer;

import genericRequest.Reponse;
import genericRequest.Requete;
import genericRequest.Traitement;
import MyGenericServer.SourceTaches;

import java.beans.Beans;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ThreadClientConnecte extends Thread implements ThreadClient {

    private SourceTaches _taches;
    private String nom;
    private Socket tacheEnCours;
    private Traitement traitement;

    public ThreadClientConnecte()
    {
        System.out.println("DEMARRAGE ThreadClientConnecte");
    }

    public void set_taches(SourceTaches _taches) {
        this._taches = _taches;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public void setTraitement(String nom) throws IOException, ClassNotFoundException {
        traitement = (Traitement)Beans.instantiate(null, nom);
    }

    public void setTacheEnCours(Socket tacheEnCours) {
        this.tacheEnCours = tacheEnCours;
    }

    @Override
    public void run() {

        System.out.println("Je suis :" + nom + " et je demarre en tant que Thread");
        boolean inCommunication = false;
        while(!isInterrupted())
        {
            try {
                tacheEnCours = _taches.getTache();
                inCommunication = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ObjectInputStream ois=null;
            ObjectOutputStream oos=null;
            Requete req = null;
            try {
                ois = new ObjectInputStream(tacheEnCours.getInputStream());
                oos = new ObjectOutputStream(tacheEnCours.getOutputStream());
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

                    Reponse rp = traitement.traiteRequete(req.getChargeUtile());
                    oos.writeObject(rp);
                    oos.flush();
                }
                catch (ClassNotFoundException e)
                {
                    System.err.println("Erreur de def de classe [" + e.getMessage() + "]");
                } catch (IOException e) {
                    inCommunication = false;
                    System.out.println("Le client s'est deconnecte");
                }
            }
        }
    }
}
