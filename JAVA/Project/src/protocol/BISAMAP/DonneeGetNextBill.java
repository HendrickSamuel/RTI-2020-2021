//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 15/11/2020

package protocol.BISAMAP;

import genericRequest.DonneeRequete;

import javax.crypto.SealedObject;

public class DonneeGetNextBill implements DonneeRequete {
    private static final long serialVersionUID = -6690572510423430173L;

    /********************************/
    /*           Variables          */
    /********************************/

    private SealedObject factureCryptee;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeGetNextBill() {
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public SealedObject getFactureCryptee() {
        return factureCryptee;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setFactureCryptee(SealedObject factureCryptee) {
        this.factureCryptee = factureCryptee;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields) {

    }
}
