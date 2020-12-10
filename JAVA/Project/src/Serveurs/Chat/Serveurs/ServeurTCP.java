//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 08/12/2020

package Serveurs.Chat.Serveurs;

import MyGenericServer.ConsoleServeur;
import MyGenericServer.ServeurGenerique;
import genericRequest.Reponse;
import genericRequest.Traitement;
import lib.BeanDBAcces.BDCompta;
import protocol.PFMCOP.ReponsePFMCOP;
import protocol.PFMCOP.TraitementPFMCOP;
import security.SecurityHelper;


public class ServeurTCP extends ServeurGenerique
{
    /********************************/
    /*           Variables          */
    /********************************/
    private BDCompta _bdCompta;
    private SecurityHelper securityHelper;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public ServeurTCP(int port, boolean connecte, int NbThreads, BDCompta _bdCompta, ConsoleServeur cs, boolean isJavaCommunication)
    {
        super(port, connecte, NbThreads, cs, isJavaCommunication);
        this._bdCompta = _bdCompta;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public BDCompta getBDCompta()
    {
        return _bdCompta;
    }

    public SecurityHelper getSecurityHelper()
    {
        return securityHelper;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    public void setBDCompta(BDCompta bd)
    {
        _bdCompta = bd;
    }

    public void setSecurityHelper(SecurityHelper securityHelper)
    {
        this.securityHelper = securityHelper;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public Traitement CreationTraitement()
    {
        TraitementPFMCOP tp = new TraitementPFMCOP();
        tp.setConsole(this._console);
        tp.set_bd(this._bdCompta);
        tp.set_sh(this.securityHelper);
        return tp;
    }

    @Override
    public Reponse CreateBusyResponse()
    {
        return new ReponsePFMCOP(ReponsePFMCOP.NOK, "Plus de ressources disponibles veuillez reesayer plus tard" , null);
    }
}