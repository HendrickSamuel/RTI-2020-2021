//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package protocol.BISAMAP;

import genericRequest.DonneeRequete;

import javax.crypto.SealedObject;
import java.util.Date;
import java.util.List;

public class DonneeListBills implements DonneeRequete {
    private static final long serialVersionUID = 3083234996227904444L;

    /********************************/
    /*           Variables          */
    /********************************/
    private String _societe;
    private Date _dateDepart;
    private Date _dateFin;

    private byte[] _signature;
    private List<SealedObject> _factures;

    private transient List<Facture>facturesDecryptees;
    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeListBills() {
    }


    public DonneeListBills(String _societe, Date _dateDepart, Date _dateFin) {
        this._societe = _societe;
        this._dateDepart = _dateDepart;
        this._dateFin = _dateFin;
    }
    /********************************/
    /*            Getters           */
    /********************************/
    public String get_societe() {
        return _societe;
    }

    public Date get_dateDepart() {
        return _dateDepart;
    }

    public Date get_dateFin() {
        return _dateFin;
    }

    public byte[] get_signature() {
        return _signature;
    }

    public String get_content()
    {
        return _societe + _dateDepart.toString() + _dateFin.toString();
    }

    public List<SealedObject> get_factures() {
        return _factures;
    }

    public List<Facture> getFacturesDecryptees() {
        return facturesDecryptees;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void set_societe(String _societe) {
        this._societe = _societe;
    }

    public void set_dateDepart(Date _dateDepart) {
        this._dateDepart = _dateDepart;
    }

    public void set_dateFin(Date _dateFin) {
        this._dateFin = _dateFin;
    }

    public void set_signature(byte[] _signature) {
        this._signature = _signature;
    }

    public void set_factures(List<SealedObject> _factures) {
        this._factures = _factures;
    }

    public void setFacturesDecryptees(List<Facture> facturesDecryptees) {
        this.facturesDecryptees = facturesDecryptees;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields) {

    }
}
