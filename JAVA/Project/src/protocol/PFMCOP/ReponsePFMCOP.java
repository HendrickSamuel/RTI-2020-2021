//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 08/12/2020

package protocol.PFMCOP;

import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import java.io.Serializable;

public class ReponsePFMCOP implements Reponse, Serializable
{
    private static final long serialVersionUID = -2415607784185544462L;

    /********************************/
    /*           Variables          */
    /********************************/
    public static int OK = 200;
    public static int NOK = 400;
    public static int REQUEST_NOT_FOUND = 404;

    private int codeRetour;
    private String message;
    private DonneeRequete chargeUtile;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ReponsePFMCOP()
    {

    }

    public ReponsePFMCOP(int codeRetour, String message, DonneeRequete chargeUtile)
    {
        this.codeRetour = codeRetour;
        this.message = message;
        this.chargeUtile = chargeUtile;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    @Override
    public int getCode()
    {
        return codeRetour;
    }

    public String getMessage()
    {
        return message;
    }

    public DonneeRequete getChargeUtile()
    {
        return chargeUtile;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setCodeRetour(int codeRetour)
    {
        this.codeRetour = codeRetour;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

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
        return this.getClass().getName() + "##codeRetour==" + getCode() + "##message==" + getMessage() + "##chargeUtile==" + chargeUtile.getClass().getName() + "#" + getChargeUtile();
    }


    public void setFiledsFromString(String fields) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String[] parametres = fields.split("##");
        String[] row;
        for (int i = 1; i < parametres.length; i++)
        {
            row = parametres[i].split("==");
            switch (row[0]) {
                case "codeRetour":
                    this.setCodeRetour(Integer.parseInt(row[1]));
                    break;
                case "message":
                    System.out.println("Objet reçu: " + row[1]);
                    this.setMessage(row[1]);
                    break;
                case "chargeUtile":
                    String[] recu = row[1].split("#");
                    System.out.println("Objet reçu: " + recu[0]);
                    setChargeUtile((DonneeRequete)Class.forName(recu[0]).newInstance());
                    this.getChargeUtile().setFiledsFromString(row[1]);
                    break;
            }
        }
    }
}

