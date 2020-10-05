package protocolTRAMAP;

import genericRequest.Reponse;
import java.io.Serializable;
import java.util.Properties;

public class ReponseTRAMAP implements Reponse, Serializable {
    public static int BAD_DATA = 401;
    public static int LOGIN_OK = 201;
    private int codeRetour;
    private String message;
    private DonneesTRAMAP chargeUtile;

    public ReponseTRAMAP(int c, DonneesTRAMAP chu, String msg)
    {
        codeRetour = c;
        setChargeUtile(chu);
        message = msg;
    }

    public int getCode() { return codeRetour; }
    public DonneesTRAMAP getChargeUtile() { return chargeUtile; }
    public void setChargeUtile(DonneesTRAMAP chargeUtile) { this.chargeUtile = chargeUtile; }
}
