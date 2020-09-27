/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#ifndef SOCKETS_H
#define SOCKETS_H

#include "CMMP.h"
#include "Trace.h"
#include <errno.h>
#include <stdio.h>
#include <netdb.h>
#include <iostream>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include "BaseException.h"


#define EOC "END_OF_CONNEXION"
#define DOC "DENY_OF_CONNEXION"

using namespace std;

class Sockets
{
    protected: 
        int port;
        int hSocket;
        bool libre;
        struct sockaddr_in adresseSocket;

    public:
        //constructeurs
        Sockets();
        Sockets(int hSocket, sockaddr_in adresse);
        Sockets(const Sockets& old);

        //destructeurs
        
        //operators

        //getters
        bool esLibre();
        int gethSocket();
        sockaddr_in getAdresse();
        sockaddr_in getAdressByName(const char* hostName);

        //setters
        void setLibre(bool libre);

        //méthodes
        void closeSocket();
        void createSocket();
        void sendString(char* message, int taille);
        void sendStruct(void* structure, int taille);
        void recieveString(char* message, int taille);
        void receiveStruct(void* structure, int taille); 
        virtual void initSocket(const char* host, int port) = 0;

        //en commun des sockets create - getadresse - send - receive - close - shutdown
};

#endif //SOCKETS_H
