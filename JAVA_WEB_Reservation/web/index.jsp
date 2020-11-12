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
        <link rel="stylesheet" type="text/css" href="./test.css"/>
    </head>
    <body>
        <h1>Login</h1>
        <form action="./LoginServlet" method="POST">
            <label for="">Company</label>
            <input type="text" name="company"/>
            <br>
            <label>Username</label>
            <input type="text" name="username"/> 
            <br>
            <label>Password</label>
            <input type="text" name="password"/>
            <br>
            <label for="">1ere connexion ?</label>
            <input type="checkbox" name="firstConnexion">
            <br>
            <input type="submit" value="Valide choix">
            
        </form>
    </body>
</html>
