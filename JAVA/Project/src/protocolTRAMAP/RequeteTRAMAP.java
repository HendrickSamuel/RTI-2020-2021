/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

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
    /********************************/
    /*           Variables          */
    /********************************/
    private EnumTRAMAP _type;
    private DonneesTRAMAP chargeUtile;
    private Socket socketClient;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public RequeteTRAMAP(DonneesTRAMAP chu)
    {
        chargeUtile = chu;
        if(chu instanceof DonneeLogin)
            _type = EnumTRAMAP.LOGIN;
        else if(chu instanceof DonneeInputLory)
            _type = EnumTRAMAP.INPUT_LORY;
        else if(chu instanceof DonneeInputLoryWithoutReservation)
            _type = EnumTRAMAP.INPUT_LORRY_WITHOUT_RESERVATION;
        else if(chu instanceof DonneeListOperations)
            _type = EnumTRAMAP.LIST_OPERATIONS;
        else if(chu instanceof  DonneeLogout)
            _type = EnumTRAMAP.LOGOUT;
        else
            _type = EnumTRAMAP.NONE;
    }

    public RequeteTRAMAP(DonneesTRAMAP chu, Socket s)
    {
        this(chu);
        socketClient = s;
    }


    /********************************/
    /*          Méthodes            */
    /********************************/
    @Override
    public Runnable createRunnable(Socket s, ConsoleServeur cs) {
        switch (_type)
        {
            case LOGIN:
                return new Runnable()
                {
                    public void run()
                    {
                        traiteLOGIN(s,cs);
                    }
                };

            case INPUT_LORY:
                return new Runnable()
                {
                    public void run()
                    {
                        traiteINPUTLORY(s,cs);
                    }
                };

            case INPUT_LORRY_WITHOUT_RESERVATION:
                return new Runnable()
                {
                    @Override
                    public void run()
                    {
                        traiteINPUTLORYWITHOUTRESERVATION(s, cs);
                    }
                };

            case LIST_OPERATIONS:
                return new Runnable()
                {
                    @Override
                    public void run()
                    {
                        traiteListe(s, cs);
                    }
                };

            case LOGOUT:
                return new Runnable()
                {
                    @Override
                    public void run()
                    {
                        traiteLOGOUT(s, cs);
                    }
                };
            default:
                return new Runnable()
                {
                    @Override
                    public void run()
                    {
                        traite404(s, cs);
                    }
                };
        }
    }

    private void traiteLOGIN(Socket sock, ConsoleServeur cs)
    {
        System.out.println("traiteLOGIN");

        System.out.println("Mot de passe: " + ((DonneeLogin) chargeUtile).getPassword());
        System.out.println("Utilisateur: " + ((DonneeLogin) chargeUtile).getUsername());

        //MysqlConnector mc = new MysqlConnector("root","root","bd_mouvements");
        ObjectOutputStream oos=null;
        try
        {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(new ReponseTRAMAP(ReponseTRAMAP.LOGIN_OK, null, null));
            oos.flush();
        }
        catch (IOException e)
        {
            System.out.println("Le client s'est deconnecte apres cette reponse: " + e.getMessage());
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

    private void traite404(Socket sock, ConsoleServeur cs)
    {
        System.out.println("traite404 Request not found");
        ObjectOutputStream oos=null;
        try
        {
            oos = new ObjectOutputStream(sock.getOutputStream());
            oos.writeObject(new ReponseTRAMAP(ReponseTRAMAP.REQUEST_NOT_FOUND, null, "request could not be exeuted due to unsopported version."));
            oos.flush();
        }
        catch (IOException e)
        {
            System.out.println("Le client s'est deconnecte apres cette reponse: " + e.getMessage());
        }
    }
}
