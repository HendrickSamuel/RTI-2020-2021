/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include "Sockets.h"

/**/

//TODO: revoir utilité de toutes le fonctions

//FIXME: it's broken

int getAdressByName(sockaddr_in *adresse,const char* hostName)
{
    //TODO: passer par un booleen ? 
    struct hostent *infosHost;

    if((infosHost = gethostbyname(hostName)) == 0)
    {
        printf("<Erreur> acquisition d'informations sur le host: %d\n", errno);
        return 0; //toujours 0 en cas d'erreur
    }
    else
        printf("<OK> acquisition d'informations sur le host\n");
    
    memcpy(adresse, infosHost->h_addr, infosHost->h_length);
    
    return 1;
}

int socketCreate(void)
{
    int handler;

    handler = socket(AF_INET, SOCK_STREAM, 0);
    //if(debug)
    //{
        if(handler == -1)
        {
            printf("Erreur de creation de la socket %d\n", errno);
        }
        else
        {
            printf("Creation de la socket OK\n");
        }  
    //}

    return handler;
}


int socketBind(int handler, const char* hostName)
{
    //handler = hSocketEcoute

    struct sockaddr_in adresseSocket;
    int result;

    memset(&adresseSocket, 0, sizeof(struct sockaddr_in)); // préparation de la zone mémoire

    if( getAdressByName(&adresseSocket,hostNa   me) == 0)
    {
        // probleme de recup d'adresse
    }

    adresseSocket.sin_family = AF_INET;
    adresseSocket.sin_port = htons(PORT);

    printf("Adresse IP = %s\n",inet_ntoa(adresseSocket.sin_addr)); 

    if ((result = bind(handler, (struct sockaddr *)&adresseSocket, sizeof(struct sockaddr_in))) == -1)
    {
        printf("<Erreur> sur le bind de la socket %d\n", errno);
        return(result);
    }
    else 
        printf("<OK> Bind adresse et port socket OK\n");    

    return result;
}



int socketClose(int handler)
{


}


int socketShutdown(int handler, int sensFermeture)
{


}


int socketListen(int handler, int maxConnexion)
{
    listen(handler, maxConnexion);

}


int socketAccept(int handler, sockaddr_in* adresse)
{
    int hSocketService;
    unsigned int tailleSockaddr_in = sizeof(struct sockaddr_in);
    hSocketService = accept(handler,(struct sockaddr*)adresse, &tailleSockaddr_in);
    return hSocketService;
}


int socketConnect(int handler, sockaddr_in adresse)
{


}


int socketSend(int handler, const void *message, int taille)
{


}


int socketRecieveStruct(int handler, void *message, int taille)
{


}

int socketRecieveString(int handler, void *message, int taille)
{


}