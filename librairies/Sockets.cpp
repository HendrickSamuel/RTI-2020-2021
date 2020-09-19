/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include "Sockets.h"

Sockets::Sockets()
{
    SocketCreate();
}

void Sockets::SocketCreate()
{
    this->hSocket = socket(AF_INET, SOCK_STREAM, 0);
        if(this->hSocket == -1)
            throw BaseException("impossible de creer un socket");        
}

sockaddr_in Sockets::getAdressByName(const char* hostName)
{
    sockaddr_in adresse;
    struct hostent *infosHost;
    if((infosHost = gethostbyname(hostName)) == 0)
    {
        printf("<Erreur> acquisition d'informations sur le host: %d\n", errno);
        throw BaseException("<Erreur> acquisition d'informations sur le host ");
    }
    else
        printf("<OK> acquisition d'informations sur le host\n");
    
    memcpy(&adresse, infosHost->h_addr, infosHost->h_length);
    
    return adresse;
}