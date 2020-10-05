package Tests;

import genericRequest.Requete;
import protocolTRAMAP.DonneeLogin;
import protocolTRAMAP.EnumTRAMAP;
import protocolTRAMAP.ReponseTRAMAP;
import protocolTRAMAP.RequeteTRAMAP;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

public class Client {
    public static void main(String[] args)
    {
        ObjectInputStream ois;
        ObjectOutputStream oos;
        Socket cliSock;

        DonneeLogin dl = new DonneeLogin("Sam","PwdSam");

        RequeteTRAMAP req = null;
        req = new RequeteTRAMAP(EnumTRAMAP.LOGIN, dl);

        // Connexion au serveur
        ois=null; oos=null; cliSock = null;
        try
        {
            cliSock = new Socket("127.0.0.1", 50001);
            System.out.println(cliSock.getInetAddress().toString());
        }
        catch (UnknownHostException e)
        { System.err.println("Erreur ! Host non trouvé [" + e + "]"); }
        catch (IOException e)
        { System.err.println("Erreur ! Pas de connexion ? [" + e + "]"); }
        // Envoie de la requête
        try
        {
            oos = new ObjectOutputStream(cliSock.getOutputStream());
            oos.writeObject(req); oos.flush();
        }
        catch (IOException e)
        { System.err.println("Erreur réseau ? [" + e.getMessage() + "]"); }
        // Lecture de la réponse
        ReponseTRAMAP rep = null;
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseTRAMAP)ois.readObject();
            System.out.println(" *** Reponse reçue : " + rep.getCode());
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(System.in));
            String name = reader.readLine();
        }
        catch (ClassNotFoundException e)
        { System.out.println("--- erreur sur la classe = " + e.getMessage()); }
        catch (IOException e)
        { System.out.println("--- erreur IO = " + e.getMessage()); }

    }
}
