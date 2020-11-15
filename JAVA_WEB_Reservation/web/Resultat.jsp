<%-- 
    Document   : Resultat
    Created on : 13-nov.-2020, 12:14:00
    Author     : hydro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="reponse" scope="session" class="Beans.ReponseReservation">
            
</jsp:useBean>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="./inc/css/bootstrap.min.css"/>
    </head>
    <body class="bg-secondary">
        <div class="mx-auto pt-5" style="width: 25%;" >
            <h2 class="text-center " > Etape Finale </h2>
         <% 
         if(reponse.isResultat() == true)
         {%>
             <div class="card text-white bg-success my-3">
                 <div class="card-header">Validé</div>
                <div class="card-body">
                    societe: ${sessionScope.societe} <br>
                    transporteur: ${sessionScope.transporteur} <br>
                    container: ${sessionScope.container} <br>
                    
                    <hr>
                    
                    <p>
                        Une réservation à été faite à l'emplacement: 
                        ${reponse.x} - ${reponse.y} <br>
                        pour la destination: ${reponse.destination}  <br>
                        à la date du: ${reponse.dateReservation} <br>
                        
                    </p>
                    
                    
                </div>
             </div>
         <%}
         else
         {%>
           <div class="card text-white bg-danger my-3">
                <div class="card-header">Erreur</div>
                <div class="card-body">
                    ${reponse.message}
                    <br>
                </div>
             </div>
         <%}
         
         
         %>

         <div class="d-flex justify-content-between">
            <a href="./Societe.jsp" class="btn btn-primary">Recommencer</a>
            <a href="./LogoutServlet" class="btn btn-primary">Se déconnecter</a>
         </div>

        </div>
    </body>
</html>
