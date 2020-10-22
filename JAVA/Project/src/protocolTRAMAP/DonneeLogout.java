/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package protocolTRAMAP;

import genericRequest.DonneeRequete;

import java.io.Serializable;

public class DonneeLogout implements DonneeRequete, Serializable
{
    /********************************/
    /*          Variables           */
    /********************************/
    private String username;
    private String password;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeLogout(String username, String password)
    {
        setUsername(username);
        setPassword(password);
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

}
