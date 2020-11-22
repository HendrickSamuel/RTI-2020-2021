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

<jsp:useBean id="rc" scope="session" class="Beans.ReponseCaddie">      
</jsp:useBean>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PFM Loisirs : Caddie</title>
        <link rel="stylesheet" type="text/css" href="./inc/css/bootstrap.min.css"/>
    </head>
    <body class="bg-secondary">
        <div class="mx-auto pt-5" style="width: 85%;">
            <div class="card">
                <div class="card-body">
                    <h3 style='text-align: center; text-decoration: underline;' class="card-title">PFM Loisirs - Session <%=  session.getAttribute("username")%></h3>
                    <% 
                        if(rc.getCode() != 0)
                        {
                            if(rc.getCode() == rc.ok)
                            {
                                %><h6 style="text-align: center"><%
                                out.print(rc.getMessage());
                                %></h6><%
                            }
                            else
                            {
                                %><h6 style="color : red; text-align: center"><%
                                out.print(rc.getMessage());
                                %></h6><%
                            }
                        }
                    %>
                    <div style="float: left; width: 65%;" class="card">
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
                                        int i = 0;
                                        bean.Init();
                                        PreparedStatement ps = bean.getPreparedStatement("SELECT * FROM article;");
                                        ResultSet rs = bean.ExecuteQuery(ps);
                                        while(rs.next())
                                        {
                                            if( i==2 )
                                            {
                                                out.print("<tr><td colspan=\"7\"></td></tr>");
                                            }
                                            %>
                                            <form action="./ServletCaddie" method="POST">
                                                <tr style="text-align: center;" class="table">
                                                    <td scope="row"><% out.print(rs.getString("id")); %></td>
                                                    <td scope="row"><% out.print(rs.getString("libelle")); %></td>
                                                    <td scope="row"><img style="width: 75px; height: 75px" src="<% out.print(rs.getString("image_path")); %>" alt="<% out.print(rs.getString("libelle")); %>"></td>
                                                    <td scope="row"><% out.print(rs.getString("prix")); %> &#8364;</td>
                                                    <td scope="row"><% 
                                                                        int quantite = 0;
                                                                        if(rs.getString("quantite") == null)
                                                                        {
                                                                            if(rs.getString("id").equals("art000001"))
                                                                            {
                                                                                quantite = 500;
                                                                                out.println("&#x221E;");   
                                                                            }
                                                                            else
                                                                            {
                                                                                ps = bean.getPreparedStatement("SELECT quantite FROM caddie WHERE id_article = \"art000002\" AND DATE(date_res) = DATE(CURDATE());");
                                                                                ResultSet rsint = bean.ExecuteQuery(ps);
                                                                                while(rsint.next())
                                                                                {
                                                                                    quantite = quantite + rsint.getInt("quantite");
                                                                                }
                                                                                quantite = 20 - quantite;
                                                                                out.println(quantite);
                                                                            }
                                                                        }
                                                                        else
                                                                        {
                                                                            quantite = rs.getInt("quantite");
                                                                            out.println(rs.getString("quantite"));
                                                                        }
                                                                                                            %></td>
                                                    <td scope="row"><input type="number" class="form-control" name="quantite" max="<%out.print(quantite);%>" min="0"></td>
                                                    <td scope="row"><input type="submit" class="btn btn-primary d-block mx-auto" name="ajouter" value="Ajouter" ></td>
                                                    <input type="hidden" name="article" value="<% out.print(rs.getString("id")); %>" >
                                                </tr>
                                            </form>
                                            <%
                                        i++;
                                        }
                                    %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                     <div style="float: right; width: 33.6%;" class="card">
                        <div class="card-body">
                            <h5 style='text-align: center; text-decoration: underline;'>Caddie</h5>
                            <br>
                            <table style="width: 100%;" class="table">
                                <tbody>
                                <%
                                    ps = bean.getPreparedStatement("SELECT article.id as id, article.libelle as libelle, article.image_path as image, caddie.quantite as quantite, article.prix as prix FROM caddie INNER JOIN article ON caddie.id_article = article.id WHERE caddie.id_client = ? AND caddie.acheter = 0;");
                                    ps.setString(1, (String)session.getAttribute("userid"));
                                    rs = bean.ExecuteQuery(ps);
                                    if(rs.next())
                                    {
                                        do
                                        {
                                            %>
                                                <tr><td colspan="3"></td></tr>
                                                <form action="./ServletCaddie" method="POST">
                                                    <tr style="text-align: center;" class="table">
                                                        <td scope="row"><% out.print(rs.getString("libelle")); %></td>
                                                        <td scope="row">quantité : <% out.print(rs.getString("quantite")); %></td>
                                                        <td scope="row">prix total : <% out.print(rs.getFloat("quantite")*rs.getFloat("prix")); %> &#8364;</td>
                                                    </tr>
                                                <tr style="text-align: center;" class="table">
                                                    <td scope="row"><img style="width: 75px; height: 75px" src="<% out.print(rs.getString("image")); %>" alt="<% out.print(rs.getString("libelle")); %>"></td>
                                                    <td scope="row"></td>
                                                    <td scope="row"><input type="submit" class="btn btn-danger d-block mx-auto" name="supprimer" value="Supprimer" ></td>
                                                </tr>
                                                <input type="hidden" name="article" value="<% out.print(rs.getString("id")); %>" >
                                                <input type="hidden" name="quantite" value="<% out.print(rs.getString("quantite")); %>" >
                                                </form>  
                                            <%
                                        }while(rs.next());
                                    }
                                    else
                                    {
                                        out.print("<h6 style='text-align: center;'>Malheureusement il semblerait que votre caddie soit vide</h6>");
                                    }
                                %>  
                                </tbody>
                            </table>   
                            <form action="./ServletCaddie" method="POST">
                                <input type="submit" class="btn btn-primary d-block mx-auto" name="valider" value="Valider le caddie" >
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>