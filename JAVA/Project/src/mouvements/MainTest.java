package mouvements;

public class MainTest {
    public static void main (String[] args){
        //MysqlConnector connector = new MysqlConnector("newuser","",  "BD_MOUVEMENTS");
        int _nbClientsMax = 6;
        int port1 = 50001;
        int port2 = 50002;

        Tache_Mouvements tm = new Tache_Mouvements();

        ThreadEcoute te1 = new ThreadEcoute(port1, tm);
        ThreadEcoute te2 = new ThreadEcoute(port2, tm);

        te1.start();
        te2.start();

        for(int i = 0; i < _nbClientsMax; i++)
        {
            ThreadMetier threadm = new ThreadMetier(i, tm);
            threadm.start();
        }
    }
}
