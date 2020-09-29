package mouvements;

import lib.BeanDBAcces.*;

public class MainTest {
    public static void main (String[] args){
        //MysqlConnector connector = new MysqlConnector("newuser","",  "BD_MOUVEMENTS");
        Serveur_Mouvements sm = new Serveur_Mouvements();
        sm.start();
    }
}
