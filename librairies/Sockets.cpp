/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include "Sockets.h"

/**/

//TODO: revoir utilité de toutes le fonctions

//FIXME: it's broken


int socketCreate(void)
{
    int handler;

    handler = socket(AF_INET, SOCK_STREAM, 0);
    //if(debug)
    //{
        if(handler == -1)
        {
            printf("Erreur de creation de la socket %d\n", errno);
        }
        else
        {
            printf("Creation de la socket OK\n");
        }  
    //}

    return handler;
}



int socketBind(int handler, sockaddr_in adresse)
{



}



int socketClose(int handler)
{


}


int socketShutdown(int handler, int sensFermeture)
{


}


int socketListen(int handler, int maxConnexion)
{



}


int socketAccept(int handler, sockaddr_in adresse, int taille)
{


}


int socketConnect(int handler, sockaddr_in adresse)
{


}


int socketSend(int handler, const void *message, int taille)
{


}


int socketRecieveStruct(int handler, void *message, int taille)
{


}

int socketRecieveString(int handler, void *message, int taille)
{


}