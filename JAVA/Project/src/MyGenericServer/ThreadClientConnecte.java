/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package MyGenericServer;

import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import genericRequest.Requete;
import protocol.IOBREP.DonneeBoatArrived;

import java.io.*;
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
        if(this.is_javaObjectsCommunication())
            runJavaObject();
        else
            runByteStream();

    }

    public void runJavaObject()
    {
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

    public void runByteStream()
    {
        DataInputStream dis = null;
        DataOutputStream dos = null;
        boolean inCommunication = false;

        while(!isInterrupted()) {
            try {
                tacheEnCours = _taches.getTache();
                _client = new Client(); //nouveau client par connection
                inCommunication = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                inCommunication = false;
            }

            while (!isInterrupted() && inCommunication) {
                try {
                    dis = new DataInputStream(tacheEnCours.getInputStream());
                    dos = new DataOutputStream(tacheEnCours.getOutputStream());

                    //req = (Requete) ois.readObject();
                    String message = readAllBytes(dis);
                    DonneeRequete req = parseString(message);
                    System.out.println("Requete lue par le serveur, instance de " + req.getClass().getName());

                    Reponse rp = _traitement.traiteRequete(req, _client);
                    dos.write(rp.toString().getBytes());
                    dos.flush();
                } catch (IOException e) {
                    if (e instanceof EOFException) {
                        inCommunication = false;
                        System.out.println("Le client s'est deconnecte");
                    } else {
                        inCommunication = false;
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String readAllBytes(DataInputStream dis)
    {
        StringBuffer message = new StringBuffer();
        byte b = 0;
        message.setLength(0);
        try
        {
            while((b = dis.readByte()) != (byte)'\n')
            {
                if(b != '\n')
                    message.append((char)b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return message.toString().trim();
    }

    public DonneeRequete parseString(String message)
    {
        //return new DonneeBoatArrived();
        // todo: stuff
        return null;
    }
}
