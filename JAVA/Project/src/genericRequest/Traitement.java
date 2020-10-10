package genericRequest;

import java.net.Socket;

public interface Traitement {
    public Reponse traiteRequete(DonneeRequete Requete) throws ClassCastException;
}
