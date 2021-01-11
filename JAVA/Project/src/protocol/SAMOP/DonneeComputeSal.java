//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/12/2020

package protocol.SAMOP;

import genericRequest.DonneeRequete;
import java.io.Serializable;

public class DonneeComputeSal implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = 1986668120301904461L;

    /********************************/
    /*           Variables          */
    /********************************/
    private String _username;
    private byte[] _signature;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeComputeSal()
    {

    }

    public DonneeComputeSal(String _username, byte[] _signature)
    {
        this._username = _username;
        this._signature = _signature;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public String get_username()
    {
        return _username;
    }

    public byte[] get_signature()
    {
        return _signature;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_username(String _username)
    {
        this._username = _username;
    }

    public void set_signature(byte[] _signature)
    {
        this._signature = _signature;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields)
    {

    }
}