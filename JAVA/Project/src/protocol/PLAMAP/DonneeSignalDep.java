//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 27/10/2020

package protocol.PLAMAP;

import genericRequest.DonneeRequete;
import java.io.Serializable;
import java.util.List;

public class DonneeSignalDep implements DonneeRequete, Serializable
{
    //todo: MAKE_BILL partie 4 avec le serveur compta en réponse au signal_dep
    /********************************/
    /*           Variables          */
    /********************************/
    private String idTransporteur;
    private List<String> ListIdCont;
    private int type;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeSignalDep()
    {
        this.type = 5;
    }

    public DonneeSignalDep(String idTransporteur, List<String> listIdCont)
    {
        this.idTransporteur = idTransporteur;
        ListIdCont = listIdCont;
        this.type = 5;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public int getType()
    {
        return type;
    }

    public String getIdTransporteur()
    {
        return idTransporteur;
    }

    public List<String> getListIdCont()
    {
        return ListIdCont;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setIdTransporteur(String idTransporteur)
    {
        this.idTransporteur = idTransporteur;
    }

    public void setListIdCont(List<String> listIdCont)
    {
        ListIdCont = listIdCont;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public String toString()
    {
        return  getType() + "";
    }
}