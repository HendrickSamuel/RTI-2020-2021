//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 14/12/2020

package protocol.CSA;

import genericRequest.DonneeRequete;
import genericRequest.Requete;

import java.io.Serializable;

public class RequeteCSA implements Requete, Serializable
{
    private static final long serialVersionUID = -6291459835590984779L;

    /********************************/
    /*           Variables          */
    /********************************/
    private DonneeRequete chargeUtile;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public RequeteCSA()
    {

    }

    public RequeteCSA(DonneeRequete chargeUtile)
    {
        this.chargeUtile = chargeUtile;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    @Override
    public DonneeRequete getChargeUtile()
    {
        return chargeUtile;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setChargeUtile(DonneeRequete chargeUtile)
    {
        this.chargeUtile = chargeUtile;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public String toString()
    {
        return chargeUtile.getClass().getName() + "##" + chargeUtile;
    }

    public void setFiledsFromString(String fields) throws ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        String[] parametres = fields.split("##");

        System.out.println("Objet reçu: " + parametres[0]);
        setChargeUtile((DonneeRequete)Class.forName(parametres[0]).newInstance());
        this.getChargeUtile().setFiledsFromString(parametres[1]);
    }

}