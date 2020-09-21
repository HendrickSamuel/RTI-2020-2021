#include "SocketsServeur.h"

SocketsServeur::SocketsServeur()
{
    //this->create();
}

SocketsServeur::SocketsServeur(const SocketsServeur& old)
{
    this->hSocket = old.hSocket;
    this->adresseSocket = old.adresseSocket;
}

SocketsServeur::SocketsServeur(int hSocket, sockaddr_in adresse)
{
    this->hSocket = hSocket;
    this->adresseSocket = adresseSocket;
}

void SocketsServeur::bind(const char* host, int port)
{
    int result;
    struct sockaddr_in adresseSocket;
    
    adresseSocket = this->getAdressByName(host);
    adresseSocket.sin_family = AF_INET;
    adresseSocket.sin_port = htons(port);
    
    this->adresseSocket = adresseSocket;
    
    printf("Adresse IP = %s\n",inet_ntoa(adresseSocket.sin_addr)); 

    //TODO: verifier que this->hSocket est set (valide)
    if (bind(this->hSocket, (struct sockaddr *)&adresseSocket, sizeof(struct sockaddr_in)) == -1)
    {
        throw BaseException("Erreur de bind");
    }
    else 
        printf("<OK> Bind adresse et port socket OK\n");
}

void SocketsServeur::listen(int maxConn)
{
    //TODO: verifier handler ? 
    listen(this->hSocket, maxConn);
}

SocketsServeur SocketsServeur::accept()
{
    struct sockaddr_in adresse;
    int hSocketService;
    unsigned int tailleSockaddr_in = sizeof(struct sockaddr_in);
    hSocketService = accept(this->hSocket,(struct sockaddr*)&adresse, &tailleSockaddr_in);

    return SocketsServeur(hSocketService, adresse);
}


