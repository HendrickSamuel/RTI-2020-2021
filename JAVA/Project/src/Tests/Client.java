/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package Tests;

import genericRequest.DonneeRequete;
import genericRequest.Requete;
import protocolTRAMAP.*;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;

public class Client
{
    public static void main(String[] args)
    {
        ObjectInputStream ois;
        ObjectOutputStream oos;
        Socket cliSock;

        System.out.println("Veuillez selectionner votre action: ");
        System.out.println("1. Login");
        System.out.println("2. InputLory");
        System.out.println("3. InputLory Without Reservation");
        System.out.println("4. DonneeList");
        System.out.println("5. Logout");
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        DonneeRequete dt = null;
        try
        {
            String name = reader.readLine();
            int option = Integer.parseInt(name);
            switch (option)
            {
                case 1: dt = new DonneeLogin("Sam","superSecurePass123"); break;
                case 2: dt = new DonneeInputLory("test","blabla"); break;
                case 3: dt = new DonneeInputLoryWithoutReservation("Container"); break;
                case 4: dt = new DonneeListOperations(new Date(), new Date(), "Societe", "Destination"); break;
                case 5: dt = new DonneeLogout("SamOut","SamP"); break;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        RequeteTRAMAP req = null;
        req = new RequeteTRAMAP(dt);

        // Connexion au serveur
        ois=null; oos=null; cliSock = null;
        try
        {
            cliSock = new Socket("127.0.0.1", 50001);
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
        ReponseTRAMAP rep = null;
        try
        {
            ois = new ObjectInputStream(cliSock.getInputStream());
            rep = (ReponseTRAMAP)ois.readObject();
            System.out.println(" *** Reponse reçue : " + rep.getCode());
            if(rep.getMessage() != null)
            {
                System.out.println("Message reçu: " + rep.getMessage());
            }
            reader.readLine();
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
