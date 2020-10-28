//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 28/10/2020

package Serveurs.Mouvement.Serveur;

import MyGenericServer.ConsoleServeur;
import MyGenericServer.ServeurGenerique;
import genericRequest.Reponse;
import genericRequest.Traitement;
import lib.BeanDBAcces.BDMouvements;
import protocol.PLAMAP.ReponsePLAMAP;
import protocol.PLAMAP.TraitementPLAMAP;

public class ServeurMouvementPLAMAP extends ServeurGenerique
{
    /********************************/
    /*           Variables          */
    /********************************/
    private BDMouvements _bd;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ServeurMouvementPLAMAP(int port, boolean connecte, int NbThreads, BDMouvements bd , ConsoleServeur cs)
    {
        super(port, connecte, NbThreads, cs, false);
        set_bd(bd);
    }

    public ServeurMouvementPLAMAP(int port, boolean connecte, int NbThreads, BDMouvements bd , ConsoleServeur cs , boolean isJava)
    {
        super(port, connecte, NbThreads, cs, isJava);
        set_bd(bd);
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public BDMouvements get_bd()
    {
        return _bd;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void set_bd(BDMouvements _bd)
    {
        this._bd = _bd;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public Traitement CreationTraitement()
    {
        TraitementPLAMAP tp = new TraitementPLAMAP();
        tp.set_bd(get_bd());
        tp.setConsole(this._console);
        return tp;
    }

    @Override
    public Reponse CreateBusyResponse()
    {
        return new ReponsePLAMAP(ReponsePLAMAP.NOK, "Plus de ressources disponibles veuillez reesayer plus tard" , null);
    }
}