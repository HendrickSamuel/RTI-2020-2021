/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package Tests;

import Serveurs.Compta.Client.ClientCompta;
import genericRequest.DonneeRequete;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import protocol.BISAMAP.*;
import protocol.BISAMAP.DonneeLogin;
import protocol.TRAMAP.*;
import security.SecurityHelper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Date;

public class Client
{
    public static void main(String[] args) throws Exception {
        ClientCompta cc = new ClientCompta();
        cc.SendLogin("Samuel","sam");
        if(cc.is_connected()) {
            ReponseBISAMAP rep = (ReponseBISAMAP) cc.GetNextBill();
            Facture facture = ((DonneeGetNextBill)rep.getChargeUtile()).get_facture();
            if(facture != null)
            {
                System.out.println(facture.get_societe());
                if(cc.ValidateBill(1).getCode() == 200)
                {
                    System.out.println("Facture validee");
                }
                else
                {
                    System.out.println("Facture non validee");
                }
            }
            else
            {
                System.out.println("pas de nouvelle facture");
            }
            cc.closeSocket();
        }
        else
        {
            System.out.println("Erreur de connection");
        }

    }
}
