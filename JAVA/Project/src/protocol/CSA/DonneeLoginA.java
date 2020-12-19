//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 14/12/2020

package protocol.CSA;

import genericRequest.DonneeRequete;

import java.io.Serializable;

public class DonneeLoginA implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = 6870730171267860212L;

    /********************************/
    /*           Variables          */
    /********************************/
    private String _username;
    private String _password;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeLoginA()
    {

    }

    public DonneeLoginA(String _username, String _password)
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
    public String toString()
    {
        return "username{=}" + get_username()+ "#password{=}" + get_password() + "#%";
    }


    @Override
    public void setFiledsFromString(String fields)
    {
        String[] parametres = fields.split("#");
        String[] row;
        for(int i = 0; i < parametres.length; i++)
        {
            row = parametres[i].split("\\{=}");
            switch (row[0])
            {
                case "username": this.set_username(row[1]); break;
                case "password": this.set_password(row[1]); break;
            }
        }
    }
}