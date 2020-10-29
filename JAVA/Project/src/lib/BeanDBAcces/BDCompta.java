//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 24/10/2020

package lib.BeanDBAcces;

import java.sql.SQLException;

public class BDCompta extends MysqlConnector
{
    /********************************/
    /*           Variables          */
    /********************************/

    /********************************/
    /*         Constructeurs        */
    /********************************/

    public BDCompta(String username, String password, String database) throws ClassNotFoundException, SQLException
    {
        super(username, password, database);
    }

    /********************************/
    /*            Getters           */
    /********************************/

    /********************************/
    /*            Setters           */
    /********************************/

    /********************************/
    /*            Methodes          */
    /********************************/

}
