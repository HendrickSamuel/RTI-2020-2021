package MyGenericServer;

import MyGenericServer.SourceTaches;

import java.net.Socket;

public class ThreadClientDeconnecte extends Thread implements ThreadClient{

    private SourceTaches _taches;
    private String nom;
    private Socket tacheEnCours;

    public ThreadClientDeconnecte()
    {
        System.out.println("DEMARRAGE ThreadClientDeconnecte");
    }

    @Override
    public void run()
    {
        System.out.println("Je suis :" + nom + " et je demarre en tant que Thread");
    }

    @Override
    public void set_taches(SourceTaches _taches) {
        this._taches = _taches;
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public void setTraitement(String nom) {

    }
}
