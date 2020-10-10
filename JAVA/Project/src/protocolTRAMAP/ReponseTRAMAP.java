package protocolTRAMAP;

import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import java.io.Serializable;
import java.util.Properties;

public class ReponseTRAMAP implements Reponse, Serializable {
    public static int OK = 200;
    public static int LOGIN_OK = 201;
    public static int BAD_DATA = 401;
    public static int REQUEST_NOT_FOUND = 404;
    private int codeRetour;
    private String message;
    private DonneeRequete chargeUtile;

    public ReponseTRAMAP(int c, DonneeRequete chu, String msg)
    {
        codeRetour = c;
        setChargeUtile(chu);
        message = msg;
    }

    public int getCode() { return codeRetour; }
    public DonneeRequete getChargeUtile() { return chargeUtile; }
    public void setChargeUtile(DonneeRequete chargeUtile) { this.chargeUtile = chargeUtile; }
}
