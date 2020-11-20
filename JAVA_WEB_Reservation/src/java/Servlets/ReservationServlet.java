//Auteurs : HENDRICK Samuel et DELAVAL Kevin                                                
//Groupe : 2302                                                
//Projet : R.T.I.                                 
//Date de la création : 10/11/2020

package Servlets;

import Beans.ReponseReservation;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lib.BeanDBAcces.MysqlConnector;

public class ReservationServlet extends HttpServlet {
    
    private MysqlConnector connection;
    
    @Override
    public void init(ServletConfig config) throws ServletException { 
        super.init(config);
        
        try {
            connection = new MysqlConnector("root","","bd_mouvements");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ReservationServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if(session.getAttribute("logon.isDone") == null)
        {
             getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
             return;
        }
        
        String source = request.getParameter("source");
        switch(source)
        {
            case "societe": 
                if(request.getParameter("new").equals("true"))
                    newSociete(request, response);
                else
                    existingSociete(request, response);
                break;
            case "transporteur": 
                if(request.getParameter("new").equals("true"))
                    newTransporteur(request, response);
                else
                    existingTransporteur(request, response);
                break;
            case "container": 
                if(request.getParameter("new").equals("true"))
                    newContainer(request, response);
                else
                    existingContainer(request, response);
                break;
        }
    }
    
    public void existingSociete(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        HttpSession session = request.getSession(true);
        session.setAttribute("societe", request.getParameter("societe"));
        response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Reservation/" + "Transporteur.jsp");
    }
    
    public void newSociete(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        try {
            PreparedStatement ps = 
                    connection.getPreparedStatement("INSERT into Societes (nom, email, telephone, adresse) VALUES (?,?,?,?);");
            ps.setString(1, request.getParameter("societe"));
            ps.setString(2, request.getParameter("email"));
            ps.setString(3, request.getParameter("telephone"));
            ps.setString(4, request.getParameter("adresse"));
            
            connection.Execute(ps);
            
            existingSociete(request, response);
            
        } catch (SQLException ex) {
            Logger.getLogger(ReservationServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void existingTransporteur(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        HttpSession session = request.getSession(true);
        session.setAttribute("transporteur", request.getParameter("transporteur"));
        response.sendRedirect(request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort()+ "/JAVA_WEB_Reservation/" + "Reservation.jsp");
    }
    
    public void newTransporteur(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        HttpSession session = request.getSession(true);
        try {
            PreparedStatement ps = 
                    connection.getPreparedStatement("INSERT into Transporteurs (idTransporteur, idSociete, capacite, caracteristiques, types) VALUES (?,?,?,?,?);");
            ps.setString(1, request.getParameter("transporteur"));
            ps.setString(2, session.getAttribute("societe").toString());
            ps.setInt(3, Integer.parseInt(request.getParameter("capacite")));
            ps.setString(4, request.getParameter("caracteristique"));
            ps.setString(5, "C");
            
            connection.Execute(ps);
            
            existingTransporteur(request, response);
            
        } catch (SQLException ex) {
            Logger.getLogger(ReservationServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void existingContainer(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        HttpSession session = request.getSession(true);
        session.setAttribute("container", request.getParameter("container"));
        reservation(request, response);
    }
    
    public void newContainer(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        HttpSession session = request.getSession(true);
        try {
            PreparedStatement ps = 
                    connection.getPreparedStatement("INSERT into Containers (idContainer,idSociete, contenu, capacite, dangers, poids) VALUES (?,?,?,?,?,?);");
            ps.setString(1, request.getParameter("container"));
            ps.setString(2, session.getAttribute("societe").toString());
            ps.setString(1, request.getParameter("contenu"));
            ps.setInt(3, Integer.parseInt(request.getParameter("capacite")));
            ps.setString(4, request.getParameter("dangers"));
            ps.setInt(3, Integer.parseInt(request.getParameter("poids")));
            
            connection.Execute(ps);
            
            existingContainer(request, response);
            
        } catch (SQLException ex) {
            Logger.getLogger(ReservationServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void reservation(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        HttpSession session = request.getSession(true);
        ReponseReservation rr = new ReponseReservation();
        try {
            PreparedStatement verify = connection.getPreparedStatement("SELECT * FROM parc WHERE upper(idContainer) = upper(?) AND etat <> 0;");
            verify.setString(1, request.getParameter("container"));
            ResultSet verifyResultSet = connection.ExecuteQuery(verify);
            if(verifyResultSet.next())
            {
                 rr.setResultat(false);
                 rr.setMessage("C'est embarassant ... il semblerait que le container soit déjà dans le parc... ");
                 session.setAttribute("reponse", rr);
                 getServletContext().getRequestDispatcher("/Resultat.jsp").forward(request, response);
                 return;
            }
            
            PreparedStatement ps = connection.getPreparedStatement("SELECT * FROM parc WHERE etat = 0;");
            ResultSet rs = connection.ExecuteQuery(ps);
            if(rs != null && rs.next())
            {
                rs.updateString("idContainer", request.getParameter("container"));
                rs.updateDate("dateReservation", Date.valueOf(request.getParameter("date")));
                rs.updateString("destination", request.getParameter("destination"));
                rs.updateInt("etat", 1);
                connection.UpdateResult(rs);

                rr.setResultat(true);
                rr.setX(rs.getInt("x"));
                rr.setY(rs.getInt("y"));
                rr.setDateReservation(request.getParameter("date"));
                rr.setDestination(request.getParameter("destination"));
                session.setAttribute("reponse", rr);
                
                getServletContext().getRequestDispatcher("/Resultat.jsp").forward(request, response);
            }
            else
            {
                rr.setResultat(false);
                rr.setMessage("Refus poli");
                session.setAttribute("reponse", rr);
                getServletContext().getRequestDispatcher("/Resultat.jsp").forward(request, response);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReservationServlet.class.getName()).log(Level.SEVERE, null, ex);
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
