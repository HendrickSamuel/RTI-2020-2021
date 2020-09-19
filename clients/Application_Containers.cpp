/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include <errno.h>

#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netdb.h>

#include "Sockets.h"

int main(int argc, char *argv[])
{
    int hSocketClient;
    struct hostent *infosHost;
    struct in_addr adresseIP;
    struct sockaddr_in adresseSocket;
    int ret;

    hSocketClient = socketCreate();

    memset(&adresseSocket, 0, sizeof(struct sockaddr_in)); // préparation de la zone mémoire

    if(getAdressByName(&adresseSocket,"localhost") == 0)
    {
        // probleme de recup d'adresse
    }
    adresseSocket.sin_family = AF_INET;
    adresseSocket.sin_port = htons(PORT); 

    if (( ret = connect(hSocketClient, (struct sockaddr *)&adresseSocket, sizeof(struct sockaddr_in)))== -1)
    {
        printf("Erreur sur connect de la socket %d\n", errno);
        close(hSocketClient); exit(1);
    }
    else 
        printf("Connect socket OK\n");

    getchar();

    return 0;
}