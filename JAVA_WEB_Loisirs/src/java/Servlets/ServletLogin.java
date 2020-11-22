//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 20/11/2020

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


public class ServletLogin extends HttpServlet 
{

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        HttpSession session = request.getSession(true);
        session.invalidate();
        
        response.setContentType("text/html"); 
        PrintWriter sortie = response.getWriter();
        
        //Si bouton pour aller sur le site
        if(request.getParameter("con") != null)
        {   //Si le username ou le password son vide
            if(request.getParameter("username").length() < 1)
            {
                session = request.getSession(true);
                session.setAttribute("erreur", "Veuiller encoder un nom d'utilisateur");
                response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/");
                return; 
            }
            else
            {
                try 
                {
                    if(request.getParameter("password").length() < 1)
                    {
                        session = request.getSession(true);
                        session.setAttribute("username", request.getParameter("username"));
                        
                        response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/" + "init.jsp");
                        return;
                    }
                    else
                    {
                        MysqlConnector conn = new MysqlConnector("root", "", "bd_shopping");
                        PreparedStatement ps = conn.getPreparedStatement("SELECT * FROM client WHERE upper(username) = upper(?);");
                        ps.setString(1, request.getParameter("username"));
                        ResultSet rs = conn.ExecuteQuery(ps);
                        //Si le username existe
                        if(rs.next())
                        {   //Si les mots de passes correspondent
                            if(rs.getString("userpassword").equals(request.getParameter("password")))
                            {
                                session = request.getSession(true);
                                session.setAttribute("username", request.getParameter("username"));
                                session.setAttribute("userid", rs.getString("id"));

                                response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/" + "init.jsp");
                                return;
                            }
                            else //Sinon le mot de passe est incorrect
                            {
                                session = request.getSession(true);
                                session.setAttribute("username", request.getParameter("username"));
                                session.setAttribute("erreur", "Nom d'utilisateur ou mot de passe incorrecte");
                                response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/");
                                return;
                            }
                        }
                        else //Sinon le usename n'existe pas
                        {
                            session = request.getSession(true);
                            session.setAttribute("username", request.getParameter("username"));
                            session.setAttribute("erreur", "Nom d'utilisateur ou mot de passe incorrecte");
                            response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/");
                            return;
                        }
                    }
                }
                catch (ClassNotFoundException | SQLException ex) 
                {
                    Logger.getLogger(ServletLogin.class.getName()).log(Level.SEVERE, null, ex);
                    sortie.println("<H1>"+ ex.getMessage() +"</H1>"); 
                    sortie.close(); 
                }
            }
        }
        else //Sinon demande de mot de passe
        {   //Si pas de username
            if(request.getParameter("username").length() < 1)
            {
                session = request.getSession(true);
                session.setAttribute("erreur", "Veuiller encoder un nom d'utilisateur");
                response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/");
                return; 
            }
            else //Sinon on redirige sur la page newPassword.jsp
            {
                session = request.getSession(true);
                session.setAttribute("username", request.getParameter("username")); 
                response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Loisirs/" + "newPassword.jsp");
                return;   
            }
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