/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package MyGenericServer;

import genericRequest.Reponse;
import genericRequest.Requete;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ThreadClientConnecte extends ThreadClient
{
    /********************************/
    /*           Variables          */
    /********************************/
    private Socket tacheEnCours;

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

    /********************************/
    /*            Setters           */
    /********************************/


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void run()
    {
        System.out.println("Demarrage du Thread: " + index);
        boolean inCommunication = false;
        ObjectInputStream ois=null;
        ObjectOutputStream oos=null;
        Requete req = null;

        while(!isInterrupted())
        {
            try {
                tacheEnCours = _taches.getTache();
                _client = new Client(); //nouveau client par connection
                inCommunication = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                inCommunication = false;
            }

            while(!isInterrupted() && inCommunication)
            {
                try
                {
                    ois = new ObjectInputStream(tacheEnCours.getInputStream());
                    oos = new ObjectOutputStream(tacheEnCours.getOutputStream());

                    req = (Requete)ois.readObject();
                    System.out.println("Requete lue par le serveur, instance de " +
                            req.getClass().getName() + " et " + req.getChargeUtile().getClass().getName());

                    Reponse rp = _traitement.traiteRequete(req.getChargeUtile(), _client);
                    oos.writeObject(rp);
                    oos.flush();
                }
                catch (ClassNotFoundException e)
                {
                    System.err.println("Erreur de def de classe [" + e.getMessage() + "]");
                } catch (IOException e) {
                    if(e instanceof EOFException)
                    {
                        inCommunication = false;
                        System.out.println("Le client s'est deconnecte");
                    }
                    else
                    {
                        inCommunication = false;
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
