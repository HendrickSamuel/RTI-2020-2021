/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package protocol.TRAMAP;

import genericRequest.DonneeRequete;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DonneeListOperations implements DonneeRequete, Serializable
{
    private static final long serialVersionUID = -8131406145842552497L;
    /********************************/
    /*           Variables          */
    /********************************/
    private Date dateDebut;
    private Date dateFin;
    private String nomSociete;
    private String nomDestination;

    private List<Operation> operations;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public DonneeListOperations(Date dateDebut, Date dateFin, String nomSociete, String nomDestination)
    {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nomSociete = nomSociete;
        this.nomDestination = nomDestination;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    public Date getDateDebut()
    {
        return dateDebut;
    }

    public Date getDateFin()
    {
        return dateFin;
    }

    public String getNomSociete()
    {
        return nomSociete;
    }

    public String getNomDestination()
    {
        return nomDestination;
    }

    public List<Operation> getOperations()
    {
        return operations;
    }

    /********************************/
    /*            Setters           */
    /********************************/
    public void setDateDebut(Date dateDebut)
    {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(Date dateFin)
    {
        this.dateFin = dateFin;
    }

    public void setNomSociete(String nomSociete)
    {
        this.nomSociete = nomSociete;
    }

    public void setNomDestination(String nomDestination)
    {
        this.nomDestination = nomDestination;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    /********************************/
    /*            Methodes          */
    /********************************/
    @Override
    public void setFiledsFromString(String fields) {

    }
}
