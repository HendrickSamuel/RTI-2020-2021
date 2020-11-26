<%-- 
    Document   : pay
    Created on : 22 nov. 2020, 11:49:51
    Author     : delav
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PFM Loisirs : Payement</title>
        <link rel="stylesheet" type="text/css" href="./inc/css/bootstrap.min.css"/>
    </head>
    <body class="bg-secondary">
        <div class="mx-auto pt-5" style="width: 900px;">
            <div class="card" style="width: 50rem;">
                <div class="card-body">
                    <h3 style='text-align: center; text-decoration: underline;' class="card-title">PFM Loisirs - Session <%=  session.getAttribute("username")%></h3>
                    <div class="card">
                        <div class="card-body">
                            <h5 style='text-align: center; text-decoration: underline;'>Payement</h5>
                            <%
                                if(session.getAttribute("erreur") != null)
                                {
                                    out.print("<h6 style=\"color:red; text-align: center; margin-bottom:-20px;\">" + session.getAttribute("erreur") + "</h6>");
                                }
                            %>
                            <br>
                            <label>Choisissez votre moyen de payement :</label>
                            <br>
                            <form action="./ServletPayement" method="POST">
                                <table style="width: 50%;margin-left: auto; margin-right: auto" class="table">
                                    <tbody>
                                        <tr class="table">
                                            <td scope="row"><input type="radio" id="paypal" name="moypay" value="paypal" checked><label for="paypal">Paypal</label></td>
                                            <td style="text-align: center;" scope="row"><img style="width: 110px; height: 75px" src="./images/paypal.jpg" alt="paypal.jpg"></td>
                                        </tr>
                                        <tr class="table">
                                            <td scope="row"><input type="radio" id="visa" name="moypay" value="visa"><label for="visa">Visa</label></td>
                                            <td style="text-align: center;"  scope="row"><img style="width: 110px; height: 75px" src="./images/visa.jpg" alt="visa.jpg"></td>
                                        </tr>
                                        <tr class="table">
                                            <td scope="row"><input type="radio" id="bancontact" name="moypay" value="bancontact"><label for="bancontact">Bancontact</label></td>
                                            <td style="text-align: center;"  scope="row"><img style="width: 100px; height: 75px" src="./images/bancontact.jpg" alt="bancontact.jpg"></td>
                                        </tr>
                                    </tbody>
                                </table>
                                <div class="form-inline">
                                    <span class="input-group-addon">Entrez votre mot de passe avant de confirmer :</span>
                                    <input style="margin-left: 10px;" type="text" class="form-control"  name="password" placeholder="Mot de passe">
                                </div>
                                <br>
                                <input type="submit" class="btn btn-primary d-block mx-auto" name="payer" value="Procéder au payement" >
                                <br>
                                <input type="submit" class="btn btn-primary d-block mx-auto" name="abandon" value="Abandonner" >
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
