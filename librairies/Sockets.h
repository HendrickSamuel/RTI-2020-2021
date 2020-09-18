/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/


#ifndef SOCKETS_H
#define SOCKETS_H

#define PORT 5000

#include <arpa/inet.h>

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include <errno.h>

#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>

int getAdressByName(sockaddr_in *adresse,const char* hostName);

int socketCreate(void);

int socketBind(int handler, const char* hostName);

int socketClose(int handler);

int socketShutdown(int handler, int sensFermeture);

int socketListen(int handler, int maxConnexion);

int socketAccept(int handler, sockaddr_in adresse);

int socketConnect(int handler, sockaddr_in adresse);

int socketSend(int handler, const void *message, int taille);

int socketRecieveStruct(int handler, void *message, int taille);

int socketRecieveString(int handler, void *message, int taille);

#endif //SOCKETS_H