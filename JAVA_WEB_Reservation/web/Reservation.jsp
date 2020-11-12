<%-- 
    Document   : Reservation
    Created on : 12-nov.-2020, 19:50:59
    Author     : hydro
--%>

<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="lib.BeanDBAcces.MysqlConnector"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Réservation</h1>
        <form action="./ReservationServlet" method="POST">
            <label for="">Id du container</label>
            <input type="text" name="container" id="">
            <br>

            <label for="">Destination</label>
            <select>
                <%
                try
                {
                    MysqlConnector conn = new MysqlConnector("root", "root", "bd_mouvements");
                    PreparedStatement ps = conn.getPreparedStatement("SELECT * FROM destinations;");
                    ResultSet rs = conn.ExecuteQuery(ps);
                    while(rs.next())
                    {
                        %>
                        <option> <% out.println(rs.getString("ville")); %> </option>
                        <%
                    }
                }
                catch (ClassNotFoundException | SQLException ex) 
                {
                    %>
                    <h2>ERRUEUR</h2>
                    <%
                }
                %>
                
            </select>

            <br>
            <input type="submit" value="Réserver">
        </form>
    </body>
</html>
