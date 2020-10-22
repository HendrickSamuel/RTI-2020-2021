//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 22/10/2020

package MyGenericServer;

import genericRequest.Traitement;

import java.beans.Beans;
import java.io.IOException;
import java.util.LinkedList;

public abstract class ServeurGenerique
{

    /********************************/
    /*           Variables          */
    /********************************/
    protected int _port;
    protected String _clientType;
    protected int _nbMaxConnections = 3;
    protected SourceTaches _sourceTaches;
    protected ConsoleServeur _console;

    protected LinkedList<ThreadClient> listeThreadsEnfants;
    protected ThreadServer _threadServer;

    /********************************/
    /*         Constructeurs        */
    /********************************/

    public ServeurGenerique(int port, boolean connecte, int NbThreads, ConsoleServeur cs)
    {
        this._console = cs;
        this._port = port;
        this.listeThreadsEnfants = new LinkedList<>();

        if(connecte)
            _clientType = "MyGenericServer.ThreadClientConnecte";
        else
            _clientType = "MyGenericServer.ThreadClientDeconnecte";

        this._nbMaxConnections = NbThreads;

        this._sourceTaches = new ListeTaches();
    }

    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/

    /********************************/
    /*            Methodes          */
    /********************************/

    public void StartServeur()
    {
        _threadServer = new ThreadServer(this._port, this._sourceTaches, this._console);
        CreationPoolThreads(this._sourceTaches);

        _threadServer.start();

    }

    public void StopServeur()
    {

    }

    public void CreationPoolThreads(SourceTaches sourceTaches)
    {
        for(int i = 0; i < _nbMaxConnections; i++)
        {
            try
            {
                ThreadClient o = (ThreadClient) Beans.instantiate(null, _clientType);

                o.set_taches(sourceTaches);
                o.setIndex(i);
                o.setTraitement(CreationTraitement());
                o.set_console(this._console);

                listeThreadsEnfants.add(o);
                o.start();
            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    public abstract Traitement CreationTraitement();

}
