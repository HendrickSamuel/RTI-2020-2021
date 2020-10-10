package protocolTRAMAP;

import genericRequest.DonneeRequete;
import genericRequest.Reponse;
import genericRequest.Traitement;
import MyGenericServer.ConsoleServeur;

public class TraitementTRAMAP implements Traitement {

    //todo: rajouter un Objet client ici

    @Override
    public Reponse traiteRequete(DonneeRequete Requete) throws ClassCastException {

        if(Requete instanceof DonneeLogin)
            return traiteLOGIN(null, (DonneeLogin)Requete);
        else if(Requete instanceof DonneeInputLory)
            traiteINPUTLORY(null, (DonneeInputLory)Requete);
        else if(Requete instanceof DonneeInputLoryWithoutReservation)
            traiteINPUTLORYWITHOUTRESERVATION( null, (DonneeInputLoryWithoutReservation)Requete);
        else if(Requete instanceof DonneeListOperations)
            traiteListe( null, (DonneeListOperations)Requete);
        else if(Requete instanceof  DonneeLogout)
            traiteLOGOUT( null, (DonneeLogout)Requete);
        else
            return traite404( null);

        return null;
    }

    private Reponse traiteLOGIN(ConsoleServeur cs, DonneeLogin chargeUtile)
    {
        System.out.println("traiteLOGIN");

        System.out.println("Mot de passe: " + (chargeUtile).getPassword());
        System.out.println("Utilisateur: " + (chargeUtile).getUsername());

        //MysqlConnector mc = new MysqlConnector("root","root","bd_mouvements");

        return new ReponseTRAMAP(ReponseTRAMAP.LOGIN_OK, null, null);
    }

    private void traiteINPUTLORY(ConsoleServeur cs, DonneeInputLory chargeUtile)
    {
        System.out.println("traiteINPUTLORY");
    }

    private void traiteINPUTLORYWITHOUTRESERVATION(ConsoleServeur cs, DonneeInputLoryWithoutReservation chargeUtile)
    {
        System.out.println("traiteINPUTLORYWITHOUTRESERVATION");
    }

    private void traiteListe(ConsoleServeur cs, DonneeListOperations chargeUtile)
    {
        System.out.println("traiteListe");
    }

    private void traiteLOGOUT(ConsoleServeur cs, DonneeLogout chargeUtile)
    {
        System.out.println("traiteLOGOUT");
    }

    private Reponse traite404(ConsoleServeur cs)
    {
        System.out.println("traite404 Request not found");
        return new ReponseTRAMAP(ReponseTRAMAP.REQUEST_NOT_FOUND, null, "request could not be exeuted due to unsopported version.");
    }
}
