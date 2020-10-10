/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package MyGenericServer;

import MyGenericServer.SourceTaches;

import java.io.IOException;

public interface ThreadClient
{
    public void set_taches(SourceTaches _taches);
    public void setNom(String nom);
    public void setTraitement(String nom) throws IOException, ClassNotFoundException;
}
