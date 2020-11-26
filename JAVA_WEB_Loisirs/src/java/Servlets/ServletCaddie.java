//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 21/11/2020

package Servlets;

import java.sql.ResultSet;
import Beans.ReponseCaddie;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import lib.BeanDBAcces.MysqlConnector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ServletCaddie extends HttpServlet 
{
    private MysqlConnector connection;
    private String username;
    private String id;
    
   @Override
    public void init(ServletConfig config) throws ServletException 
    { 
        super.init(config);
        
        try 
        {
            connection = new MysqlConnector("root","","bd_shopping");
        } 
        catch (ClassNotFoundException | SQLException ex) 
        {
            Logger.getLogger(ServletCaddie.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        HttpSession session = request.getSession(false);
        //Pour si on veux accéder à la page sans avoir de session
        if(session.getAttribute("username") == null)
        {
            response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/");
            return; 
        }
        
        //Pour effectuer des actions il faut etre login
        if(session.getAttribute("userid") == null)
        {
            ReponseCaddie rc = new ReponseCaddie();

            rc.setCode(ReponseCaddie.error);
            rc.setMessage("Il faut être connecté pour effectuer cette action, <a href=\"./index.jsp\">identifiez vous ici.</a>");
            session.setAttribute("rc", rc);
            getServletContext().getRequestDispatcher("/caddie.jsp").forward(request, response);
            return; 
        }
        
        response.setContentType("text/html"); 
        PrintWriter sortie = response.getWriter();
        
        id = (String)session.getAttribute("userid");
        username = (String)session.getAttribute("username");
        
        
        if(request.getParameter("ajouter") != null)
        {
            ajoutePanier(session, sortie, request);
            verifCaddie(sortie);
            getServletContext().getRequestDispatcher("/caddie.jsp").forward(request, response);
            return;  
        }
        else if(request.getParameter("supprimer") != null)
        {
            supprimePanier(session, sortie, request);
            verifCaddie(sortie);
            getServletContext().getRequestDispatcher("/caddie.jsp").forward(request, response);
            return;            
        }
        else if(request.getParameter("valider") != null)
        {
            response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/" + "pay.jsp");
            return;  
        }  
        
        response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/" + "caddie.jsp");
        return; 
    }

    private void ajoutePanier(HttpSession session, PrintWriter sortie, HttpServletRequest request)
    {
        try 
        {
            int quantStock;
            PreparedStatement ps = null;
            ResultSet rs = null;
            
            //on compare les quantité demandée avec celle en stock
            quantStock = quantitéArticleStock(sortie, request);
            if(quantStock < Integer.parseInt(request.getParameter("quantite")))
            {
                ReponseCaddie rc = new ReponseCaddie();
                //Alors pas assez de stock --> message d'erreur
                session.invalidate();
                rc.setCode(ReponseCaddie.error);
                rc.setMessage("L'acticle souhaité n'est plus disponnible en quantité suffisante");
                session = request.getSession(true);
                session.setAttribute("username", username);
                session.setAttribute("userid", id);
                session.setAttribute("rc", rc);
            }
            else
            {
                //mise à jour du stock si article != de "art000001" et "art000002"
                if(!(request.getParameter("article")).equals("art000001") && !(request.getParameter("article")).equals("art000002"))
                {
                    quantStock = quantStock -  Integer.parseInt(request.getParameter("quantite"));

                    ps = connection.getPreparedStatement("UPDATE article SET quantite = ? WHERE id = ?;");
                    ps.setInt(1, quantStock);
                    ps.setString(2, request.getParameter("article"));
                    connection.Execute(ps);
                }
                
                //recherche si cet article n'est pas déjà dans le caddie
                ps = connection.getPreparedStatement("SELECT * FROM caddie WHERE id_client = ? AND id_article = ? AND acheter = 0;");
                ps.setString(1, id);
                ps.setString(2, request.getParameter("article"));
                rs = connection.ExecuteQuery(ps);
                if(rs.next())
                {
                    quantStock = rs.getInt("quantite");
                    quantStock = quantStock + Integer.parseInt(request.getParameter("quantite"));
                    ps = connection.getPreparedStatement("UPDATE caddie SET quantite = ? WHERE id_client = ? AND id_article = ? AND acheter = 0;");
                    ps.setInt(1, quantStock);
                    ps.setString(2, id);
                    ps.setString(3, request.getParameter("article"));
                    connection.Execute(ps);
                }
                else
                {
                    ps = connection.getPreparedStatement("INSERT INTO caddie (id_client, id_article, quantite) VALUES (?, ?, ?);");
                    ps.setString(1, id);
                    ps.setString(2, request.getParameter("article"));
                    ps.setString(3, request.getParameter("quantite"));
                    connection.Execute(ps); 
                }
                ReponseCaddie rc = new ReponseCaddie();
                //Alors stock suffisant
                session.invalidate();
                rc.setCode(ReponseCaddie.ok);
                rc.setMessage("L'acticle a bien été ajouté au caddie");
                session = request.getSession(true);
                session.setAttribute("username", username);
                session.setAttribute("userid", id);
                session.setAttribute("rc", rc);
            }
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(ServletLogin.class.getName()).log(Level.SEVERE, null, ex);
            sortie.println("<H1>"+ ex.getMessage() +"</H1>"); 
            sortie.close(); 
        }        
    }
    
    private void supprimePanier(HttpSession session, PrintWriter sortie, HttpServletRequest request)
    {
        try 
        {
            int quantStock;
            PreparedStatement ps = null;
            ResultSet rs = null;
            
            //on va rechercher la quantité en stock
            quantStock = quantitéArticleStock(sortie, request);
            
            //on supprime du caddie
            ps = connection.getPreparedStatement("DELETE FROM caddie WHERE id_client = ? AND id_article = ? AND acheter = 0;");
            ps.setString(1, id);
            ps.setString(2, request.getParameter("article"));
            connection.Execute(ps);


            //mise à jour du stock si article != de "art000001" et "art000002"
            if(!(request.getParameter("article")).equals("art000001") && !(request.getParameter("article")).equals("art000002"))
            {
                quantStock = quantStock +  Integer.parseInt(request.getParameter("quantite"));

                ps = connection.getPreparedStatement("UPDATE article SET quantite = ? WHERE id = ?;");
                ps.setInt(1, quantStock);
                ps.setString(2, request.getParameter("article"));
                connection.Execute(ps);
            }


            ReponseCaddie rc = new ReponseCaddie();

            session.invalidate();
            rc.setCode(ReponseCaddie.ok);
            rc.setMessage("L'acticle a bien été supprimé au caddie");
            session = request.getSession(true);
            session.setAttribute("username", username);
            session.setAttribute("userid", id);
            session.setAttribute("rc", rc);

        }
        catch (SQLException ex) 
        {
            Logger.getLogger(ServletLogin.class.getName()).log(Level.SEVERE, null, ex);
            sortie.println("<H1>"+ ex.getMessage() +"</H1>"); 
            sortie.close(); 
        }     
    }
    
    private int quantitéArticleStock(PrintWriter sortie, HttpServletRequest request)
    {
        try 
        {
            int quant = 0;
            PreparedStatement ps = null;
            ResultSet rs = null;
            
            //recherche des quantite en stock
            if((request.getParameter("article")).equals("art000001") || (request.getParameter("article")).equals("art000002"))
            {
                if((request.getParameter("article")).equals("art000001"))
                {
                    return 500;
                }
                else
                {
                    ps = connection.getPreparedStatement("SELECT quantite FROM caddie WHERE id_article = \"art000002\" AND DATE(date_res) = DATE(CURDATE());");
                    rs = connection.ExecuteQuery(ps);
                    while(rs.next())
                    {
                        quant = quant + rs.getInt("quantite");
                    }
                    return (20 - quant);
                }
            }
            else
            {
                ps = connection.getPreparedStatement("SELECT quantite FROM article WHERE id = ? ;");
                ps.setString(1, request.getParameter("article"));
                rs = connection.ExecuteQuery(ps);
                rs.next();
                return rs.getInt("quantite");
            }
                
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(ServletCaddie.class.getName()).log(Level.SEVERE, null, ex);
            sortie.println("<H1>"+ ex.getMessage() +"</H1>"); 
            sortie.close(); 
        } 
        return 0;
    }
    
    private void verifCaddie(PrintWriter sortie)
    {

    try 
        {
            PreparedStatement ps = connection.getPreparedStatement("SELECT id_client, id_article, quantite, TIMESTAMPDIFF(MINUTE,date_res,current_timestamp()) as duree FROM caddie WHERE acheter = 0;");
            ResultSet rs = connection.ExecuteQuery(ps);
            //Si l'utilisateur a un caddie on le met a jour
            if(rs.next())
            {
                do
                {
                    if(rs.getInt("duree") >= 30)
                    {
                        if(!rs.getString("id_article").equals("art000001") && !rs.getString("id_article").equals("art000002"))
                        {
                            ps = connection.getPreparedStatement("SELECT quantite FROM article WHERE id = ?;");
                            ps.setString(1, rs.getString("id_article"));
                            ResultSet rsUp = connection.ExecuteQuery(ps);

                            rsUp.next();
                            int quantite = rsUp.getInt("quantite")+rs.getInt("quantite");

                            ps = connection.getPreparedStatement("UPDATE article SET quantite = ? WHERE id = ?;");
                            ps.setString(1, String.valueOf(quantite));
                            ps.setString(2, rs.getString("id_article"));
                            connection.Execute(ps);
                        }

                        ps = connection.getPreparedStatement("DELETE FROM caddie WHERE id_client = ? AND id_article = ? AND acheter = 0;");
                        ps.setString(1, rs.getString("id_client"));
                        ps.setString(2, rs.getString("id_article"));
                        connection.Execute(ps);
                    }    
                }while(rs.next());
            }
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(ServletCaddie.class.getName()).log(Level.SEVERE, null, ex);
            sortie.println("<H1>"+ ex.getMessage() +"</H1>"); 
            sortie.close(); 
        }  
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() 
    {
        return "Short description";
    }// </editor-fold>

}
