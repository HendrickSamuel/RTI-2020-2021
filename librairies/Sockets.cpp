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

void Sockets::Close()
{
    close(this->hSocket);
}

void Sockets::ReceiveStruct(void* structure, int taille)
{
    Warning("taille que l'on attend: %d\n", taille);
    int tailleMessageRecu = 0;
    int nbreBytesLus = 0;
    int nbreBytesRecus = 0;
    do 
    {
        if((nbreBytesRecus = recv(this->hSocket,((char*)structure)+tailleMessageRecu, taille-tailleMessageRecu,0)) == -1)
        {
            Affiche("Error","Tout le message n'a pas su etre lu: %d", errno);
        }
        else
        {
            if(nbreBytesRecus == 0)
                throw BaseException("Le client est parti");
            tailleMessageRecu += nbreBytesRecus;
            Affiche("INFO","Taille message recu = %d et taille attendue %d \n",tailleMessageRecu, taille);
        }
    }
    while(nbreBytesRecus!=0 && nbreBytesRecus!=-1 && tailleMessageRecu!= taille);

    //Affiche("test","Tout le message à été lu \n");
    printf("Tout le message à été lu \n");
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