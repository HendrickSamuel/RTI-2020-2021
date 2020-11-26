//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 21/11/2020

package Beans;


public class ReponseCaddie 
{
    /********************************/
    /*           Variables          */
    /********************************/
    public static int error = 400;
    public static int ok = 200;
    
    private int _code;
    private String _message;
    
    
    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ReponseCaddie() 
    {
        _code = 0;
    }

    
    /********************************/
    /*            Getters           */
    /********************************/
    public int getCode() 
    {
        return _code;
    }

    public String getMessage() 
    {
        return _message;
    }
    
    
    /********************************/
    /*            Setters           */
    /********************************/
    public void setCode(int _code) 
    {
        this._code = _code;
    }

    public void setMessage(String _message) 
    {
        this._message = _message;
    }
}
