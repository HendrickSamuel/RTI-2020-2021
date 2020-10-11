/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package protocolTRAMAP;

import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import genericRequest.Traitement;
import MyGenericServer.ConsoleServeur;
import lib.BeanDBAcces.BDMouvements;

public class TraitementTRAMAP implements Traitement
{

    //todo: rajouter un Objet client ici

    BDMouvements _bd;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public TraitementTRAMAP() {
        this._bd = new BDMouvements("root","root","bd_mouvements");;
    }

    /********************************/
    /*            Getters           */
    /********************************/
    public BDMouvements get_bd() {
        return _bd;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void set_bd(BDMouvements _bd) {
        this._bd = _bd;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public Reponse traiteRequete(DonneeRequete Requete) throws ClassCastException
    {
        if(Requete instanceof DonneeLogin)
            return traiteLOGIN(null, (DonneeLogin)Requete);
        else if(Requete instanceof DonneeInputLory)
            return traiteINPUTLORY(null, (DonneeInputLory)Requete);
        else if(Requete instanceof DonneeInputLoryWithoutReservation)
            traiteINPUTLORYWITHOUTRESERVATION( null, (DonneeInputLoryWithoutReservation)Requete);
        else if(Requete instanceof DonneeListOperations)
            traiteListe( null, (DonneeListOperations)Requete);
        else if(Requete instanceof  DonneeLogout)
            traiteLOGOUT( null, (DonneeLogout)Requete);
        else
            return traite404( null);

        return null;
    }

    private Reponse traiteLOGIN(ConsoleServeur cs, DonneeLogin chargeUtile)
    {
        System.out.println("traiteLOGIN");

        System.out.println("Mot de passe: " + chargeUtile.getPassword());
        System.out.println("Utilisateur: " + chargeUtile.getUsername());
        boolean ret = _bd.tryLogin(chargeUtile.getUsername(),chargeUtile.getPassword());
        if(ret)
            return new ReponseTRAMAP(ReponseTRAMAP.LOGIN_OK, null, null);
        else
            return new ReponseTRAMAP(ReponseTRAMAP.LOGIN_NOK, null, "Mot de passe ou nom d'utilisateur erroné");
    }

    private Reponse traiteINPUTLORY(ConsoleServeur cs, DonneeInputLory chargeUtile)
    {
        System.out.println("traiteINPUTLORY");
        Object ret = _bd.getReservation(chargeUtile.getNumeroReservation(), chargeUtile.getIdContainer());
        if(ret != null)
        {
            return new ReponseTRAMAP(ReponseTRAMAP.OK, null, null);
        }
        else
        {
            return new ReponseTRAMAP(ReponseTRAMAP.LOGIN_NOK, null, "Aucune réservation ne correspond à ce container");
            //todo: verifier si on attent + si les 2 vont ensemble ?
        }
    }

    private void traiteINPUTLORYWITHOUTRESERVATION(ConsoleServeur cs, DonneeInputLoryWithoutReservation chargeUtile)
    {
        System.out.println("traiteINPUTLORYWITHOUTRESERVATION");
    }

    private void traiteListe(ConsoleServeur cs, DonneeListOperations chargeUtile)
    {
        System.out.println("traiteListe");
    }

    private void traiteLOGOUT(ConsoleServeur cs, DonneeLogout chargeUtile)
    {
        System.out.println("traiteLOGOUT");
    }

    private Reponse traite404(ConsoleServeur cs)
    {
        System.out.println("traite404 Request not found");
        return new ReponseTRAMAP(ReponseTRAMAP.REQUEST_NOT_FOUND, null, "request could not be exeuted due to unsopported version.");
    }
}
