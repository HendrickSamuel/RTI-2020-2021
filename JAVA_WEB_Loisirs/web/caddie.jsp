<%-- 
    Document   : caddie
    Created on : 20 nov. 2020, 22:50:24
    Author     : delav
--%>

<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="lib.BeanDBAcces.MysqlConnector"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="bean" class="lib.BeanDBAcces.MysqlConnector">
    <jsp:setProperty name="bean" property="username" value="root"/>
    <jsp:setProperty name="bean" property="password" value=""/>
    <jsp:setProperty name="bean" property="database" value="bd_shopping"/>
</jsp:useBean>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PFM Loisirs : Caddie</title>
        <link rel="stylesheet" type="text/css" href="./inc/css/bootstrap.min.css"/>
    </head>
    <body class="bg-secondary">
        <div class="mx-auto pt-5" style="width: 80%;">
            <div class="card">
                <div class="card-body">
                    <h3 style='text-align: center; text-decoration: underline;' class="card-title">PFM Loisirs - Session <%=  session.getAttribute("username")%></h3>
                    <div style="float: left; width: 70%;" class="card">
                        <div class="card-body">
                            <h5 style='text-align: center; text-decoration: underline;'>Magasin</h5>
                            <br>
                            <table style="width: 100%;" class="table">
                                <thead>
                                    <tr style="text-align: center;">
                                        <th scope="col">Id article</th>
                                        <th scope="col">Libelle</th>
                                        <th scope="col">Image</th>
                                        <th scope="col">Prix</th>
                                        <th scope="col">Quantité restante</th>
                                        <th scope="col">Choix quantité</th>
                                        <th scope="col"></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        bean.Init();
                                        PreparedStatement ps = bean.getPreparedStatement("SELECT * " +
                                                                                         "FROM article ");
                                        ResultSet rs = bean.ExecuteQuery(ps);
                                        while(rs.next())
                                        {
                                            %>
                                            <tr style="text-align: center;" class="table">
                                                <td scope="row"><% out.println(rs.getString("id")); %></td>
                                                <td scope="row"><% out.println(rs.getString("libelle")); %></td>
                                                <td scope="row"><img style="width: 75px; height: 75px" src="<% out.println(rs.getString("image_path")); %>" alt="<% out.println(rs.getString("libelle")); %>"> </td>
                                                <td scope="row"><% out.println(rs.getString("prix")); %> €</td>
                                                <td scope="row"><% 
                                                                    if(rs.getString("quantite") == null)
                                                                    {
                                                                        if(rs.getString("id").equals("art000001"))
                                                                        {
                                                                            out.println("&#x221E;");   
                                                                        }
                                                                        else
                                                                        {
                                                                            
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        out.println(rs.getString("quantite"));
                                                                    }
                                                                                                        %></td>
                                                <td scope="row"></td>
                                                <td scope="row"><button class="btn btn-primary d-block mx-auto" >Ajouter</button></td>

                                            </tr>
                                            <%
                                        }
                                    %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                     <div style="float: right; width: 28.6%;" class="card">
                        <div class="card-body">
                            <h5 style='text-align: center; text-decoration: underline;'>Caddie</h5>
                            
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
