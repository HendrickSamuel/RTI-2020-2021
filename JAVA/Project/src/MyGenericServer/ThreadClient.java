/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package MyGenericServer;

import lib.BeanDBAcces.DataSource;

import java.io.IOException;

public abstract class ThreadClient extends Thread {
    protected ConsoleServeur _console;
    protected DataSource _dataSource;

    void set_taches(SourceTaches _taches){};
    public void setNom(String nom){};
    public void setTraitement(String nom, DataSource ds) throws IOException, ClassNotFoundException{};

    public ConsoleServeur get_console() {
        return _console;
    }

    public void set_console(ConsoleServeur _console) {
        this._console = _console;
    }

    public DataSource get_dataSource() {
        return _dataSource;
    }
}
