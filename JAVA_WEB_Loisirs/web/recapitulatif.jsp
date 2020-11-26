<%-- 
    Document   : recapitulatif
    Created on : 22 nov. 2020, 15:36:23
    Author     : delav
--%>

<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="lib.BeanDBAcces.MysqlConnector"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="bean" class="lib.BeanDBAcces.MysqlConnector">
    <jsp:setProperty name="bean" property="username" value="root"/>
    <jsp:setProperty name="bean" property="password" value="root"/>
    <jsp:setProperty name="bean" property="database" value="bd_shopping"/>
</jsp:useBean>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PFM Loisirs : Récapitulatif</title>
        <link rel="stylesheet" type="text/css" href="./inc/css/bootstrap.min.css"/>
    </head>
    <body class="bg-secondary">
        <div class="mx-auto pt-5" style="width: 50%;">
            <div class="card" >
                <div class="card-body">
                    <h3 style='text-align: center; text-decoration: underline;' class="card-title">PFM Loisirs</h3>
                    <div class="card">
                        <div class="card-body">
                            <h5 style='text-align: center; text-decoration: underline;'>Récapitulatif des achats</h5>
                            
                            <h6 style="color:red; text-align: center; margin-bottom:-20px;">Vous avez payer avec <%=  session.getAttribute("moypay")%></h6>
                            <table style="width: 75%;margin-left: auto; margin-right: auto" class="table">
                                <tbody>
                                    <%
                                        bean.Init();
                                        PreparedStatement ps = bean.getPreparedStatement("SELECT article.id as id, article.libelle as libelle, article.image_path as image, caddie.quantite as quantite, article.prix as prix FROM caddie INNER JOIN article ON caddie.id_article = article.id WHERE caddie.id_client = ? AND caddie.acheter = 1 AND DATE(date_res) = DATE(CURDATE());");
                                        ps.setString(1, (String)session.getAttribute("recapid"));
                                        ResultSet rs = bean.ExecuteQuery(ps);
                                        if(rs.next())
                                        {
                                            do
                                            {
                                                %>
                                                    <tr><td colspan="4"></td></tr>
                                                    <tr style="text-align: center;" class="table">
                                                        <td scope="row"><% out.print(rs.getString("libelle")); %></td>
                                                        <td scope="row"><img style="width: 75px; height: 75px" src="<% out.print(rs.getString("image")); %>" alt="<% out.print(rs.getString("libelle")); %>"></td>
                                                        <td scope="row">quantité : <% out.print(rs.getString("quantite")); %></td>
                                                        <td scope="row">prix unitaire : <% out.print(rs.getFloat("prix")); %></td>
                                                        <td scope="row">prix total : <% out.print(rs.getFloat("quantite")*rs.getFloat("prix")); %> &#8364;</td>
                                                    </tr>

                                                <%
                                            }while(rs.next());
                                        }
                                    %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
