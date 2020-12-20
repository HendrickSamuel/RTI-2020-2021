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
import java.security.cert.X509Certificate;

public class SecurityHelper {

    /********************************/
    /*           Variables          */
    /********************************/

    private String _codeProvider = "BC";
    private String _algoDigest = "SHA-256";
    private String _algoHMAC = "HMAC-MD5";
    private String _algoSymetrique = "DES";
    private String _algoSymetriqueCipher = "DES/ECB/PKCS5Padding";
    private String _algoAssymetrique = "RSA/ECB/PKCS1Padding";
    private String _algoSignature = "SHA256withRSA";

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

    public byte[] cipherMessage(byte[] message, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher chiffrement = Cipher.getInstance(_algoSymetriqueCipher,_codeProvider);
        chiffrement.init(Cipher.ENCRYPT_MODE, secretKey);
        return chiffrement.doFinal(message);
    }

    public byte[] decipherMessage(byte[] cipheredMessage, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher dechiffrement = Cipher.getInstance(_algoSymetriqueCipher,_codeProvider);
        dechiffrement.init(Cipher.DECRYPT_MODE, secretKey);
        return dechiffrement.doFinal(cipheredMessage);
    }

    public byte[] createSaltedDigest(String message, long time, double random) throws NoSuchProviderException, NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(_algoDigest, _codeProvider);
        md.update(message.getBytes());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream bdos = new DataOutputStream(baos);
        bdos.writeLong(time);
        bdos.writeDouble(random);

        md.update(baos.toByteArray());
        return md.digest();
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

    public byte[] signMessage(byte[] message, String yourAlias, String yourPassword) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, InvalidKeyException, SignatureException {
        PrivateKey privateKey = (PrivateKey)_keystore.getKey(yourAlias, yourPassword.toCharArray());
        Signature s = Signature.getInstance(_algoSignature,_codeProvider);
        s.initSign(privateKey);
        s.update(message);
        return s.sign();
    }

    public boolean verifySignature(byte[] message, byte[] signature, String communicantAlia) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        X509Certificate certif = (X509Certificate)_keystore.getCertificate(communicantAlia);
        PublicKey publicKey = certif.getPublicKey();
        Signature sign = Signature.getInstance(_algoSignature, _codeProvider);
        sign.initVerify(publicKey);
        sign.update(message);
        return sign.verify(signature);
    }

    public byte[] createHMAC(byte[] message, SecretKey secretKey) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance(_algoHMAC, _codeProvider);
        hmac.init(secretKey);
        hmac.update(message);
        return hmac.doFinal();
    }

    public boolean verifyHMAC(byte[] message, byte[] hmacMessage, SecretKey secretKey) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance(_algoHMAC, _codeProvider);
        hmac.init(secretKey);
        hmac.update(message);
        byte[] bytes = hmac.doFinal();
        return MessageDigest.isEqual(bytes, hmacMessage);
    }

    public  byte[] createDigest(String message) throws NoSuchProviderException, NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(_algoDigest, _codeProvider);
        md.update(message.getBytes());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        md.update(baos.toByteArray());
        return md.digest();
    }

    public boolean CompareSimpleDigests(byte[] digested, byte[] toDigest) throws NoSuchProviderException, NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance(_algoDigest, _codeProvider);
        md.update(toDigest);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        md.update(baos.toByteArray());

        byte[] msgDLocal = md.digest();
        return MessageDigest.isEqual(digested, msgDLocal);
    }

}
