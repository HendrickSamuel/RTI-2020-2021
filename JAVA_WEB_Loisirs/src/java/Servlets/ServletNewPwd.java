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


public class ServletNewPwd extends HttpServlet 
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        HttpSession session = request.getSession(false);
        //Pour si on veux accéder à la page sans avoir de session
        if(session == null)
        {
            response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/");
            return; 
        }
        
        response.setContentType("text/html"); 
        PrintWriter sortie = response.getWriter();

        //Si on appuie sur confirmer
        if(request.getParameter("conf") != null)
        {
            //Mettre un message d'erreur que les mots de passe sont differents        
            if(!request.getParameter("password1").equals(request.getParameter("password2")) || request.getParameter("password1").length() < 1 || request.getParameter("password2").length() < 1 )
            {
                session.setAttribute("erreur", "Les mots de passe ne sont pas correctes");
                response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/" + "newPassword.jsp");
                return;
            }
            else //Si les mot de passe sont bien égaux et non NULL
            {
                try 
                {
                    MysqlConnector conn = new MysqlConnector("root", "", "bd_shopping");
                    PreparedStatement ps = conn.getPreparedStatement("SELECT * FROM client WHERE upper(username) = upper(?);");
                    ps.setString(1, request.getParameter("username"));
                    ResultSet rs = conn.ExecuteQuery(ps);
                    //Si l'utilisateur existe deja on le met a jour
                    if(rs.next())
                    {
                        ps = conn.getPreparedStatement("UPDATE client SET userpassword = ? WHERE UPPER(username) = UPPER(?);");
                        ps.setString(1, request.getParameter("password1"));
                        ps.setString(2, request.getParameter("username"));
                        conn.Execute(ps);
                    }
                    else //Sinon on le crée
                    {
                        ps = conn.getPreparedStatement("INSERT into client (username,userpassword) VALUES (?,?);");
                        ps.setString(1, request.getParameter("username"));
                        ps.setString(2, request.getParameter("password1"));
                        conn.Execute(ps); 
                    }
                    session.invalidate();
                    session = request.getSession(true);
                    session.setAttribute("username", request.getParameter("username"));
                    session.setAttribute("password", request.getParameter("password1")); 

                    response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/");
                    return;
                }
                catch (ClassNotFoundException | SQLException ex) 
                {
                    Logger.getLogger(ServletLogin.class.getName()).log(Level.SEVERE, null, ex);
                    sortie.println("<H1>"+ ex.getMessage() +"</H1>"); 
                    sortie.close(); 
                }  
            }
        }
        else //Sinon appui de Annuler
        {
            session.invalidate();
            response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/");
            return;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
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
