//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 10/12/2020

package Serveurs.Chat.Client;

public class Question
{
    /********************************/
    /*           Variables          */
    /********************************/
    private String _tag;
    private String _question;
    private byte[] _msgDigest;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public Question()
    {

    }

    public Question(String _tag, String _question, byte[] _msgDigest)
    {
        this._tag = _tag;
        this._question = _question;
        this._msgDigest = _msgDigest;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public String get_tag()
    {
        return _tag;
    }

    public String get_question()
    {
        return _question;
    }

    public byte[] get_msgDigest()
    {
        return _msgDigest;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void set_tag(String _tag)
    {
        this._tag = _tag;
    }

    public void set_question(String _question)
    {
        this._question = _question;
    }

    public void set_msgDigest(byte[] _msgDigest)
    {
        this._msgDigest = _msgDigest;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public String toString()
    {
        return get_tag() + " : " + get_question();
    }
}