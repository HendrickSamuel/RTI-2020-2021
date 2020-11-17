//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package protocol.BISAMAP;

import genericRequest.DonneeRequete;

import javax.crypto.SealedObject;
import java.util.List;

public class DonneeListWaiting implements DonneeRequete {
    private static final long serialVersionUID = 1789454630741857067L;

    /********************************/
    /*           Variables          */
    /********************************/
    public static int Societe = 1;
    public static int Duree = 2;

    private String _societe;
    private String _comptable;
    private int _nature;

    private byte[] _signature;

    private List<SealedObject> _factures;
    private transient List<Facture> _facturesDecryptees;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeListWaiting(String _comptable, int _nature) {
        this._comptable = _comptable;
        this._nature = _nature;
    }
    /********************************/
    /*            Getters           */
    /********************************/
    public String get_societe() {
        return _societe;
    }

    public int get_nature() {
        return _nature;
    }

    public byte[] get_signature() {
        return _signature;
    }

    public String get_comptable() {
        return _comptable;
    }

    public List<SealedObject> get_factures() {
        return _factures;
    }

    public List<Facture> get_facturesDecryptees() {
        return _facturesDecryptees;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void set_societe(String _societe) {
        this._societe = _societe;
    }

    public void set_nature(int _nature) {
        this._nature = _nature;
    }

    public void set_signature(byte[] _signature) {
        this._signature = _signature;
    }

    public void set_comptable(String _comptable) {
        this._comptable = _comptable;
    }

    public void set_factures(List<SealedObject> _factures) {
        this._factures = _factures;
    }

    public String get_content(){
        if(_nature == 1)
            return ""+_nature+_societe+_comptable;
        else
            return ""+_nature+_comptable;
    }

    public void set_facturesDecryptees(List<Facture> _facturesDecryptees) {
        this._facturesDecryptees = _facturesDecryptees;
    }

    /********************************/
    /*            Methodes          */
    /********************************/

    @Override
    public void setFiledsFromString(String fields) {

    }
}
