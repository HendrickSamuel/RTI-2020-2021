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
import protocol.PLAMAP.DonneeLoginCont;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ThreadClientConnecte extends ThreadClient
{
    /********************************/
    /*           Variables          */
    /********************************/
    private Socket tacheEnCours;

    protected Client _client;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ThreadClientConnecte()
    {

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
        this.AfficheServeur("Demarrage du Thread: " + index);
        try {
            if(this.is_javaObjectsCommunication())
                runJavaObject();
            else
                runByteStream();
        } catch (InterruptedException e) {
            this.AfficheServeur("Arret du Thread: " + index);
        }
    }

    public void runJavaObject() throws InterruptedException {
        boolean inCommunication = false;
        ObjectInputStream ois=null;
        ObjectOutputStream oos=null;
        Requete req = null;

        while(!isInterrupted())
        {
            tacheEnCours = _taches.getTache();
            _client = new Client(); //nouveau client par connection
            inCommunication = true;

            while(!isInterrupted() && inCommunication)
            {
                try
                {
                    ois = new ObjectInputStream(tacheEnCours.getInputStream());
                    oos = new ObjectOutputStream(tacheEnCours.getOutputStream());

                    req = (Requete)ois.readObject();
                    this.AfficheServeur("Requete lue par le serveur, instance de " +
                            req.getClass().getName() + " et " + req.getChargeUtile().getClass().getName());

                    Reponse rp = _traitement.traiteRequete(req.getChargeUtile(), _client);
                    this.AfficheServeur("Reponse ==> " + rp);
                    oos.writeObject(rp);
                    oos.flush();

                    //sleep(5000);

                }
                catch (ClassNotFoundException e)
                {
                    this.AfficheServeur("Erreur de def de classe [" + e.getMessage() + "]");
                }
                catch (SocketException e)
                {
                    inCommunication = false;
                    this.AfficheServeur("Le client s'est deconnecte");
                }
                catch (IOException e)
                {
                    inCommunication = false;
                    if(e instanceof EOFException)
                    {
                        this.AfficheServeur("Le client s'est deconnecte");
                    }
                    else
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void runByteStream() throws InterruptedException {
        DataInputStream dis = null;
        DataOutputStream dos = null;
        boolean inCommunication = false;

        while(!isInterrupted())
        {
            tacheEnCours = _taches.getTache();
            _client = new Client(); //nouveau client par connection
            inCommunication = true;

            while (!isInterrupted() && inCommunication)
            {
                try
                {
                    dis = new DataInputStream(tacheEnCours.getInputStream());
                    dos = new DataOutputStream(tacheEnCours.getOutputStream());

                    String message = readAllBytes(dis);
                    this.AfficheServeur("Recu ==> " + message);
                    System.out.println("Recu ==> [" + message + "]");
                    DonneeRequete req = parseString(message);
                    this.AfficheServeur("Requete lue par le serveur, instance de " + req.getClass().getName());

                    Reponse rp = _traitement.traiteRequete(req, _client);
                    this.AfficheServeur("Reponse ==> " + rp);
                    dos.write(rp.toString().getBytes());
                    dos.flush();
                }
                catch (IOException e)
                {
                    if (e instanceof EOFException)
                    {
                        inCommunication = false;
                        this.AfficheServeur("Le client s'est deconnecte");
                    }
                    else
                        {
                        inCommunication = false;
                        this.AfficheServeur("[Erreur] - " + e.getLocalizedMessage());
                    }
                }
            }
        }
    }

    public String readAllBytes(DataInputStream dis) throws IOException {
        StringBuffer message = new StringBuffer();
        byte b = 0;
        message.setLength(0);
        while((b = dis.readByte()) != (byte)'\n')
        {
            if(b != '\n')
                message.append((char)b);
        }
        return message.toString().trim();

    }

    public DonneeRequete parseString(String message)
    {
        String[] parametres = message.split("#");
        String[] row;
        System.out.println("Objet reçu: " + parametres[0]);
        try {
            DonneeRequete dr = (DonneeRequete)Class.forName(parametres[0]).newInstance();
            dr.setFiledsFromString(message);
            return dr;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}