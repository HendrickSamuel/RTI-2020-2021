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

#include "BaseException.h"

#define PORT 5000

using namespace std;
class Sockets
{
    protected: 
        int hSocket;

    public: 
        Sockets();

        void SocketCreate();
        sockaddr_in getAdressByName(const char* hostName);

};

#endif