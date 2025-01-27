//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/10/2020

package MyGenericServer;

import javax.crypto.SecretKey;

public class Client
{

    /********************************/
    /*           Variables          */
    /********************************/

    private boolean _loggedIn;

    private SecretKey _sessionKey;
    private SecretKey _hmacKey;
    /********************************/
    /*         Constructeurs        */
    /********************************/
    public Client()
    {
        this._loggedIn = false;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public boolean is_loggedIn()
    {
        return _loggedIn;
    }

    public SecretKey get_sessionKey() {
        return _sessionKey;
    }

    public SecretKey get_hmacKey() {
        return _hmacKey;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void set_loggedIn(boolean _loggedIn)
    {
        this._loggedIn = _loggedIn;
    }

    public void set_sessionKey(SecretKey _sessionKey) {
        this._sessionKey = _sessionKey;
    }

    public void set_hmacKey(SecretKey _hmacKey) {
        this._hmacKey = _hmacKey;
    }

    /********************************/
    /*            Methodes          */
    /********************************/

}
