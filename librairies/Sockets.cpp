/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include "Sockets.h"

/********************************/
/*                              */
/*         Constructeurs        */
/*                              */
/********************************/

Sockets::Sockets()
{
    //this->create();
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


/********************************/
/*                              */
/*          Destructeurs        */
/*                              */
/********************************/

/********************************/
/*                              */
/*           Operators          */
/*                              */
/********************************/

/********************************/
/*                              */
/*            Getters           */
/*                              */
/********************************/

bool Sockets::esLibre()
{
    return this->libre;
}

int Sockets::gethSocket()
{
    return this->hSocket;
}

sockaddr_in Sockets::getAdresse()
{
    return this->adresseSocket;
}

sockaddr_in Sockets::getAdressByName(const char* hostName)
{
    struct sockaddr_in adresse;
    struct hostent *infosHost;

    //TODO: peut etre memset à 0 ? 
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


/********************************/
/*                              */
/*            Setters           */
/*                              */
/********************************/

void Sockets::setLibre(bool libre)
{
    this->libre = libre;
}


/********************************/
/*                              */
/*            Methodes          */
/*                              */
/********************************/

void Sockets::closeSocket()
{
    close(this->hSocket);
}

void Sockets::createSocket()
{
    this->hSocket = socket(AF_INET, SOCK_STREAM, 0);
        if(this->hSocket == -1)
            throw BaseException("impossible de creer un socket");        
}

void Sockets::sendString(char* message, int taille)
{

}

void Sockets::sendStruct(void* structure, int taille)
{

	if(send(gethSocket(), structure, taille,0) == -1)
    {
        printf("<Erreur> Le message n'a pas pu etre envoye: %d\n", errno);
        throw BaseException("<Erreur> Le message n'a pas pu etre envoye ");      
    }	
    else
    	printf("<OK> Le message a bien été envoye\n");

}

void Sockets::recieveString(char* message, int taille)
{
    char buff[taille];
    int tailleMessageRecu = 0;
    bool fin = false;
    int nbrBytesRecus = 0;

    memset(buff,0,sizeof(buff));
    do
    {
        if((nbrBytesRecus = recv(this->hSocket, buff, taille, 0)) == -1)
        {
            Affiche("Error","Tout le message n'a pas su etre lu: %d\n", errno);
            //TODO: fermer socket ou qqch ? 
        }
        else
        {
            //TODO: verifier si le buffer contient les chars de fin de chaine
            if(nbrBytesRecus == 0)
            {
                throw BaseException("Le client est parti");
            }
            memcpy((char*)message+tailleMessageRecu,buff,nbrBytesRecus);
            tailleMessageRecu+=nbrBytesRecus;
        }
        
    } while (nbrBytesRecus!=0 && nbrBytesRecus!=-1 && !fin);
    
    message[tailleMessageRecu-2]=0; // \0 ?
    memcpy(message, buff, sizeof(buff)); //mettre dans message le contenu de buff
}

void Sockets::receiveStruct(void* structure, int taille)
{
    Warning("taille","taille que l'on attend: %d\n", taille);
    int tailleMessageRecu = 0;
    int nbreBytesLus = 0;
    int nbreBytesRecus = 0;
    do 
    {
        Affiche("SOCKET","numero: %d", this->hSocket);
        if((nbreBytesRecus = recv(this->hSocket,((char*)structure)+tailleMessageRecu, taille-tailleMessageRecu,0)) == -1)
        {
            Affiche("Error","Tout le message n'a pas su etre lu: %d\n", errno);
            //TODO: fermer socket ou qqch ?
        }
        else
        {
            if(nbreBytesRecus == 0)
            {
                throw BaseException("Le client est parti");
            }
            tailleMessageRecu += nbreBytesRecus;
            Affiche("INFO","Taille message recu = %d et taille attendue %d \n",tailleMessageRecu, taille);
        }
    }
    while(nbreBytesRecus!=0 && nbreBytesRecus!=-1 && tailleMessageRecu!= taille);

    //Affiche("test","Tout le message à été lu \n");
    printf("Tout le message à été lu \n");
}
