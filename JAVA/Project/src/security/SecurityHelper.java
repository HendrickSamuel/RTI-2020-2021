//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

public class SecurityHelper {

    /********************************/
    /*           Variables          */
    /********************************/

    private String _codeProvider = "BC";
    private String _algoDigest = "SHA-256";
    private String _algoHMAC;
    private String _algoSymetrique = "DES";
    private String _algoSymetriqueCipher = "DES/CBC/PKCS5Padding";
    private String _algoAssymetrique = "RSA/ECB/PKCS1Padding";

    private KeyStore _keystore;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public SecurityHelper() {
        Security.addProvider(new BouncyCastleProvider());
    }

    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/

    public void initKeyStore(String location, String password) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        _keystore = KeyStore.getInstance("JKS"); //PKCS12
        _keystore.load(new FileInputStream(location), password.toCharArray());
    }

    /********************************/
    /*            Methodes          */
    /********************************/

    public SecretKey getSecretKey() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance(_algoSymetrique,_codeProvider);
        keygen.init(new SecureRandom());
        return keygen.generateKey();
    }

    public byte[] cipherSecretKey(SecretKey key, String communicantAlias) throws KeyStoreException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        PublicKey publicKey = (PublicKey)_keystore.getCertificate(communicantAlias).getPublicKey();
        Cipher chiffrement = Cipher.getInstance(_algoAssymetrique,_codeProvider);
        chiffrement.init(Cipher.ENCRYPT_MODE, publicKey);
        return chiffrement.doFinal(key.getEncoded());
    }

    public SecretKey decipherSecretKey(byte[] cipheredKey, String yourAlias, String yourPassword) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnrecoverableKeyException, KeyStoreException {
        PrivateKey privateKey = (PrivateKey)_keystore.getKey(yourAlias, yourPassword.toCharArray());
        Cipher dechiffrement = Cipher.getInstance(_algoAssymetrique,_codeProvider);
        dechiffrement.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] cleDechiffree = dechiffrement.doFinal(cipheredKey);

        return new SecretKeySpec(cleDechiffree, 0, cleDechiffree.length, _algoSymetrique);
    }

    public boolean CompareDigests(byte[] digested, byte[] toDigest, long time, double random ) throws NoSuchProviderException, NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(_algoDigest, _codeProvider);
        md.update(toDigest);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream bdos = new DataOutputStream(baos);
        bdos.writeLong(time);
        bdos.writeDouble(random);
        md.update(baos.toByteArray());

        byte[] msgDLocal = md.digest();
        return MessageDigest.isEqual(digested, msgDLocal);
    }

    public SealedObject cipherObject(Serializable object, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException, IllegalBlockSizeException {
        Cipher chiffrementObjet = Cipher.getInstance(_algoSymetriqueCipher,_codeProvider);
        chiffrementObjet.init(Cipher.ENCRYPT_MODE, secretKey);
        return new SealedObject(object, chiffrementObjet);
    }

    public Object decipherObject(SealedObject object, SecretKey secretKey) throws ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        return object.getObject(secretKey);
    }



}
