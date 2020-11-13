<%-- 
    Document   : index
    Created on : 12-nov.-2020, 13:21:15
    Author     : hydro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login page</title>
        <link rel="stylesheet" type="text/css" href="./inc/css/bootstrap.min.css"/>
    </head>
    <body class="bg-secondary">
        <div class="mx-auto pt-5" style="width: 400px;">
            <div class="card" style="width: 18rem;">
                <div class="card-body">
                    <h5 class="card-title">Login</h5>
                    <form action="./LoginServlet" method="POST">
                        <div class="form-group">
                            <label>Username</label>
                            <input type="text" class="form-control" name="username"/> 
                        </div>
                        <div class="form-group">
                            <l<label>Password</label>
                            <input type="text" class="form-control" name="password"/>
                        </div>
                        
                        <input type="checkbox" name="firstConnexion">
                        <label for="">1ere connexion ?</label>
                        <br>
                        <input type="submit" class="btn btn-primary" value="Connexion">
                    </form>
                </div>
            </div>
        </div>
        
        
    </body>
</html>
