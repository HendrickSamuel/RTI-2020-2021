//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 10/12/2020

package protocol.PFMCOP;

import genericRequest.DonneeRequete;

import java.io.Serializable;

public class DonneeBaseUDP  implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = 9055160069921171691L;

    /********************************/
    /*           Variables          */
    /********************************/
    private String _username;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeBaseUDP()
    {

    }

    public DonneeBaseUDP(String _username)
    {
        this._username = _username;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public String get_username()
    {
        return _username;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_username(String _username)
    {
        this._username = _username;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public String toString()
    {
        return "username{=}" + get_username();
    }


    @Override
    public void setFiledsFromString(String fields)
    {
        String[] row;
        row = fields.split("\\{=}");

        this.set_username(row[1]);
    }
}