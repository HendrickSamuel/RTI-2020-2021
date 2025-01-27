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
        switch (errno)
        {
            case HOST_NOT_FOUND: cout << "<Erreur> HOST_NOT_FOUND" << endl; break;
            case NO_DATA: cout << "<Erreur> NO_DATA" << endl; break;
            case NO_RECOVERY: cout << "<Erreur> NO_RECOVERY" << endl; break;
            case TRY_AGAIN: cout << "<Erreur> TRY_AGAIN" << endl; break;
        }
        
        throw BaseException("<Erreur> acquisition d'informations sur le host ");
    }
    else
        cout << "<OK> acquisition d'informations sur le host" << endl;
    
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
	if(send(gethSocket(), message, taille,0) == -1)
    {
        switch (errno)
        {
            case EBADF: cout << "<Erreur> BAD FILE DESCRIPTOR" << endl; break;
            case ENOTSOCK: cout << "<Erreur> SOCKET OPERATION ON NON SOCKET" << endl; break;
            case EINTR: cout << "<Erreur> EINTR" << endl; break;
        }
        throw BaseException("<Erreur> Le message n'a pas pu etre envoye ");      
    }	
    else
    {
    	printf("<OK> Le message a bien été envoye\n");
    }
}

void Sockets::sendStruct(void* structure, int taille)
{
	if(send(gethSocket(), structure, taille,0) == -1)
    {
        switch (errno)
        {
            case EBADF: cout << "<Erreur> BAD FILE DESCRIPTOR" << endl; break;
            case ENOTSOCK: cout << "<Erreur> SOCKET OPERATION ON NON SOCKET" << endl; break;
            case EINTR: cout << "<Erreur> EINTR" << endl; break;
        }
        throw BaseException("<Erreur> Le message n'a pas pu etre envoye ");      
    }	
    else
    {
    	printf("<OK> Le message a bien été envoye\n");
    }
}

char* Sockets::receiveString(int taille, char marq1, char marq2)
{
    char *message = (char*) malloc(taille);
    bool fin = false;
    char buff[taille];
    int nbrBytesRecus = 0;
    int tailleMessageRecu = 0;

    memset(buff,0,sizeof(buff));
    do
    {
        if((nbrBytesRecus = recv(this->hSocket, buff, taille, 0)) == -1)
        {
            switch (errno)
            {
                case EBADF: cout << "<Erreur> BAD FILE DESCRIPTOR" << endl; break;
                case ENOTSOCK: cout << "<Erreur> SOCKET OPERATION ON NON SOCKET" << endl; break;
                case EINTR: cout << "<Erreur> EINTR" << endl; break;
            }
            Affiche("Error","Tout le message n'a pas su etre lu: %d\n", errno);
            //TODO: fermer socket ou qqch ? 
        }
        else
        {
            if(nbrBytesRecus == 0)
            {
                throw BaseException("La connection a ete fermee");
            }

            fin = marqueurRecu(buff, nbrBytesRecus, marq1, marq2);

            memcpy((char*)message+tailleMessageRecu,buff,nbrBytesRecus);
            tailleMessageRecu+=nbrBytesRecus;
        }
        
    } while (nbrBytesRecus!=0 && nbrBytesRecus!=-1 && !fin);

    message[tailleMessageRecu] = '\0';
    
    return message;
}

void Sockets::receiveStruct(void* structure, int taille)
{
    Warning("taille","taille que l'on attend: %d\n", taille);
    int tailleMessageRecu = 0;
    int nbreBytesRecus = 0;
    do
    {
        Affiche("SOCKET","numero: %d", this->hSocket);
        if((nbreBytesRecus = recv(this->hSocket,((char*)structure)+tailleMessageRecu, taille-tailleMessageRecu,0)) == -1)
        {
            switch (errno)
            {
                case EBADF: cout << "<Erreur> BAD FILE DESCRIPTOR" << endl; break;
                case ENOTSOCK: cout << "<Erreur> SOCKET OPERATION ON NON SOCKET" << endl; break;
                case EINTR: cout << "<Erreur> EINTR" << endl; break;
            }
            Affiche("Error","Tout le message n'a pas su etre lu: %d\n", errno);
            //TODO: fermer socket ou qqch ?
        }
        else
        {
            if(nbreBytesRecus == 0)
            {
                throw BaseException("La connection a ete fermee");
            }
            tailleMessageRecu += nbreBytesRecus;
            Affiche("INFO","Taille message recu = %d et taille attendue %d \n",tailleMessageRecu, taille);
        }
    }
    while(nbreBytesRecus!=0 && nbreBytesRecus!=-1 && tailleMessageRecu!= taille);

    Affiche("OK","Tout le message à été lu \n");
}


bool Sockets::marqueurRecu(char* mess, int nbrRecu, char marq1, char marq2)
{ 
    /* Recherche de la sequence marq1marq2 */ 
    static char demiTrouve = 0;
    int i;
    char trouve = 0;
    
    if (demiTrouve==1 && mess[0]==marq2)
    {
        return true;
    }
    else
    { 
        demiTrouve = 0;
    } 
    
    for( i = 0 ; i < nbrRecu-1 && !trouve ; i++ ) 
    {
        if( mess[i] == marq1 && mess[i+1] == marq2 )
        {
            trouve = 1;
        }
    } 
    
    if(trouve)
    {
        return true;
    }
    else if( mess[nbrRecu] == marq1 ) 
    {
        demiTrouve = 1;
        return false;
    }
    else
    {
        return false; 
    }
}