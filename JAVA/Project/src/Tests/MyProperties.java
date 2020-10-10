/***********************************************************/
/*Auteurs : DELAVAL Kevin & HENDRICK Samuel                */
/*Groupe : 2203                                            */
/*Application : InpresHarbour                              */
/*Date de la dernière mise à jour : 18/05/2020             */
/***********************************************************/

package Tests;
        import java.io.*;
        import java.util.*;

public class MyProperties {
    /**************************/
    /*                        */
    /*   VARIABLES MEMBRES    */
    /*                        */
    /**************************/

    private Properties _properties;
    private String _fileName;

    /**************************/
    /*                        */
    /*      CONSTRUCTEURS     */
    /*                        */
    /**************************/

    public MyProperties(String fileName)
    {
        setProperty(new Properties());
        setFileName(fileName);
        Load();
    }

    /**************************/
    /*                        */
    /*         SETTERS        */
    /*                        */
    /**************************/

    public void setProperty(Properties prop)
    {
        _properties = prop;
    }

    public void setFileName(String fileName)
    {
        _fileName = fileName;
    }

    /**************************/
    /*                        */
    /*         GETTERS        */
    /*                        */
    /**************************/

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

    /**************************/
    /*                        */
    /*        METHODES        */
    /*                        */
    /**************************/

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






