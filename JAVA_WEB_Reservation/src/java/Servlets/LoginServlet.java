package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lib.BeanDBAcces.MysqlConnector;

public class LoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        session.invalidate();
        
        response.setContentType("text/html"); 
        PrintWriter sortie = response.getWriter();
        try {
            MysqlConnector conn = new MysqlConnector("root", "root", "bd_mouvements");
            PreparedStatement ps = conn.getPreparedStatement("SELECT * FROM logins WHERE upper(username) = upper(?);");
            ps.setString(1, request.getParameter("username"));
            ResultSet rs = conn.ExecuteQuery(ps);
            if(rs.next())
            {
                if(rs.getString("userpassword").equals(request.getParameter("password")))
                {
                    session = request.getSession(true);
                    session.setAttribute("logon.isDone", request.getParameter("username")); 

                    response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Reservation/" + "Societe.jsp");
                    return;
                }
                else
                {
                    response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Reservation/");
                    return;
                }
            }
            else
            {
                if(request.getParameter("firstConnexion")!=null && request.getParameter("firstConnexion").equals("on"))
                {
                    ps = conn.getPreparedStatement("INSERT into logins (username,userpassword) VALUES (?,?);");
                    ps.setString(1, request.getParameter("username"));
                    ps.setString(2, request.getParameter("password"));
                    conn.Execute(ps);
                    
                    session = request.getSession(true);
                    session.setAttribute("logon.isDone", request.getParameter("username"));
                    
                    response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Reservation/" + "Societe.jsp");
                    return;
                }
                else
                    response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Reservation/");
                return;
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
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
