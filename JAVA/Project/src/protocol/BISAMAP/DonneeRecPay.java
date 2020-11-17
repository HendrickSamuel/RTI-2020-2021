//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package protocol.BISAMAP;

import genericRequest.DonneeRequete;

public class DonneeRecPay implements DonneeRequete {
    private static final long serialVersionUID = -7370139342959375532L;

    /********************************/
    /*           Variables          */
    /********************************/
    private int _facture;
    //private String _infosBancaires;
    private String _comptable;
    private float _montant;
    private byte[] _infosBancaireCryptees;
    private byte[] _hmac;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeRecPay() {
    }

    public DonneeRecPay(int _facture, float _montant, String _comptable) {
        this._montant = _montant;
        this._facture = _facture;
        this._comptable = _comptable;
    }
    /********************************/
    /*            Getters           */
    /********************************/
    public int get_facture() {
        return _facture;
    }

    public String get_comptable() {
        return _comptable;
    }

    public byte[] get_infosBancaireCryptees() {
        return _infosBancaireCryptees;
    }

    public String get_content(){
        return _comptable+_facture;
    }

    public byte[] get_hmac() {
        return _hmac;
    }

    public float get_montant() {
        return _montant;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void set_facture(int _facture) {
        this._facture = _facture;
    }

    public void set_comptable(String _comptable) {
        this._comptable = _comptable;
    }

    public void set_infosBancaireCryptees(byte[] _infosBancaireCryptees) {
        this._infosBancaireCryptees = _infosBancaireCryptees;
    }

    public void set_hmac(byte[] _hmac) {
        this._hmac = _hmac;
    }

    public void set_montant(float _montant) {
        this._montant = _montant;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields) {

    }
}
