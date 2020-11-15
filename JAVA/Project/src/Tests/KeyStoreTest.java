//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package Tests;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class KeyStoreTest {
    public static void main(String[] args) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, InvalidKeyException, SignatureException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("./Confs/ComptaKeyVault"), "password".toCharArray());

        System.out.println("-- Recuperation de la cle privee --");
        PrivateKey privateKey = (PrivateKey)ks.getKey("ComptaKeyVault","password".toCharArray());
        System.out.println("la cle: " + privateKey.toString());

        String Message = "Code du jour : CVCCDMMM - bye";
        System.out.println("Message a envoyer au serveur : " + Message);
        byte[] message = Message.getBytes();
        System.out.println("Instanciation de la signature");
        Signature s = Signature.getInstance("SHA256withRSA","BC");
        System.out.println("Initialisation de la signature");
        s.initSign(privateKey);
        System.out.println("Hachage du message");
        s.update(message);
        System.out.println("Generation des bytes");
        byte[] signature = s.sign();
        System.out.println("Termine : signature construite");
        System.out.println("Signature = " + new String(signature));

        //fin du client

        System.out.println("\nVérification de la signature");
        KeyStore ksv = KeyStore.getInstance("PKCS12", "BC");
        ksv.load(new FileInputStream("./Confs/ClientKeyVault"),
                "password".toCharArray());
        System.out.println("Recuperation du certificat");
        X509Certificate certif = (X509Certificate)ksv.getCertificate("ComptaKeyEntry");
        System.out.println("Recuperation de la cle publique");
        PublicKey cléPublique = certif.getPublicKey();
        System.out.println("*** Cle publique recuperee = "+cléPublique.toString());
        System.out.println("Debut de verification de la signature construite");
        Signature sv = Signature.getInstance("SHA256withRSA", "BC");
        sv.initVerify(cléPublique);
        System.out.println("Hachage du message");
        sv.update(message);
        System.out.println("Verification de la signature construite");
        boolean ok = sv.verify(signature);
        if (ok) System.out.println("Signature testee avec succes");
        else System.out.println("Signature testee sans succes");
    }

}
