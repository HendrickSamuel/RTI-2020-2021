//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package protocol.BISAMAP;

import genericRequest.DonneeRequete;

public class DonneeValidateBill implements DonneeRequete {
    private static final long serialVersionUID = -281271144626328253L;

    /********************************/
    /*           Variables          */
    /********************************/
    private int _factureNumber;
    private String _comtpable;
    private byte[] _signature;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeValidateBill() {
    }

    public DonneeValidateBill(int _factureNumber, String _comtpable) {
        this._factureNumber = _factureNumber;
        this._comtpable = _comtpable;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public int get_factureNumber() {
        return _factureNumber;
    }

    public String get_comtpable() {
        return _comtpable;
    }

    public byte[] get_signature() {
        return _signature;
    }

    public String get_content()
    {
        return _comtpable+_factureNumber;
    }
    /********************************/
    /*            Setters           */
    /********************************/
    public void set_factureNumber(int _factureNumber) {
        this._factureNumber = _factureNumber;
    }

    public void set_signature(byte[] _signature) {
        this._signature = _signature;
    }

    public void set_comtpable(String _comtpable) {
        this._comtpable = _comtpable;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields) {

    }
}
