//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package protocol.CHAMAP;

import genericRequest.DonneeRequete;

public class DonneeLoginTraf implements DonneeRequete
{
    private static final long serialVersionUID = 3065921016595731657L;
    /********************************/
    /*           Variables          */
    /********************************/
    private String _nom;
    private byte[] _pwdDigest;
    private long _temps;
    private double _aleatoire;

    /********************************/
    /*         Constructeurs        */
    /********************************/

    public DonneeLoginTraf() {
    }

    public DonneeLoginTraf(String _nom, byte[] _pwdDigest, long _temps, double _aleatoire) {
        this._nom = _nom;
        this._pwdDigest = _pwdDigest;
        this._temps = _temps;
        this._aleatoire = _aleatoire;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public String get_nom() {
        return _nom;
    }

    public byte[] get_pwdDigest() {
        return _pwdDigest;
    }

    public long get_temps() {
        return _temps;
    }

    public double get_aleatoire() {
        return _aleatoire;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void set_nom(String _nom) {
        this._nom = _nom;
    }

    public void set_pwdDigest(byte[] _pwdDigest) {
        this._pwdDigest = _pwdDigest;
    }

    public void set_temps(long _temps) {
        this._temps = _temps;
    }

    public void set_aleatoire(double _aleatoire) {
        this._aleatoire = _aleatoire;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields) {

    }
}
