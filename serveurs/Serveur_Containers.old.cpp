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

#include <pthread.h>

#include "Sockets.h"
#include "Trace.h"

#include <stdbool.h>


#define NB_MAX_CONNECTIONS 3

pthread_mutex_t mutexIndiceCourant;
pthread_cond_t condIndiceCourant;
int indiceCourant = -1;
pthread_t threads[NB_MAX_CONNECTIONS];
int sockets[NB_MAX_CONNECTIONS];

void* fctThread(void *param);

int main(int argc, char *argv[])
{
    int hSocketEcoute;
    int hSocketService;
    struct sockaddr_in adresseSocket;
    int i;
    int j;
    int ret;

    /* ---- INITIALISATION DES THREADS ---- */
    printf("\033[1;32m<DEMARRAGE>\033[0m Démarrage du thread principale: \n %d.%u \n", getpid(), pthread_self());
    pthread_mutex_init(&mutexIndiceCourant, NULL);
    pthread_cond_init(&condIndiceCourant, NULL);

    for(i = 0; i < NB_MAX_CONNECTIONS; i++)
        sockets[i] = -1;
    
    hSocketEcoute = socketCreate();
    if ( socketBind(hSocketEcoute, "localhost") == -1)
    {
        Error("could not bind socket: %d \n", errno);
        exit(-1);
    }
    
    Affiche("Attente apres create et bind \n");
    getchar();

    /* ---- DEMARRAGE DES THREADS ---- */
    for(i = 0; i < NB_MAX_CONNECTIONS; i++)
    {
        ret = pthread_create(&threads[i], NULL, fctThread, (void*)&i);
        Affiche("Démarrage du thread secondaire %d\n", i);
        ret = pthread_detach(threads[i]);
    }


    do
    {
        socketListen(hSocketEcoute, SOMAXCONN);

        hSocketService = socketAccept(hSocketEcoute,&adresseSocket);
        if(hSocketService == -1)
        {
            Error("Erreur sur le accept de la socket %d\n", errno);
            exit(1);
        }
        else
        {
            Affiche("Connecté avec IP = %s\n",inet_ntoa(adresseSocket.sin_addr));
        }   

        Affiche("Recherche d'une socket libre\n");

        for(j = 0; j < NB_MAX_CONNECTIONS && sockets[j] != -1; j++); //j'aime pas

        if(j == NB_MAX_CONNECTIONS)
        {
            Error("Plus de connexion disponible\n");
            //TODO: SEND message
        }
        else
        {
            Affiche("Connexion sur la socket %d\n", j);
            pthread_mutex_lock(&mutexIndiceCourant);
            sockets[j] = hSocketService;
            indiceCourant = j;
            pthread_mutex_unlock(&mutexIndiceCourant);
            pthread_cond_signal(&condIndiceCourant);
        }
        
    } while (true);

    close(hSocketEcoute);
    Affiche("Fermeture de la socket d'ecoute et du serveur");   
    
    return 0;
}

void * fctThread(void * param)
{
    int* identite = (int*)malloc(sizeof(int));
    *identite = *((int *)param);
    //int vr = (int)(param);
    Affiche("Thread n° %d demarre a la position %d \n", pthread_self(), *identite);
    int hSocketService;
    bool finDialogue = false;
    int indiceClientTraite;

    while(true)
    {
        pthread_mutex_lock(&mutexIndiceCourant);
        while(indiceCourant == -1)
        {
            pthread_cond_wait(&condIndiceCourant, &mutexIndiceCourant);
        }
        indiceClientTraite = indiceCourant;
        indiceCourant = -1;

        hSocketService = sockets[indiceClientTraite];
        pthread_mutex_unlock(&mutexIndiceCourant);
        printf("\033[1;36m<TASK>\033[0m Thread n° %d s'occupe du socket %d \n", pthread_self(), indiceClientTraite);

        finDialogue = false;
        do
        {
            /* code */
        } while (!finDialogue);

        pthread_mutex_lock(&mutexIndiceCourant);
        sockets[indiceClientTraite] = -1;
        pthread_mutex_unlock(&mutexIndiceCourant);

    }
    close(hSocketService);
    return identite;
}