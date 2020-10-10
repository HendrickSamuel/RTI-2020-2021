/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 10/10/2020             */
/***********************************************************/

package Tests;
        import java.io.*;
        import java.util.*;

public class MyProperties
{
    /********************************/
    /*           Variables          */
    /********************************/
    private Properties _properties;
    private String _fileName;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public MyProperties(String fileName)
    {
        setProperty(new Properties());
        setFileName(fileName);
        Load();
    }


    /********************************/
    /*           Setters            */
    /********************************/
    public void setProperty(Properties prop)
    {
        _properties = prop;
    }

    public void setFileName(String fileName)
    {
        _fileName = fileName;
    }


    /********************************/
    /*           Getters            */
    /********************************/
    public Properties getPropertY()
    {
        return _properties;
    }

    public String getFileName()
    {
        return _fileName;
    }

    public String getFilePath()
    {
        String sep = System.getProperty("file.separator");
        String rep = System.getProperty("user.dir");

        return rep+sep;
    }


    /********************************/
    /*          Méthodes            */
    /********************************/
    public void Save()
    {
        try(OutputStream output = new FileOutputStream(getFilePath()+getFileName()))
        {
            getPropertY().store(output,null);
        }
        catch(IOException io)
        {
            System.out.println("Erreur de sauvegarder "+io.getMessage());
        }
    }

    public void Load()
    {
        try(InputStream input = new FileInputStream(getFilePath()+getFileName()))
        {
            getPropertY().load(input);
        }
        catch(IOException io)
        {
            System.out.println("Un probleme est survenu lors du chargement du fichier : "+io.getMessage());
            getPropertY().setProperty("PORT","50005");
            this.Save();
        }
    }

    public String getContent(String prop)
    {
        if(_properties.containsKey(prop))
            return _properties.getProperty(prop);
        else
            return null;
    }
    /*------------------------------------------*/
    public void setPropertie(String prop, String val)
    {
        _properties.setProperty(prop, val);
    }

}






