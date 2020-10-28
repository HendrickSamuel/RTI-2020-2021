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
    this->hSocket = hSocket;
    this->adresseSocket = adresseSocket;
}

SocketsClient::SocketsClient(const SocketsClient& old)
{
    this->hSocket = old.hSocket;
    this->adresseSocket = old.adresseSocket;
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

void SocketsClient::connectSocket(const char* host, int port)
{
    struct sockaddr_in adresseSocket; //FIXME: redondant ?? avec socket

    adresseSocket = this->getAdressByName(host);
    adresseSocket.sin_family = AF_INET;
    adresseSocket.sin_port = htons(port);

    this->adresseSocket = adresseSocket;

    printf("Adresse IP = %s\n",inet_ntoa(adresseSocket.sin_addr)); 

    //TODO: verifier que this->hSocket est set (valide)
    if (connect(this->hSocket, (struct sockaddr *)&adresseSocket, sizeof(struct sockaddr_in)) == -1)
    {
        throw BaseException("Erreur de connect\n");
    }
    else 
        printf("<OK> connect socket OK\n");
}

void SocketsClient::initSocket(const char* host, int port)
{
    this->createSocket();
    this->connectSocket(host, port);
}


