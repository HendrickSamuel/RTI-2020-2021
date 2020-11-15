//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package Tests;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Calendar;

public class CryptageCles {

    public static void main(String[] args) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("./Confs/ComptaKeyVault"), "password".toCharArray());

        PublicKey publicKey = (PublicKey)ks.getCertificate("ClientKeyEntry").getPublicKey();
        System.out.println("cle publique: " + publicKey.toString());

        KeyGenerator keygen = KeyGenerator.getInstance("DES","BC");
        keygen.init(new SecureRandom());
        SecretKey cleSession =  keygen.generateKey();
        System.out.println(cleSession);

        //byte[] message = "coucou thomas, ceci est un message secret".getBytes();

        Cipher chiffrement = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
        chiffrement.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cleChiffree = chiffrement.doFinal(cleSession.getEncoded());

        //fin serveur

        KeyStore ksclient = KeyStore.getInstance("JKS");
        ksclient.load(new FileInputStream("./Confs/ClientKeyVault"), "password".toCharArray());
        PrivateKey privateKey = (PrivateKey)ksclient.getKey("ClientKeyVault","password".toCharArray());

        Cipher dechiffrement = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
        dechiffrement.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] cleDechiffree = dechiffrement.doFinal(cleChiffree);

        SecretKey seccondKey = new SecretKeySpec(cleDechiffree, 0, cleDechiffree.length, "DES");
        System.out.println(seccondKey);
    }

}
