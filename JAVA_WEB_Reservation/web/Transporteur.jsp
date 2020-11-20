<%-- 
    Document   : Transporteur
    Created on : 13-nov.-2020, 10:35:09
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

            <h2 class="text-center " > Etape 02 </h2>

            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Mon transporteur existe déjà</h5>
                    <form action="./ReservationServlet" method="POST">
                        <input type="hidden" name="new" value="false">
                        <input type="hidden" name="source" value="transporteur">
                        <div class="form-group">
                            <label for="">Transporteur existantes</label>
                            <select name="transporteur" class="form-control">
                                <%
                                bean.Init();
                                PreparedStatement ps = bean.getPreparedStatement("SELECT * FROM Transporteurs WHERE types = \"C\";");
                                ResultSet rs = bean.ExecuteQuery(ps);
                                while(rs.next())
                                {
                                    %>
                                    <option> <% out.println(rs.getString("idTransporteur")); %> </option>
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
                    <h5 class="card-title">J'inscrit un camion</h5>
                    <form action="./ReservationServlet" method="POST">
                        <input type="hidden" name="source" value="transporteur">
                        <input type="hidden" name="new" value="true">
                        <div class="form-group">
                            <label for="">Id</label>
                            <input type="text" class="form-control" name="transporteur" id="">
                        </div>
                        <div class="form-group">
                            <label for="">capacite</label>
                            <input type="number" class="form-control" name="capacite" id="">
                        </div>
                        <div class="form-group">
                            <label for="">caracteristique</label>
                            <input type="text" class="form-control" name="caracteristique" id="">
                        </div>
                        <input type="submit" class="btn btn-primary" value="valider">
                    </form>
                </div>
            </div>
        </div>

        
    </body>
</html>