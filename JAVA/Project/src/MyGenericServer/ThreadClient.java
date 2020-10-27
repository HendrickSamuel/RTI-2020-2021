/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package MyGenericServer;

import genericRequest.Traitement;
import lib.BeanDBAcces.DataSource;

import java.io.IOException;

public abstract class ThreadClient extends Thread {
    protected ConsoleServeur _console;
    protected DataSource _dataSource;
    protected SourceTaches _taches;
    protected Traitement _traitement;
    protected boolean _javaObjectsCommunication = true;

    public boolean is_javaObjectsCommunication() {
        return _javaObjectsCommunication;
    }

    public void set_javaObjectsCommunication(boolean _javaObjectsCommunication) {
        this._javaObjectsCommunication = _javaObjectsCommunication;
    }

    protected int index;

    public void setIndex(int index) {
        this.index = index;
    }

    public void set_taches(SourceTaches _taches)
    {
        this._taches = _taches;
    }

    public void setTraitement(Traitement traitement)
    {
        this._traitement = traitement;
    };

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
