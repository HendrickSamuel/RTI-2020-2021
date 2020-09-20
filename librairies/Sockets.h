/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#ifndef SOCKETS_H
#define SOCKETS_H

#include <iostream>
#include <arpa/inet.h>
#include <errno.h>
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <unistd.h>
#include "BaseException.h"
#include "Trace.h"


#define PORT 5000
#define EOC "END_OF_CONNEXION"
#define DOC "DENY_OF_CONNEXION"

using namespace std;

class Sockets
{
    protected: 
        int hSocket;
        struct sockaddr_in adresseSocket;
        bool _libre;

    public:
        //constructeurs
        Sockets();
        Sockets(int hSocket, sockaddr_in adresse);
        Sockets(const Sockets& old);

        //destructeurs
        
        //operators

        //getters
        sockaddr_in getAdressByName(const char* hostName);
        int gethSocket();
        sockaddr_in getAdresse();
        bool esLibre();

        //setters
        void setLibre(bool libre);

        //méthodes
        void Create();
        void Close();
        void ReceiveStruct(void* structure, int taille);


        //en commun des sockets create - getadresse - send - receive - close - shutdown
};

#endif