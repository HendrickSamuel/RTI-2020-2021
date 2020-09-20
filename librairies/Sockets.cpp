/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include "Sockets.h"

Sockets::Sockets()
{
    //this->Create();
}

Sockets::Sockets(const Sockets& old)
{
    this->hSocket = old.hSocket;
    this->adresseSocket = old.adresseSocket;
}

Sockets::Sockets(int hSocket, sockaddr_in adresse)
{
    this->hSocket = hSocket;
    this->adresseSocket = adresseSocket;
}

void Sockets::Create()
{
    this->hSocket = socket(AF_INET, SOCK_STREAM, 0);
        if(this->hSocket == -1)
            throw BaseException("impossible de creer un socket");        
}

void Sockets::Bind()
{
    int result;
    struct sockaddr_in adresseSocket;
    
    adresseSocket = this->getAdressByName("localhost");
    this->adresseSocket = adresseSocket;
    adresseSocket.sin_family = AF_INET;
    adresseSocket.sin_port = htons(PORT);
    printf("Adresse IP = %s\n",inet_ntoa(adresseSocket.sin_addr)); 

    //TODO: verifier que this->hSocket est set (valide)
    if (bind(this->hSocket, (struct sockaddr *)&adresseSocket, sizeof(struct sockaddr_in)) == -1)
    {
        throw BaseException("Erreur de bind");
    }
    else 
        printf("<OK> Bind adresse et port socket OK\n");
}

void Sockets::Listen(int maxConn)
{
    //TODO: verifier handler ? 
    listen(this->hSocket, maxConn);
}

Sockets Sockets::Accept()
{
    struct sockaddr_in adresse;
    int hSocketService;
    unsigned int tailleSockaddr_in = sizeof(struct sockaddr_in);
    hSocketService = accept(this->hSocket,(struct sockaddr*)&adresse, &tailleSockaddr_in);

    return Sockets(hSocketService, adresse);
}

sockaddr_in Sockets::getAdressByName(const char* hostName)
{
    struct sockaddr_in adresse;
    struct hostent *infosHost;
    if((infosHost = gethostbyname(hostName)) == 0)
    {
        printf("<Erreur> acquisition d'informations sur le host: %d\n", errno);
        throw BaseException("<Erreur> acquisition d'informations sur le host ");
    }
    else
        printf("<OK> acquisition d'informations sur le host\n");
    
    memcpy(&adresse.sin_addr, infosHost->h_addr, infosHost->h_length);

    return adresse;
}


int Sockets::gethSocket()
{
    return this->hSocket;
}


sockaddr_in Sockets::getAdresse()
{
    return this->adresseSocket;
}

bool Sockets::esLibre()
{
    return this->_libre;
}


void Sockets::setLibre(bool libre)
{
    this->_libre = libre;
}