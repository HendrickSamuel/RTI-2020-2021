/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package MyGenericServer;

import genericRequest.Reponse;
import genericRequest.Requete;
import genericRequest.Traitement;

import java.beans.Beans;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ThreadClientConnecte extends ThreadClient
{
    /********************************/
    /*           Variables          */
    /********************************/
    private SourceTaches _taches;
    private String nom;
    private Socket tacheEnCours;
    private Traitement traitement;

    private Client _client;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ThreadClientConnecte()
    {
        System.out.println("DEMARRAGE ThreadClientConnecte");
        _client = new Client();
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public String getNom()
    {
        return nom;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_taches(SourceTaches _taches)
    {
        this._taches = _taches;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    @Override
    public void setTraitement(String nom) throws IOException, ClassNotFoundException
    {
        traitement = (Traitement)Beans.instantiate(null, nom);
        traitement.setConsole(this._console);
    }

    public void setTacheEnCours(Socket tacheEnCours)
    {
        this.tacheEnCours = tacheEnCours;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void run()
    {
        System.out.println("Je suis :" + nom + " et je demarre en tant que Thread");
        boolean inCommunication = false;
        while(!isInterrupted())
        {
            ObjectInputStream ois=null;
            ObjectOutputStream oos=null;
            Requete req = null;

            try
            {
                tacheEnCours = _taches.getTache();
                _client = new Client(); //nouveau client par connection
                inCommunication = true;

                try
                {
                    ois = new ObjectInputStream(tacheEnCours.getInputStream());
                    oos = new ObjectOutputStream(tacheEnCours.getOutputStream());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            catch (InterruptedException e)
            {
                System.out.println(nom + " a quitte le truc");
                inCommunication = false;
            }

            while(!isInterrupted() && inCommunication)
            {
                try
                {
                    req = (Requete)ois.readObject();
                    System.out.println("Requete lue par le serveur, instance de " +
                            req.getClass().getName());

                    Reponse rp = traitement.traiteRequete(req.getChargeUtile(), _client);
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
