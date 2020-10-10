package protocolTRAMAP;

import genericRequest.DonneeRequete;
import genericRequest.Requete;

import java.io.Serializable;


public class RequeteTRAMAP implements Requete, Serializable
{
    private DonneeRequete chargeUtile;

    public RequeteTRAMAP(DonneeRequete chu)
    {
        chargeUtile = chu;

    }

    @Override
    public DonneeRequete getChargeUtile() {
        return chargeUtile;
    }
}
