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

#define PORT 5000

int main(int argc, char *argv[])
{
    int hSocketClient;
    struct hostent *infosHost;
    struct in_addr adresseIP;
    struct sockaddr_in adresseSocket;
    int ret;

    hSocketClient = socketCreate();

    if ( (infosHost = gethostbyname("localhost"))==0)
    {
        printf("Erreur d'acquisition d'infos sur le host distant %d\n", errno); exit(1);
    }
    else 
        printf("Acquisition infos host distant OK\n");
    
    memcpy(&adresseIP, infosHost->h_addr, infosHost->h_length);
    printf("Adresse IP = %s\n",inet_ntoa(adresseIP)); 

    memset(&adresseSocket, 0, sizeof(struct sockaddr_in));
    adresseSocket.sin_family = AF_INET; /* Domaine */
    adresseSocket.sin_port = htons(PORT); /* conversion port au format réseau */
    memcpy(&adresseSocket.sin_addr, infosHost->h_addr,infosHost->h_length); 

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