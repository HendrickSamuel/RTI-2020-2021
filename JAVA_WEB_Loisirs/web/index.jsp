<%-- 
    Document   : index
    Created on : 20 nov. 2020, 14:49:44
    Author     : delav
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PFM Loisirs : Login</title>
        <link rel="stylesheet" type="text/css" href="./inc/css/bootstrap.min.css"/>
    </head>
    <body class="bg-secondary">
        <div class="mx-auto pt-5" style="width: 400px;">
            <div class="card" style="width: 18rem;">
                <div class="card-body">
                    <h5 style='text-align: center' class="card-title">Bienvenue sur PFM Loisirs</h5>
                    <%
                        if(session.getAttribute("erreur") != null)
                        {
                            out.print("<label style=\"color:red;\">" + session.getAttribute("erreur") + "</label>");
                        }
                    %>
                    <form action="./ServletLogin" method="POST">
                        <div class="form-group">
                            <label>Votre identifiant :</label>
                            <input type="text" class="form-control" name="username" value="<% if(session.getAttribute("username") != null)out.print(session.getAttribute("username")); %>"/> 
                        </div>
                        <div class="form-group">
                            <label>Votre mot de passe :</label>
                            <input type="text" class="form-control" name="password" value="<% if(session.getAttribute("password") != null)out.print(session.getAttribute("password")); %>"/>
                        </div>
                        
                        <br>
                        <input style='margin-bottom: 15px;' type="submit" name="con" class="btn btn-primary d-block mx-auto" value="Entrer sur le site">
                        
                        <input type="submit" name="nopwd" class="btn btn-primary d-block mx-auto" value="Je n'ai pas de mot de passe">
                    </form>
                </div>
            </div>
        </div> 
    </body>
</html>