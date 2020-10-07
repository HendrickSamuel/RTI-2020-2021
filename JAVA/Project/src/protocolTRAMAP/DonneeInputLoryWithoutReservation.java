package protocolTRAMAP;

import java.io.Serializable;

public class DonneeInputLoryWithoutReservation implements DonneesTRAMAP, Serializable {
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
