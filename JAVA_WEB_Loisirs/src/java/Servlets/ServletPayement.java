//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 21/11/2020

package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lib.BeanDBAcces.MysqlConnector;


public class ServletPayement extends HttpServlet 
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
        
        //Pour voir si le choix est abandonner
        if(request.getParameter("abandon") != null)
        {
            try 
            {
                MysqlConnector conn = new MysqlConnector("root", "root", "bd_shopping");
                PreparedStatement ps = conn.getPreparedStatement("SELECT id_article, quantite FROM caddie WHERE id_client = ? AND acheter = 0;");
                ps.setString(1, String.valueOf(session.getAttribute("userid")));
                ResultSet rs = conn.ExecuteQuery(ps);
                //Si l'utilisateur a un caddie on le met a jour
                while(rs.next())
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
                    ps.setString(1, String.valueOf(session.getAttribute("userid")));
                    ps.setString(2, rs.getString("id_article"));
                    conn.Execute(ps);
                }
            }
            catch (ClassNotFoundException | SQLException ex) 
            {
                Logger.getLogger(ServletInit.class.getName()).log(Level.SEVERE, null, ex);
                sortie.println("<H1>"+ ex.getMessage() +"</H1>"); 
                sortie.close(); 
            }  
            
            session.invalidate();
            response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/");
            return; 
        }
        
        //verifie si un mot de passe a été entré
        if(request.getParameter("password").length() < 1)
        {
            session.setAttribute("erreur", "Veuiller encoder votre mot de passe !!!");
            response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/" + "pay.jsp");
            return; 
        }
        
        if(verifPayement(session, sortie, request) == true)
        {
            response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/" + "recapitulatif.jsp");
            return; 
        }
        else
        {
            response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/" + "pay.jsp");
            return; 
        }
        
    }
    
    private boolean verifPayement(HttpSession session, PrintWriter sortie, HttpServletRequest request)
    {
        try 
        {
            MysqlConnector conn = new MysqlConnector("root", "root", "bd_shopping");
            PreparedStatement ps = conn.getPreparedStatement("SELECT * FROM client WHERE id = ?;");
            ps.setString(1, (String)session.getAttribute("userid"));
            ResultSet rs = conn.ExecuteQuery(ps);
            //Si le username existe
            if(rs.next())
            {   //Si les mots de passes correspondent
                if(rs.getString("userpassword").equals(request.getParameter("password")))
                {
                    //mettre les articles dans la caddie comme "acheter"
                    ps = conn.getPreparedStatement("UPDATE caddie SET acheter = ? WHERE id_client = ? AND acheter = 0;");
                    ps.setInt(1, 1);
                    ps.setString(2, (String)session.getAttribute("userid"));
                    conn.Execute(ps);
                    
                    String id = (String)session.getAttribute("userid");
                    
                    session.invalidate();
                    
                    session = request.getSession(true);
                    session.setAttribute("recapid", id);
                    session.setAttribute("moypay", request.getParameter("moypay"));
                    return true;
                }
                else
                {
                    session.setAttribute("erreur", "Mot de passe incorrecte !!!");
                    return false;
                }
            }
        } 
        catch (ClassNotFoundException | SQLException ex) 
        {
            Logger.getLogger(ServletPayement.class.getName()).log(Level.SEVERE, null, ex);
            sortie.println("<H1>"+ ex.getMessage() +"</H1>"); 
            sortie.close(); 
        }
        return false;
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
