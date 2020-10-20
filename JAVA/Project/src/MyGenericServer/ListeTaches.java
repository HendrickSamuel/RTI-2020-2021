/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package MyGenericServer;

import java.net.Socket;
import java.util.ArrayList;

public class ListeTaches implements SourceTaches
{
    /********************************/
    /*           Variables          */
    /********************************/
    private ArrayList<Socket> _listeTaches;
    private int _waitingThreads = 0;

    public ListeTaches()
    {
        _listeTaches = new ArrayList<>();
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public synchronized Socket getTache() throws InterruptedException
    {
        _waitingThreads++;
        System.out.println("<Waiting> " + _waitingThreads + " Threads en attente de travail");
        while(!areMoreTaches()) wait();
        _waitingThreads--;
        System.out.println("<Waiting> " + _waitingThreads + " Threads en attente de travail");
        return _listeTaches.remove(0);
    }

    @Override
    public synchronized boolean areMoreTaches()
    {
        return !_listeTaches.isEmpty();
    }

    @Override
    public synchronized void addTache(Socket r)
    {
        _listeTaches.add(r);
        notify();
    }
}
