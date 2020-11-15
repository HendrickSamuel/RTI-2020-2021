//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package protocol.BISAMAP;

import java.io.Serializable;

public class Facture implements Serializable {
    private static final long serialVersionUID = -4715689580775144766L;

    /********************************/
    /*           Variables          */
    /********************************/
    private int _id;
    private String _societe;
    private int _mois;
    private int _annee;
    private float _tva;
    private boolean _factureValidee;
    private String _comptable;
    private boolean _factureEnvoyee;
    private String _moyenEnvoi;
    private boolean _facturePayee;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public Facture() {
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public int get_id() {
        return _id;
    }

    public String get_societe() {
        return _societe;
    }

    public int get_mois() {
        return _mois;
    }

    public int get_annee() {
        return _annee;
    }

    public float get_tva() {
        return _tva;
    }

    public boolean is_factureValidee() {
        return _factureValidee;
    }

    public String get_comptable() {
        return _comptable;
    }

    public boolean is_factureEnvoyee() {
        return _factureEnvoyee;
    }

    public String get_moyenEnvoi() {
        return _moyenEnvoi;
    }

    public boolean is_facturePayee() {
        return _facturePayee;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_societe(String _societe) {
        this._societe = _societe;
    }

    public void set_mois(int _mois) {
        this._mois = _mois;
    }

    public void set_annee(int _annee) {
        this._annee = _annee;
    }

    public void set_tva(float _tva) {
        this._tva = _tva;
    }

    public void set_factureValidee(boolean _factureValidee) {
        this._factureValidee = _factureValidee;
    }

    public void set_comptable(String _comptable) {
        this._comptable = _comptable;
    }

    public void set_factureEnvoyee(boolean _factureEnvoyee) {
        this._factureEnvoyee = _factureEnvoyee;
    }

    public void set_moyenEnvoi(String _moyenEnvoi) {
        this._moyenEnvoi = _moyenEnvoi;
    }

    public void set_facturePayee(boolean _facturePayee) {
        this._facturePayee = _facturePayee;
    }

    /********************************/
    /*            Methodes          */
    /********************************/

}
