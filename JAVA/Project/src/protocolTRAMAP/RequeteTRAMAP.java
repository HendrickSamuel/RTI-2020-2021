package protocolTRAMAP;

import genericRequest.Reponse;
import genericRequest.Requete;
import genericServer.ConsoleServeur;
import lib.BeanDBAcces.MysqlConnector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Properties;

public class RequeteTRAMAP implements Requete, Serializable
{
    private EnumTRAMAP _type;
    private DonneesTRAMAP chargeUtile;
    private Socket socketClient;

    public RequeteTRAMAP(EnumTRAMAP t, DonneesTRAMAP chu)
    {
        _type = t;
        chargeUtile = chu;
    }

    public RequeteTRAMAP(EnumTRAMAP t, DonneesTRAMAP chu, Socket s)
    {
        this(t, chu);
        socketClient = s;
    }

    @Override
    public Runnable createRunnable(Socket s, ConsoleServeur cs) {
        switch (_type)
        {
            case LOGIN:
                return new Runnable()
                {
                    public void run() {
                        traiteLOGIN(s,cs);
                    }
                };

            case INPUT_LORY:
                return new Runnable()
                {
                    public void run() {
                        traiteINPUTLORY(s,cs);
                    }
                };

            case INPUT_LORRY_WITHOUT_RESERVATION:
                return new Runnable() {
                    @Override
                    public void run() {
                        traiteINPUTLORYWITHOUTRESERVATION(s, cs);
                    }
                };

            case LIST_OPERATIONS:
                return new Runnable() {
                    @Override
                    public void run() {
                        traiteListe(s, cs);
                    }
                };

            case LOGOUT:
                return new Runnable() {
                    @Override
                    public void run() {
                        traiteLOGOUT(s, cs);
                    }
                };
            default:
                throw new IllegalStateException("Unexpected value: " + _type);
        }
    }

    private void traiteLOGIN(Socket sock, ConsoleServeur cs)
    {
        System.out.println("traiteLOGIN");

        if(chargeUtile instanceof DonneeLogin)
        {
            System.out.println("Mot de passe: " + ((DonneeLogin) chargeUtile).getPassword());
            System.out.println("Utilisateur: " + ((DonneeLogin) chargeUtile).getUsername());

            //MysqlConnector mc = new MysqlConnector("root","root","bd_mouvements");
            ObjectOutputStream oos=null;
            try {
                oos = new ObjectOutputStream(sock.getOutputStream());
                oos.writeObject(new ReponseTRAMAP(ReponseTRAMAP.LOGIN_OK, null, null));
                oos.flush();
            } catch (IOException e) {
                System.out.println("Le client s'est deconnecte apres cette reponse: " + e.getMessage());
            }
        }
        else
        {
            ObjectOutputStream oos=null;
            try {
                oos = new ObjectOutputStream(sock.getOutputStream());
                oos.writeObject(new ReponseTRAMAP(ReponseTRAMAP.BAD_DATA, null, "Objet demande ne correspond pas à l'action demandee"));
                oos.flush();
            } catch (IOException e) {
                System.out.println("Le client s'est deconnecte apres cette reponse: " + e.getMessage());
            }
        }
    }

    private void traiteINPUTLORY(Socket sock, ConsoleServeur cs)
    {
        System.out.println("traiteINPUTLORY");
    }

    private void traiteINPUTLORYWITHOUTRESERVATION(Socket sock, ConsoleServeur cs)
    {
        System.out.println("traiteINPUTLORYWITHOUTRESERVATION");
    }

    private void traiteListe(Socket sock, ConsoleServeur cs)
    {
        System.out.println("traiteListe");
    }

    private void traiteLOGOUT(Socket sock, ConsoleServeur cs)
    {
        System.out.println("traiteLOGOUT");
    }
}
