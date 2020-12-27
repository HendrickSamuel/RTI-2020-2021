//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/12/2020

package protocol.SAMOP;

import genericRequest.DonneeRequete;
import java.io.Serializable;

public class DonneeLaunchPayement implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = 8570434704124069770L;

    /********************************/
    /*           Variables          */
    /********************************/
    private String empName;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeLaunchPayement()
    {

    }

    public DonneeLaunchPayement(String empName)
    {
        this.empName = empName;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public String getEmpName()
    {
        return empName;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setEmpName(String empName)
    {
        this.empName = empName;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields)
    {

    }
}