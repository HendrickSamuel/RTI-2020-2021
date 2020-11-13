<%-- 
    Document   : Resultat
    Created on : 13-nov.-2020, 12:14:00
    Author     : hydro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <c:if test="${ !empty sessionScope.societe && !empty sessionScope.transporteur && !empty sessionScope.container }">
            <p> ${ sessionScope.societe } | ${ sessionScope.transporteur } | ${sessionScope.container}</p>
            <p> ${sessionScope.x} + ${sessionScope.y} </p>
        </c:if>
    </body>
</html>
