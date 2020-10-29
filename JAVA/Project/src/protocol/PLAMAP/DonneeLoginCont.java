//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/10/2020

package protocol.PLAMAP;

import genericRequest.DonneeRequete;
import java.io.Serializable;

public class DonneeLoginCont implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = -2862166183109259082L;
    /********************************/
    /*           Variables          */
    /********************************/
    private int type;
    private String username;
    private String password;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeLoginCont()
    {
        this.type = 1;
    }

    public DonneeLoginCont(String username, String password)
    {
        this.type = 1;
        this.username = username;
        this.password = password;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public int getType()
    {
        return type;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public String toString()
    {
        return  getType() + "#";
    }

    @Override
    public void setFiledsFromString(String fields) {
        String[] parametres = fields.split("#");
        String[] row;
        for(int i = 1; i < parametres.length; i++)
        {
            row = parametres[i].split("=");
            switch (row[0])
            {
                case "username": this.setUsername(row[1]); break;
                case "password": this.setPassword(row[1]); break;
            }
        }
    }
}