package protocolTRAMAP;

import genericRequest.DonneeRequete;

import java.io.Serializable;

public class DonneeInputLoryWithoutReservation implements DonneeRequete, Serializable {
    private String idContainer;

    public DonneeInputLoryWithoutReservation(String idContainer) {
        this.idContainer = idContainer;
    }

    public String getIdContainer() {
        return idContainer;
    }

    public void setIdContainer(String idContainer) {
        this.idContainer = idContainer;
    }
}
