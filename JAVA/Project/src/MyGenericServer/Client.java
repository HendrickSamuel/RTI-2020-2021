//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/10/2020

package MyGenericServer;

public class Client
{

    /********************************/
    /*           Variables          */
    /********************************/

    private boolean _loggedIn;

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

    /********************************/
    /*            Setters           */
    /********************************/
    public void set_loggedIn(boolean _loggedIn)
    {
        this._loggedIn = _loggedIn;
    }

    /********************************/
    /*            Methodes          */
    /********************************/

}
