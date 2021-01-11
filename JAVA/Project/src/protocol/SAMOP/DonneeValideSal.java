//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/12/2020

package protocol.SAMOP;

import genericRequest.DonneeRequete;
import java.io.Serializable;
import java.util.List;

public class DonneeValideSal implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = -7326278654141078310L;

    /********************************/
    /*           Variables          */
    /********************************/
    private String _username;
    private byte[] _signature;

    private List<Virement> liste;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeValideSal()
    {

    }

    public DonneeValideSal(List<Virement> liste)
    {
        this.liste = liste;
    }

    public DonneeValideSal(String _username, byte[] _signature)
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

    public List<Virement> getListe()
    {
        return liste;
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

    public void setListe(List<Virement> liste)
    {
        this.liste = liste;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields)
    {

    }
}