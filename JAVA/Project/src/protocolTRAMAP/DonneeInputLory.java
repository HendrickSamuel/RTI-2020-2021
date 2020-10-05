package protocolTRAMAP;

import java.io.Serializable;

public class DonneeInputLory implements DonneesTRAMAP, Serializable {
    private boolean reservation;
    private String numeroReservation;
    private String idContainer;

    public DonneeInputLory(boolean reservation, String numeroReservation, String idContainer) {
        this.reservation = reservation;
        this.numeroReservation = numeroReservation;
        this.idContainer = idContainer;
    }

    public String getNumeroReservation() {
        return numeroReservation;
    }

    public void setNumeroReservation(String numeroReservation) {
        this.numeroReservation = numeroReservation;
    }

    public String getIdContainer() {
        return idContainer;
    }

    public void setIdContainer(String idContainer) {
        this.idContainer = idContainer;
    }
}
