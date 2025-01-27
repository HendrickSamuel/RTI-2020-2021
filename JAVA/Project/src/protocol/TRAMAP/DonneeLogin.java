/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package protocol.TRAMAP;

import genericRequest.DonneeRequete;

import java.io.Serializable;

public class DonneeLogin implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = -2273468739102007978L;
    /********************************/
    /*           Variables          */
    /********************************/
    private String username;
    private String password;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeLogin(String username, String password)
    {
        this.username = username;
        this.password = password;
    }


    /********************************/
    /*            Getters           */
    /********************************/
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
    public void setFiledsFromString(String fields) {

    }

}
