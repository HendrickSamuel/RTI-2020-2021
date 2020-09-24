/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#ifndef SOCKETSCLIENT_H
#define SOCKETSCLIENT_H

#include "Sockets.h"

class SocketsClient : public Sockets
{
    private:
        
    public:
        //constructeurs
        SocketsClient();
        SocketsClient(int hSocket, sockaddr_in adresse);
        SocketsClient(const SocketsClient& old);

        //destructeurs

        //operators

        //getters

        //setters

        //méthodes
        void connectSocket(const char* host, int port);
        void initSocket(const char* host, int port);

};

#endif //SOCKETSCLIENT_H
