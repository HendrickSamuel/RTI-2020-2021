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
import protocol.PIDEP.DonneeGetStatInferANOVA;
import protocol.PIDEP.DonneeGetStatInferTestHomog;
import protocol.PIDEP.EchANOVA;

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
    public RServe(RConnection _c)
    {
        this._c = _c;
    }

    public RServe(String host)
    {
        connectionRserve(host);
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
    private void connectionRserve(String host)
    {
        try
        {
            setRconnection(new RConnection(host));
            System.out.println("Connexion au serveur R reussie !");
        }
        catch (RserveException e)
        {
            e.printStackTrace();
            System.out.println("Erreur de connexion au serveur R !");
        }
    }

    public void RserveClose()
    {
        System.out.println("Déconnexion du serveur R.");
        getRconnection().close();
    }


    public synchronized double getMoyenneVector(Vector vec)
    {
        double moyenne;

        try
        {
            //Création de l'échantillon
            getRconnection().voidEval("vec <- c()");

            for(int i = 0 ; i < vec.size() ; i++)
            {
                int j = i + 1;
                getRconnection().voidEval("vec["+j+"] <- "+vec.get(i));
            }

            //Calcul de la moyenne
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

    public synchronized double getMedianeVector(Vector vec)
    {
        double mediane;

        try
        {
            //Création de l'échantillon
            getRconnection().voidEval("vec <- c()");

            for(int i = 0 ; i < vec.size() ; i++)
            {
                int j = i + 1;
                getRconnection().voidEval("vec["+j+"] <- "+vec.get(i));
            }

            //Calcul de la médiane
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

    public synchronized double getModeVector(Vector vec)
    {
        double mode;

        try
        {
            //Set la fonction du mode
            getRconnection().voidEval("getmode <- function(v) {\n" +
                                            "  uniqv <- unique(v)\n" +
                                            "  uniqv[which.max(tabulate(match(v, uniqv)))]\n" +
                                            "}");

            //Création de l'échantillon
            getRconnection().voidEval("vec <- c()");

            for(int i = 0 ; i < vec.size() ; i++)
            {
                int j = i + 1;
                getRconnection().voidEval("vec["+j+"] <- "+vec.get(i));
            }

            //Calcul du mode
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

    public synchronized double getEcartTypeVector(Vector vec)
    {
        double ecartType;

        try
        {
            //Set la fonction de l'écart-type
            getRconnection().voidEval("getEcartType <- function(v) {\n" +
                                            "  sqrt((length(v)-1)/length(v))*sd(v)\n" +
                                            "}");

            //Création de l'échantillon
            getRconnection().voidEval("vec <- c()");

            for(int i = 0 ; i < vec.size() ; i++)
            {
                int j = i + 1;
                getRconnection().voidEval("vec["+j+"] <- "+vec.get(i));
            }

            //Calcul de l'écart-type
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

    public synchronized double getTestConfVector(Vector vec)
    {
        double p_value;

        try
        {
            //Création de l'échantillon
            getRconnection().voidEval("vec <- c()");

            for(int i = 0 ; i < vec.size() ; i++)
            {
                int j = i + 1;
                getRconnection().voidEval("vec["+j+"] <- "+vec.get(i));
            }

            //Calcul de la p-value
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

    public synchronized void getTestHomogVector(Vector ech1, Vector ech2, DonneeGetStatInferTestHomog chargeUtile)
    {
        try
        {
            //Création des échantillon
            getRconnection().voidEval("ech1 <- c()");
            getRconnection().voidEval("ech2 <- c()");

            for(int i = 0 ; i < ech1.size() ; i++)
            {
                int j = i + 1;
                getRconnection().voidEval("ech1["+j+"] <- "+ech1.get(i));
                getRconnection().voidEval("ech2["+j+"] <- "+ech2.get(i));
            }

            //Test d'homogénéité variance
            getRconnection().voidEval("result <- var.test(ech1, ech2)");
            chargeUtile.setResultVariance(getRconnection().eval("result$statistic").asDouble());
            chargeUtile.setVarMin(getRconnection().eval("qf(0.005,length(ech1)-1,length(ech2)-1)").asDouble());
            chargeUtile.setVarMax(getRconnection().eval("qf(0.995,length(ech1)-1,length(ech2)-1)").asDouble());

            //Homogénéité de moyenne
            if(chargeUtile.getVarMin() < chargeUtile.getResultVariance() && chargeUtile.getVarMax() > chargeUtile.getResultVariance())
            {
                getRconnection().voidEval("res = t.test(ech1, ech2, alternative = \"two.sided\", paired = FALSE, var.equal = TRUE)");
            }
            else //sinon Test de Welch
            {
                getRconnection().voidEval("res = t.test(ech1, ech2, alternative = \"two.sided\", paired = FALSE, var.equal = FALSE)");
            }

            chargeUtile.setP_value(getRconnection().eval("res$p.value").asDouble());
        }
        catch (RserveException | REXPMismatchException e)
        {
            e.printStackTrace();
        }
    }

    public synchronized void getTestANOVAVector(Vector<EchANOVA> echantillons, DonneeGetStatInferANOVA chargeUtile)
    {
        try
        {
            String destination = "Destination <- c(\"" + echantillons.get(0).getVille() + "\"";
            String jours = "Jours <- c(" + echantillons.get(0).getJours();

            //Préparation pour le Data.frame
            for( int i = 1 ; i < echantillons.size() ; i++)
            {
                destination = destination + ", \"" + echantillons.get(i).getVille() + "\"";
                jours = jours + ", " + echantillons.get(i).getJours();
            }

            destination = destination + ")";
            jours = jours + ")";

            getRconnection().voidEval(destination);
            getRconnection().voidEval(jours);

            //Création du data.frame
            getRconnection().voidEval("DataFrame <- data.frame(Destination, Jours)");
            getRconnection().voidEval("modele <- lm(DataFrame$Jours~DataFrame$Destination)");
            getRconnection().voidEval("resanova <- anova(modele)");

            chargeUtile.set_pvalue(getRconnection().eval("pvalue <- resanova$`Pr(>F)`[1]").asDouble());
        }
        catch (RserveException | REXPMismatchException e)
        {
            e.printStackTrace();
        }
    }
}