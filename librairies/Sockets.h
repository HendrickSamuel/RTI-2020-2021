/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/


#ifndef SOCKETS_H
#define SOCKETS_H

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <sys/socket.h>

int socketCreate(void);

int socketBind(int handler, sockaddr_in adresse);

int socketClose(int handler);

int socketShutdown(int handler, int sensFermeture);

int socketListen(int handler, int maxConnexion);

int socketAccept(int handler, sockaddr_in adresse);

int socketConnect(int handler, sockaddr_in adresse);

int socketSend(int handler, const void *message, int taille);

int socketRecieveStruct(int handler, void *message, int taille);

int socketRecieveString(int handler, void *message, int taille);

#endif //SOCKETS_H