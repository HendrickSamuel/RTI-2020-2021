//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 08/12/2020

package protocol.PFMCOP;

import genericRequest.DonneeRequete;

import java.io.Serializable;
import java.util.Base64;

public class DonneePostEvent implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = -118168326695754679L;
    /********************************/
    /*           Variables          */
    /********************************/
    private String _tag;
    private String _user;
    private String _message;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneePostEvent()
    {

    }

    public DonneePostEvent(String _tag, String _user, String _message)
    {
        this._tag = _tag;
        this._user = _user;
        this._message = _message;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public String get_tag()
    {
        return _tag;
    }

    public String get_user()
    {
        return _user;
    }

    public String get_message()
    {
        return _message;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_tag(String _tag)
    {
        this._tag = _tag;
    }

    public void set_user(String _user)
    {
        this._user = _user;
    }

    public void set_message(String _message)
    {
        this._message = _message;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public String toString()
    {
        return "tag{=}" + get_tag() + "#user{=}" + get_user()+ "#message{=}" + get_message();
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
                case "tag": this.set_tag(row[1]); break;
                case "user": this.set_user(row[1]); break;
                case "message": this.set_message(row[1]); break;
            }
        }
    }
}