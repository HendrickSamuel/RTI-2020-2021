//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package Tests;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Calendar;

public class CryptageTest {

    public static void main(String[] args) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnrecoverableKeyException {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("./Confs/ComptaKeyVault"), "password".toCharArray());

        PublicKey publicKey = (PublicKey)ks.getCertificate("ClientKeyEntry").getPublicKey();
        System.out.println("cle publique: " + publicKey.toString());

        byte[] message = "coucou thomas, ceci est un message secret".getBytes();
        System.out.println("Message avant: " + new String(message));
        Cipher chiffrement = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
        chiffrement.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] messageCrypte = chiffrement.doFinal(message);

        System.out.println("Message crypte: " + new String(messageCrypte));

        KeyStore ksclient = KeyStore.getInstance("JKS");
        ksclient.load(new FileInputStream("./Confs/ClientKeyVault"), "password".toCharArray());
        PrivateKey privateKey = (PrivateKey)ksclient.getKey("ClientKeyVault","password".toCharArray());

        Cipher dechiffrement = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
        dechiffrement.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] textdechiffre = dechiffrement.doFinal(messageCrypte);

        System.out.println("Message decode: " + new String(textdechiffre));

    }

}
