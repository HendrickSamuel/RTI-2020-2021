/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package genericRequest;

import java.net.Socket;

public interface Traitement
{
    public Reponse traiteRequete(DonneeRequete Requete) throws ClassCastException;
}
