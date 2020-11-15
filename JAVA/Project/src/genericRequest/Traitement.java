/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package genericRequest;

import MyGenericServer.Client;
import MyGenericServer.ConsoleServeur;
import lib.BeanDBAcces.DataSource;

import java.io.Serializable;
import java.net.Socket;

public interface Traitement
{
    public Reponse traiteRequete(DonneeRequete Requete, Client client) throws ClassCastException;
    public void AfficheTraitement(String message);
    public void setConsole(ConsoleServeur cs);
}
