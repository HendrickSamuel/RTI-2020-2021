//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 08/12/2020

package protocol.PFMCOP;

import genericRequest.DonneeRequete;

import java.io.Serializable;

public class DonneeLoginGroup  implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = -8324185403265070767L;
    /********************************/
    /*           Variables          */
    /********************************/
    private String _username;
    private byte[] _pwdDigest;

    private long _temps;
    private double _aleatoire;

    private String _adresse;
    private String _port;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeLoginGroup()
    {

    }

    public DonneeLoginGroup(String _adresse, String _port)
    {
        this._adresse = _adresse;
        this._port = _port;
    }

    public DonneeLoginGroup(String _nom, byte[] _pwdDigest, long _temps, double _aleatoire)
    {
        this._username = _nom;
        this._pwdDigest = _pwdDigest;
        this._temps = _temps;
        this._aleatoire = _aleatoire;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public String get_username()
    {
        return _username;
    }

    public byte[] get_pwdDigest()
    {
        return _pwdDigest;
    }

    public long get_temps()
    {
        return _temps;
    }

    public double get_aleatoire()
    {
        return _aleatoire;
    }

    public String get_adresse()
    {
        return _adresse;
    }

    public String get_port()
    {
        return _port;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_username(String username)
    {
        this._username = username;
    }

    public void set_pwdDigest(byte[] _pwdDigest)
    {
        this._pwdDigest = _pwdDigest;
    }

    public void set_temps(long _temps)
    {
        this._temps = _temps;
    }

    public void set_aleatoire(double _aleatoire)
    {
        this._aleatoire = _aleatoire;
    }

    public void set_adresse(String adresse)
    {
        _adresse = adresse;
    }

    public void set_port(String port)
    {
        _port = port;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields)
    {

    }

}