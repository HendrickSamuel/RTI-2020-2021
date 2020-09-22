/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include "SocketsClient.h"

/********************************/
/*                              */
/*         Constructeurs        */
/*                              */
/********************************/

SocketsClient::SocketsClient()
{

}

SocketsClient::SocketsClient(int hSocket, sockaddr_in adresse)
{
    this->hSocket = old.hSocket;
    this->adresseSocket = old.adresseSocket;
}

SocketsClient::SocketsClient(const SocketsClient& old)
{
    this->hSocket = hSocket;
    this->adresseSocket = adresseSocket;
}

/********************************/
/*                              */
/*          Destructeurs        */
/*                              */
/********************************/

/********************************/
/*                              */
/*           Operators          */
/*                              */
/********************************/

/********************************/
/*                              */
/*            Getters           */
/*                              */
/********************************/

/********************************/
/*                              */
/*            Setters           */
/*                              */
/********************************/

/********************************/
/*                              */
/*            Methodes          */
/*                              */
/********************************/

void SocketsClient::init(const char* host, int port)
{
    this->createSocket();
    this->connect(host, port);
}

void SocketsClient::connect(const char* host, int port)
{
    struct hostent *infoHost;
    struct in_addr adresseIP;
    struct sockaddr_in adresseSocket; //FIXME: redondant ?? avec socket

    if((infoHost = gethostbyname(host)) == 0)
    {
        throw BaseException("Erreur d'acquisition d'infos sur le host distant");
    }
    else 
        printf("Acquisition host distant OK\n");

    memcpy(&adresseIP, infoHost->h_addr, infoHost->h_length);
    printf("Adresse IP = %s\n",inet_ntoa(adresseIP)); 

    memset(&adresseSocket, 0, sizeof(struct sockaddr_in));
    adresseSocket.sin_family = AF_INET;
    adresseSocket.sin_port = htons(port);
    memcpy(&adresseSocket.sin_addr, infoHost->h_addr, infoHost->h_length);
    
    this->adresseSocket = adresseSocket;

    //TODO: verifier que this->hSocket est set (valide)
    if (connect(this->hSocket, (struct sockaddr *)&adresseSocket, sizeof(struct sockaddr_in)) == -1)
    {
        throw BaseException("Erreur de connect");
    }
    else 
        printf("<OK> connect socket OK\n");
}