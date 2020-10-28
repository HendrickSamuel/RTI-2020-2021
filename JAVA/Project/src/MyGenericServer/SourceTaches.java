/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package MyGenericServer;

import java.net.Socket;

public interface SourceTaches
{
    public Socket getTache() throws InterruptedException;
    public boolean areMoreTaches();
    public boolean addTache(Socket r);
}
