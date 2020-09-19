/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include <arpa/inet.h>

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include <errno.h>

#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>

#include "Sockets.h"
#include "Trace.h"

int main(int argc, char *argv[])
{
    int hSocketEcoute;
    int hSocketService;
    struct sockaddr_in adresseSocket;

    hSocketEcoute = socketCreate();
    if ( socketBind(hSocketEcoute, "localhost") == -1)
    {
        Affiche("could not bind socket: %d \n", errno);
        //printf("\033[0;31m<FATAL ERROR>\033[0m could not bind socket: %d \n", errno);
        exit(-1);
    }
    
    //printf("\033[0;34m Attente apres create \033[0m\n");
    Affiche("Attente apres create \n");
    getchar();

    socketListen(hSocketEcoute, SOMAXCONN);

    printf("\033[0;34m Attente apres listen \033[0m\n");
    getchar();

    hSocketService = socketAccept(hSocketEcoute,&adresseSocket);
    if(hSocketService == -1)
    {
        printf("<Erreur> sur le accept de la socket %d\n", errno);
        exit(1);
    }
    else
        printf("<OK> Accept fait\n"); 
        printf("Connecté avec IP = %s\n",inet_ntoa(adresseSocket.sin_addr));

    printf("\033[0;34m Attente apres accept \033[0m\n");
    getchar();
    
    return 0;
}
