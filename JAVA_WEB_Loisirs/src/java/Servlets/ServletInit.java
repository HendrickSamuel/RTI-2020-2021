//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 10/11/2020

package Servlets;

import java.sql.ResultSet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import lib.BeanDBAcces.MysqlConnector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ServletInit extends HttpServlet 
{

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        HttpSession session = request.getSession(false);
        //Pour si on veux accéder à la page sans avoir de session
        if(session.getAttribute("username") == null)
        {
            response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/");
            return; 
        }
        
        response.setContentType("text/html"); 
        PrintWriter sortie = response.getWriter();
        
        //initialisation du caddie uniquement si il est login
        if(session.getAttribute("userid") == null)
        {
            initCadie(session, sortie);
        }

        //Si on appuie sur confirmer
        if(request.getParameter("conf") != null)
        {
            response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/" + "caddie.jsp");
            return;  
        }
        else //Sinon appui de Annuler
        {
            session.invalidate();
            response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/");
            return;
        }
    }
    
    private void initCadie(HttpSession session, PrintWriter sortie)
    {

        try 
        {
            MysqlConnector conn = new MysqlConnector("root", "", "bd_shopping");
            PreparedStatement ps = conn.getPreparedStatement("SELECT id_article, quantite, TIMESTAMPDIFF(MINUTE,date_res,current_timestamp()) as duree FROM caddie WHERE id_client = ? AND acheter = 0;");
            ps.setString(1, (String)session.getAttribute("userid"));
            ResultSet rs = conn.ExecuteQuery(ps);
            //Si l'utilisateur a un caddie on le met a jour
            if(rs.next())
            {
                do
                {
                    if(rs.getInt("duree") >= 30)
                    {
                        if(!rs.getString("id_article").equals("art000001") && !rs.getString("id_article").equals("art000002"))
                        {
                            ps = conn.getPreparedStatement("SELECT quantite FROM article WHERE id = ?;");
                            ps.setString(1, rs.getString("id_article"));
                            ResultSet rsUp = conn.ExecuteQuery(ps);

                            rsUp.next();
                            int quantite = rsUp.getInt("quantite")+rs.getInt("quantite");

                            ps = conn.getPreparedStatement("UPDATE article SET quantite = ? WHERE id = ?;");
                            ps.setString(1, String.valueOf(quantite));
                            ps.setString(2, rs.getString("id_article"));
                            conn.Execute(ps);
                        }

                        ps = conn.getPreparedStatement("DELETE FROM caddie WHERE id_client = ? AND id_article = ? AND acheter = 0;");
                        ps.setString(1, (String)session.getAttribute("userid"));
                        ps.setString(2, rs.getString("id_article"));
                        conn.Execute(ps);
                    }    
                }while(rs.next());
            }
        }
        catch (ClassNotFoundException | SQLException ex) 
        {
            Logger.getLogger(ServletInit.class.getName()).log(Level.SEVERE, null, ex);
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
