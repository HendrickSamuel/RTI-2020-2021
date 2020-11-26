<%-- 
    Document   : Societe
    Created on : 13-nov.-2020, 10:34:57
    Author     : hydro
--%>

<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="lib.BeanDBAcces.MysqlConnector"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="bean" class="lib.BeanDBAcces.MysqlConnector">
    <jsp:setProperty name="bean" property="username" value="root"/>
    <jsp:setProperty name="bean" property="password" value="root"/>
    <jsp:setProperty name="bean" property="database" value="bd_mouvements"/>
</jsp:useBean>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Choix de votre societe</title>
        <link rel="stylesheet" type="text/css" href="./inc/css/bootstrap.min.css"/>
    </head>
    <body class="bg-secondary">
        <div class="mx-auto pt-5" style="width: 25%;" >
            <h2 class="text-center " > Etape 01 </h2>

            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">J'ai déjà une societe</h5>
                    <form action="./ReservationServlet" method="POST">
                        <input type="hidden" name="new" value="false">
                        <input type="hidden" name="source" value="societe">
                        <div class="form-group">
                            <label for="">Societe</label>
                            <select class="form-control" name="societe">
                                <%
                                bean.Init();
                                PreparedStatement ps = bean.getPreparedStatement("SELECT * FROM societes;");
                                ResultSet rs = bean.ExecuteQuery(ps);
                                while(rs.next())
                                {
                                    %>
                                    <option> <% out.println(rs.getString("nom")); %> </option>
                                    <%
                                }
                                %>
                            </select>
                        </div>
                        <input type="submit" class="btn btn-primary" value="valider">
                    </form>
                </div>
            </div>

            <h2 class="text-center " > OU </h2>

            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">J'inscrit une societe</h5>
                    <form action="./ReservationServlet" method="POST">
                        <input type="hidden" name="source" value="societe">
                        <input type="hidden" name="new" value="true">
                        <div class="form-group">
                            <label for="">Nom</label>
                            <input type="text" class="form-control" name="societe" id="">
                        </div>
                        <div class="form-group">
                            <label for="">email</label>
                            <input type="text" class="form-control" name="email" id="">
                        </div>

                        <div class="form-group">
                            <label for="">telephone</label>
                            <input type="text" class="form-control" name="telephone" id="">
                        </div>
                        <div class="form-group">
                            <label for="">adresse</label>
                            <input type="text" class="form-control" name="adresse" id="">
                        </div>

                        <input type="submit" class="btn btn-primary" value="valider">
                    </form>
                </div>
            </div>
        </div>

        
    </body>
</html>

