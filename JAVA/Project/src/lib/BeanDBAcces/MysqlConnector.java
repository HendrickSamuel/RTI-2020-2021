/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package lib.BeanDBAcces;

import java.sql.*;
import java.util.TimeZone;

public class MysqlConnector implements DataSource
{
    /********************************/
    /*           Variables          */
    /********************************/
    protected Connection _con;
    protected String database;
    protected String username;
    protected String password;

    /********************************/
    /*         Constructeurs        */
    /********************************/
    public MysqlConnector() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    public MysqlConnector(String username, String password, String database) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        setDatabase(database);
        setUsername(username);
        setPassword(password);
        Init();
    }

    /********************************/
    /*         Getters              */
    /********************************/

    /********************************/
    /*         Setters              */
    /********************************/
    public void setDatabase(String database) {
        this.database = database;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    /********************************/
    /*         Methodes             */
    /********************************/

    public void Init() throws SQLException {
        _con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+database+"?serverTimezone="+ TimeZone.getDefault().getID(), username, password);
    }

    public synchronized PreparedStatement getPreparedStatement(String request) throws SQLException {
        return _con.prepareStatement(request, ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
    }
 
    public synchronized void UpdateResult(ResultSet rs) throws SQLException {
        rs.updateRow();
    }

    public synchronized ResultSet ExecuteQuery(PreparedStatement statement) throws SQLException {
        return statement.executeQuery();
    }

    public synchronized boolean Execute(PreparedStatement statement) throws SQLException {
        return statement.execute();
    }

    public synchronized Statement CreateUpdatableStatement() throws SQLException {
        return _con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
    }

    public synchronized ResultSet ExecuteQuery(Statement statement, String query) throws SQLException {
        return statement.executeQuery(query);
    }


}
