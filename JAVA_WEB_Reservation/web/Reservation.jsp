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

<jsp:useBean id="bean" class="lib.BeanDBAcces.MysqlConnector">
    <jsp:setProperty name="bean" property="username" value="root"/>
    <jsp:setProperty name="bean" property="password" value=""/>
    <jsp:setProperty name="bean" property="database" value="bd_mouvements"/>
</jsp:useBean>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Choix de votre Transporteur</title>
        <link rel="stylesheet" type="text/css" href="./inc/css/bootstrap.min.css"/>
    </head>
    <body class="bg-secondary">
        <div class="mx-auto pt-5" style="width: 25%;" >
            <h2 class="text-center " > Etape 03 </h2>

            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">J'ai déjà un container</h5>
                    <form action="./ReservationServlet" method="POST">
                        <input type="hidden" name="new" value="false">
                        <input type="hidden" name="source" value="container">
                        <div class="form-group">
                            <label for="">Societes existantes</label>
                            <select name="container" class="form-control">
                                <%
                                bean.Init();
                                PreparedStatement ps = bean.getPreparedStatement("SELECT * " +
                                                                                 "FROM containers " +
                                                                                 "LEFT JOIN parc p on containers.idContainer = p.idContainer " +
                                                                                 "WHERE p.idContainer IS NULL " +
                                                                                 "OR p.etat = 0; ");
                                ResultSet rs = bean.ExecuteQuery(ps);
                                while(rs.next())
                                {
                                    %>
                                    <option> <% out.println(rs.getString("idContainer")); %> </option>
                                    <%
                                }
                                %>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="">Destination</label>
                            <select name="destination" class="form-control">
                                <%
                                ps = bean.getPreparedStatement("SELECT * FROM destinations;");
                                rs = bean.ExecuteQuery(ps);
                                while(rs.next())
                                {
                                    %>
                                    <option> <% out.println(rs.getString("ville")); %> </option>
                                    <%
                                }
                                %>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="">Date de réservation</label>
                            <input type="date" class="form-control" name="date" id="">
                        </div>

                        <input type="submit" class="btn btn-primary" value="valider">
                    </form>
                </div>
            </div>

            <h2 class="text-center " > OU </h2>

            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">J'inscrit un container</h5>
                    <form action="./ReservationServlet" method="POST">
                        <input type="hidden" name="source" value="container">
                        <input type="hidden" name="new" value="true">
                        <div class="form-group">
                            <label for="">Id</label>
                            <input class="form-control" type="text" name="container" id="">
                        </div>
                        <div class="form-group">
                            <label for="">contenu</label>
                            <input class="form-control" type="text" name="contenu" id="">
                        </div>
                        <div class="form-group">
                            <label for="">capacite</label>
                            <input class="form-control" type="number" name="capacite" id="">
                        </div>
                        <div class="form-group">
                            <label for="">dangers</label>
                            <input class="form-control" type="text" name="dangers" id="">
                        </div>
                        <div class="form-group">
                            <label for="">poids</label>
                            <input class="form-control" type="number" name="poids" id="">
                        </div>
                        <div class="form-group">
                            <label for="">Destination</label>
                            <select class="form-control" name="destination">
                                <%
                                ps = bean.getPreparedStatement("SELECT * FROM destinations;");
                                rs = bean.ExecuteQuery(ps);
                                while(rs.next())
                                {
                                    %>
                                    <option> <% out.println(rs.getString("ville")); %> </option>
                                    <%
                                }
                                %>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="">Date de réservation</label>
                            <input type="date" class="form-control" name="date" id="">
                        </div>

                        <input type="submit" class="btn btn-primary" value="valider">
                    </form>
                </div>
            </div>
        </div>

        
    </body>
</html>
