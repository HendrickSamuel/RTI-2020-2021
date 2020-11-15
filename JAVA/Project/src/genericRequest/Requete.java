/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package genericRequest;

import MyGenericServer.ConsoleServeur;

import java.io.Serializable;
import java.net.Socket;

public interface Requete extends Serializable
{
    public DonneeRequete getChargeUtile();
}
