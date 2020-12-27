//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/12/2020

package protocol.SAMOP;

import genericRequest.DonneeRequete;
import java.io.Serializable;

public class DonneeLogin implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = -8712207813173785731L;

    /********************************/
    /*           Variables          */
    /********************************/
    private String _username;
    private String _password;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeLogin()
    {

    }

    public DonneeLogin(String _username, String _password)
    {
        this._username = _username;
        this._password = _password;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public String get_username()
    {
        return _username;
    }

    public String get_password()
    {
        return _password;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_username(String _username)
    {
        this._username = _username;
    }

    public void set_password(String _password)
    {
        this._password = _password;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields)
    {

    }

    @Override
    public String toString() {
        return "DonneeLogin{" +
                "_username='" + _username + '\'' +
                ", _password='" + _password + '\'' +
                '}';
    }
}