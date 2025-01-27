/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#ifndef SOCKETSSERVEUR_H
#define SOCKETSSERVEUR_H

#include "Sockets.h"

class SocketsServeur : public Sockets
{
    private:

    public:
        //constructeurs
        SocketsServeur();
        SocketsServeur(int hSocket, sockaddr_in adresse);
        SocketsServeur(const SocketsServeur& old);

        //destructeurs

        //operators

        //getters

        //setters

        //méthodes
        SocketsServeur acceptSocket();
        void listenSocket(int maxConn);
        void initSocket(const char* host, int port);
        void bindSocket(const char* host, int port);
};

#endif //SOCKETSSERVEUR_H
