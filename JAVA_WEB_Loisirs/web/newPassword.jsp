<%-- 
    Document   : newPassword
    Created on : 20 nov. 2020, 15:33:53
    Author     : delav
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PFM Loisirs : New password</title>
        <link rel="stylesheet" type="text/css" href="./inc/css/bootstrap.min.css"/>
    </head>
    <body class="bg-secondary">
        <div class="mx-auto pt-5" style="width: 400px;">
            <div class="card" style="width: 25rem;">
                <div class="card-body">
                    <h5 style='text-align: center; text-decoration: underline;' class="card-title">PFM Loisirs : Choix du mot de passe</h5>
                    <br>
                    <%
                        if(session.getAttribute("erreur") != null)
                        {
                            out.print("<label style=\"color:red;\">" + session.getAttribute("erreur") + "</label>");
                        }
                    %>
                    <form action="./ServletNewPwd" method="POST">
                        <div class="form-group">
                            <label>Votre identifiant : <%= session.getAttribute("username") %></label>
                            <input type="hidden" class="form-control" name="username" value="<%= session.getAttribute("username") %>"/> 
                        </div>
                        <div class="form-group">
                            <label>Choisissez un mot de passe :</label>
                            <input type="text" class="form-control" name="password1"/> 
                        </div>
                        <div class="form-group">
                            <label>Confirmer votre mot de passe :</label>
                            <input type="text" class="form-control" name="password2"/>
                        </div>
                        
                        <br>
                        <input style='margin-bottom: 15px;' type="submit" name="conf" class="btn btn-primary d-block mx-auto" value="Confirmer">
                        <input style='margin-bottom: 15px;' type="submit" name="aban" class="btn btn-primary d-block mx-auto" value="Abandonner">
                    </form>
                    <a href="./aide.jsp"><button style='margin-bottom: 15px;' class="btn btn-primary d-block mx-auto" >Aide</button></a>
                </div>
            </div>
        </div> 
    </body>
</html>