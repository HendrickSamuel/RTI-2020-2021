//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 04/11/2020

package genericRequest;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import java.util.Vector;

public class RServe
{
    /********************************/
    /*           Variables          */
    /********************************/
    private RConnection _c;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public RServe()
    {

    }

    public RServe(RConnection _c)
    {
        this._c = _c;
    }


    /********************************/
    /*            Getters           */
    /********************************/
    private RConnection getRconnection()
    {
        return _c;
    }


    /********************************/
    /*            Setters           */
    /********************************/
    private void setRconnection(RConnection c)
    {
        _c = c;
    }


    /********************************/
    /*            Methodes          */
    /********************************/
    public void connectionRserve(String host)
    {
        try
        {
            setRconnection(new RConnection(host));
            System.out.println("Connection au serveur R reussie !");
        }
        catch (RserveException e)
        {
            e.printStackTrace();
            System.out.println("Erreur de connection au serveur R !");
        }
    }

    public void RserveClose()
    {
        System.out.println("Déconnection du serveur R.");
        getRconnection().close();
    }

    public double getMoyenneVector(Vector vec)
    {
        double moyenne;

        try
        {
            getRconnection().voidEval("vec <- c()");

            for(int i = 0 ; i < vec.size() ; i++)
            {
                int j = i + 1;
                getRconnection().voidEval("vec["+j+"] <- "+vec.get(i));
            }

            REXP result = getRconnection().eval("mean(vec)");
            moyenne = result.asDouble();
            return moyenne;
        }
        catch (RserveException | REXPMismatchException e)
        {
            e.printStackTrace();
        }

        return Double.parseDouble(null);
    }

    public double getMedianeVector(Vector vec)
    {
        double mediane;

        try
        {
            getRconnection().voidEval("vec <- c()");

            for(int i = 0 ; i < vec.size() ; i++)
            {
                int j = i + 1;
                getRconnection().voidEval("vec["+j+"] <- "+vec.get(i));
            }

            REXP result = getRconnection().eval("median(vec)");
            mediane = result.asDouble();
            return mediane;
        }
        catch (RserveException | REXPMismatchException e)
        {
            e.printStackTrace();
        }

        return Double.parseDouble(null);
    }

    public double getModeVector(Vector vec)
    {
        double mode;

        try
        {
            getRconnection().voidEval("getmode <- function(v) {\n" +
                                            "  uniqv <- unique(v)\n" +
                                            "  uniqv[which.max(tabulate(match(v, uniqv)))]\n" +
                                            "}");

            getRconnection().voidEval("vec <- c()");

            for(int i = 0 ; i < vec.size() ; i++)
            {
                int j = i + 1;
                getRconnection().voidEval("vec["+j+"] <- "+vec.get(i));
            }

            REXP result = getRconnection().eval("getmode(vec)");
            mode = result.asDouble();
            return mode;
        }
        catch (RserveException | REXPMismatchException e)
        {
            e.printStackTrace();
        }

        return Double.parseDouble(null);
    }

    public double getEcartTypeVector(Vector vec)
    {
        double ecartType;

        try
        {
            getRconnection().voidEval("getEcartType <- function(v) {\n" +
                                            "  sqrt((length(v)-1)/length(v))*sd(v)\n" +
                                            "}");

            getRconnection().voidEval("vec <- c()");

            for(int i = 0 ; i < vec.size() ; i++)
            {
                int j = i + 1;
                getRconnection().voidEval("vec["+j+"] <- "+vec.get(i));
            }

            REXP result = getRconnection().eval("getEcartType(vec)");
            ecartType = result.asDouble();
            return ecartType;
        }
        catch (RserveException | REXPMismatchException e)
        {
            e.printStackTrace();
        }

        return Double.parseDouble(null);
    }

    public double getTestConfVector(Vector vec)
    {
        double p_value;

        try
        {
            getRconnection().voidEval("vec <- c()");

            for(int i = 0 ; i < vec.size() ; i++)
            {
                int j = i + 1;
                getRconnection().voidEval("vec["+j+"] <- "+vec.get(i));
            }

            getRconnection().voidEval("value <- t.test(vec, mu = 10, alternative = \"two.sided\")");
            REXP result = getRconnection().eval("value$p.value");
            p_value = result.asDouble();
            return p_value;
        }
        catch (RserveException | REXPMismatchException e)
        {
            e.printStackTrace();
        }

        return Double.parseDouble(null);
    }
}