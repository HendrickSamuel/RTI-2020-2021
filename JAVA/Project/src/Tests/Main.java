package Tests;

import genericServer.ListeTaches;
import genericServer.ThreadServeur;

public class Main {

    public static void main (String[] args){
        MyProperties mp = new MyProperties("./Serveur_Mouvement.conf");
        System.out.println(mp.getContent("PORT"));
        ListeTaches lt = new ListeTaches();
        ThreadServeur ts = new ThreadServeur(50001, lt, null);
        ts.start();
    }
}
