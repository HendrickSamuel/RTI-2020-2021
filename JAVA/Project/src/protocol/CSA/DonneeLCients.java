//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 14/12/2020

package protocol.CSA;

import genericRequest.DonneeRequete;
import protocol.TRAMAP.Operation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DonneeLCients implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = -6214895292909253209L;

    /********************************/
    /*           Variables          */
    /********************************/
    private List<String> _listClient;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeLCients()
    {

    }

    public DonneeLCients(List<String> _listClient)
    {
        this._listClient = _listClient;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public List<String> get_listClient()
    {
        return _listClient;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_listClient(List<String> _listClient)
    {
        this._listClient = _listClient;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public String toString()
    {
        return "#%";
    }

    @Override
    public void setFiledsFromString(String fields)
    {
        String[] row;
        row = fields.split("\\{=}");

        ArrayList<String> al = new ArrayList<String>();
        String[] champs = row[1].split("/");
        for(String champ : champs)
        {
            al.add(champ);
        }

        this.set_listClient(al);
    }

}