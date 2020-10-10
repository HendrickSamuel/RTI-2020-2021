package protocolTRAMAP;

import genericRequest.DonneeRequete;

import java.io.Serializable;
import java.util.Date;

public class DonneeListOperations implements DonneeRequete, Serializable {
    private Date dateDebut;
    private Date dateFin;
    private String nomSociete;
    private String nomDestination;

    public DonneeListOperations(Date dateDebut, Date dateFin, String nomSociete, String nomDestination) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nomSociete = nomSociete;
        this.nomDestination = nomDestination;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getNomSociete() {
        return nomSociete;
    }

    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }

    public String getNomDestination() {
        return nomDestination;
    }

    public void setNomDestination(String nomDestination) {
        this.nomDestination = nomDestination;
    }
}
