#ifndef SOCKETSSERVEUR_H
#define SOCKETSSERVEUR_H

#include "Sockets.h"

class SocketsServeur : public Sockets
{
    private:

    public:
        SocketsServeur();
        SocketsServeur(int hSocket, sockaddr_in adresse);
        SocketsServeur(const SocketsServeur& old);

        void Bind();
        void Listen(int maxConn);
        SocketsServeur Accept();
};

#endif