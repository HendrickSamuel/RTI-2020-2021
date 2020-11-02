//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 01/11/2020

package protocol.PIDEP;

import genericRequest.DonneeRequete;
import java.io.Serializable;

public class DonneeLogin implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = 3160554856053867313L;

    /********************************/
    /*           Variables          */
    /********************************/
    private String username;
    private long temps;
    private double alea;
    private byte[] msgD;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeLogin()
    {

    }

    public DonneeLogin(String username, long temps, double alea, byte[] msgD)
    {
        this.username = username;
        this.temps = temps;
        this.alea = alea;
        this.msgD = msgD;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public String getUsername()
    {
        return username;
    }

    public long getTemps()
    {
        return temps;
    }

    public double getAlea()
    {
        return alea;
    }

    public byte[] getMsgD()
    {
        return msgD;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setTemps(long temps)
    {
        this.temps = temps;
    }

    public void setAlea(double alea)
    {
        this.alea = alea;
    }

    public void setMsgD(byte[] msgD)
    {
        this.msgD = msgD;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields)
    {

    }

}
