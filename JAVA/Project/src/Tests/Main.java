package Tests;

import MyGenericServer.ThreadServer;
import MyGenericServer.ListeTaches;

public class Main {

    public static void main (String[] args){
        MyProperties mp = new MyProperties("./Serveur_Mouvement.conf");
        int port = Integer.parseInt(mp.getContent("PORT"));
        System.out.println(port);
        ListeTaches lt = new ListeTaches();
        ThreadServer ts = new ThreadServer(port, lt, null, true,"protocolTRAMAP.TraitementTRAMAP");
        ts.start();
    }
}
