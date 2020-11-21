<%-- 
    Document   : init
    Created on : 20 nov. 2020, 21:19:23
    Author     : delav
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PFM Loisirs : Init Caddie</title>
        <link rel="stylesheet" type="text/css" href="./inc/css/bootstrap.min.css"/>
    </head>
    <body class="bg-secondary">
        <div class="mx-auto pt-5" style="width: 900px;">
            <div class="card" style="width: 50rem;">
                <div class="card-body">
                    <h5 style='text-align: center; text-decoration: underline;' class="card-title">PFM Loisirs - Session <%=  session.getAttribute("username")%></h5>

                    <div style="float: left;">
                        <label>&#127794; Visite au parc de loisirs verts et/ou à la réserve naturelle</label>
                        <br>
                        <label>&#127794; Achats de guides et d'objets "nature"</label>
                    </div>
                    <div style="float: right;">
                        <img src="./images/parc.jpg" alt="Parc" width="270" height="150">
                    </div> 
                    <br>
                    <form action="./ServletInit" method="POST">
                        <input style='margin-bottom: 15px;' type="submit" name="conf" class="btn btn-primary d-block mx-auto" value="Continuer">
                        
                        <input type="submit" name="aban" class="btn btn-primary d-block mx-auto" value="Abandonner">
                    </form>
                </div>
            </div>
        </div> 
    </body>
</html>
