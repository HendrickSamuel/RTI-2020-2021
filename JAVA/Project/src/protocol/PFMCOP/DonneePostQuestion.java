//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 08/12/2020

package protocol.PFMCOP;

import genericRequest.DonneeRequete;
import java.io.Serializable;
import java.util.Base64;

public class DonneePostQuestion implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = -6364482373350061078L;
    /********************************/
    /*           Variables          */
    /********************************/
    private String tag;
    private byte[] _msgDigest;

    private String _user;
    private String _message;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneePostQuestion()
    {

    }

    public DonneePostQuestion(String tag, byte[] _msgDigest, String _user, String _message)
    {
        this.tag = tag;
        this._msgDigest = _msgDigest;
        this._user = _user;
        this._message = _message;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public static long getSerialVersionUID()
    {
        return serialVersionUID;
    }

    public String getTag()
    {
        return tag;
    }

    public byte[] get_msgDigest()
    {
        return _msgDigest;
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
    public void setTag(String tag)
    {
        this.tag = tag;
    }

    public void set_msgDigest(byte[] _msgDigest)
    {
        this._msgDigest = _msgDigest;
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
        return "tag{=}" + getTag() + "#msgDigest{=}" + Base64.getEncoder().encodeToString(get_msgDigest()) + "#user{=}" + get_user()+ "#message{=}" + get_message();
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
                case "tag": this.setTag(row[1]); break;
                case "msgDigest": this.set_msgDigest(Base64.getDecoder().decode(row[1])); break;
                case "user": this.set_user(row[1]); break;
                case "message": this.set_message(row[1]); break;
            }
        }
    }
}