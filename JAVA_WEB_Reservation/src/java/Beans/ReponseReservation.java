//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 10/11/2020

package Beans;

public class ReponseReservation {
    
    private boolean resultat;
    private String message;
    private String dateReservation;
    private String destination;
    private String numerReservation;
    private int x;
    private int y;

    public ReponseReservation() {
    }

    public boolean isResultat() {
        return resultat;
    }

    public void setResultat(boolean resultat) {
        this.resultat = resultat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getNumerReservation() {
        return numerReservation;
    }

    public void setNumerReservation(String numerReservation) {
        this.numerReservation = numerReservation;
    }
    
}
