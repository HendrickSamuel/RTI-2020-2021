//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package protocol.BISAMAP;

import genericRequest.DonneeRequete;

import java.util.List;

public class DonneeSendBills implements DonneeRequete {
    private static final long serialVersionUID = -8545036812342564917L;

    /********************************/
    /*           Variables          */
    /********************************/
    private List<Integer> _facturesToIgnore;
    private String _userName;

    private byte[] _signature;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeSendBills() {
    }

    public DonneeSendBills(String _userName, List<Integer> _facturesToIgnore) {
        this._facturesToIgnore = _facturesToIgnore;
        this._userName = _userName;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public List<Integer> get_facturesToIgnore() {
        return _facturesToIgnore;
    }

    public String get_userName() {
        return _userName;
    }

    public byte[] get_signature() {
        return _signature;
    }

    public String get_content()
    {
        String content = _userName;
        if(_facturesToIgnore != null)
            content+=_facturesToIgnore.toString();
        return content;
    }
    /********************************/
    /*            Setters           */
    /********************************/
    public void set_facturesToIgnore(List<Integer> _facturesToIgnore) {
        this._facturesToIgnore = _facturesToIgnore;
    }

    public void set_userName(String _userName) {
        this._userName = _userName;
    }

    public void set_signature(byte[] _signature) {
        this._signature = _signature;
    }
    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields) {

    }
}
