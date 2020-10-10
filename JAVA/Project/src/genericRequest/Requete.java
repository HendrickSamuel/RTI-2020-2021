package genericRequest;

import genericServer.ConsoleServeur;

import java.net.Socket;

public interface Requete {
    public Runnable createRunnable(Socket s, ConsoleServeur cs);
}
