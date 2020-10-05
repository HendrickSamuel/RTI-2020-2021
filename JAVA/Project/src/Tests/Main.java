package Tests;

import genericServer.ListeTaches;
import genericServer.ThreadServeur;

public class Main {

    public static void main (String[] args){
        ListeTaches lt = new ListeTaches();
        ThreadServeur ts = new ThreadServeur(50001, lt, null);
        ts.start();
    }
}
