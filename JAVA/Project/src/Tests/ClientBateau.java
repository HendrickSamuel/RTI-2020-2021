//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 24/10/2020

package Tests;

import genericRequest.DonneeRequete;
import protocol.IOBREP.Container;
import protocol.IOBREP.DonneeGetContainers;
import protocol.IOBREP.ReponseIOBREP;
import protocol.IOBREP.RequeteIOBREP;
import protocol.TRAMAP.*;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class ClientBateau {

    public static void main(String[] args)
    {
        ObjectInputStream ois;
        ObjectOutputStream oos;
        Socket cliSock;

        DonneeRequete dt = new protocol.IOBREP.DonneeGetContainers("Paris","RAND", "IN");


        RequeteIOBREP req = null;
        req = new RequeteIOBREP(dt);

        // Connexion au serveur
        ois=null; oos=null; cliSock = null;
        try
        {
            cliSock = new Socket("169.254.60.97", 5000);
            System.out.println(cliSock.getInetAddress().toString());
        }
        catch (UnknownHostException e)
        {
            System.err.println("Erreur ! Host non trouvé [" + e + "]");
        }
        catch (IOException e)
        {
            System.err.println("Erreur ! Pas de connexion ? [" + e + "]");
        }
        // Envoie de la requête
        try
        {
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req); oos.flush();
        }
        catch (IOException e)
        {
            System.err.println("Erreur réseau ? [" + e.getMessage() + "]");
        }
        // Lecture de la réponse
        ReponseIOBREP rep = null;
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseIOBREP)ois.readObject();
            System.out.println(" *** Reponse reçue : " + rep.getCode());
            DonneeGetContainers charge = (DonneeGetContainers)rep.get_chargeUtile();
            for(Container cont : charge.get_containers())
            {
                System.out.println(cont.getX() + " - " + cont.getY() + " : " + cont.getId());
            }
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("--- erreur sur la classe = " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("--- erreur IO = " + e.getMessage());
        }
    }

}
