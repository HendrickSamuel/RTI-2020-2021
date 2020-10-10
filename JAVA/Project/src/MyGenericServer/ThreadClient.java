package MyGenericServer;

import MyGenericServer.SourceTaches;

import java.io.IOException;

public interface ThreadClient{
    public void set_taches(SourceTaches _taches);
    public void setNom(String nom);
    public void setTraitement(String nom) throws IOException, ClassNotFoundException;
}
