package MyGenericServer;

import java.net.Socket;

public interface SourceTaches {
    public Socket getTache() throws InterruptedException;
    public boolean areMoreTaches();
    public void addTache(Socket r);
}
