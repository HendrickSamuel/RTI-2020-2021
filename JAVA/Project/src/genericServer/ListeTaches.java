package genericServer;

import java.util.ArrayList;

public class ListeTaches implements SourceTaches
{
    private ArrayList<Runnable> _listeTaches;
    private int _waitingThreads = 0;
    public ListeTaches()
    {
        _listeTaches = new ArrayList<>();
    }

    @Override
    public synchronized Runnable getTache() throws InterruptedException {
        _waitingThreads++;
        System.out.println("<Waiting> " + _waitingThreads + " Threads en attente de travail");
        while(!areMoreTaches()) wait();
        _waitingThreads--;
        System.out.println("<Waiting> " + _waitingThreads + " Threads en attente de travail");
        return _listeTaches.remove(0);
    }

    @Override
    public synchronized boolean areMoreTaches() {
        return !_listeTaches.isEmpty();
    }

    @Override
    public synchronized void addTache(Runnable r) {
        _listeTaches.add(r);
        notify();
    }
}
