/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package genericRequest;

import java.io.Serializable;

//Moyen de regrouper toutes les classes l'implementant
public interface DonneeRequete extends Serializable
{
    public void setFiledsFromString(String fields);
}
