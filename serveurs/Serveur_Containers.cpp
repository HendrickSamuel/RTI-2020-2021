/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/
#include "Sockets.h"
#include "Trace.h"
#include "BaseException.h"
#include <iostream>
#include <unistd.h> 

#include <stdio.h>     
#include <stdlib.h>

#include <pthread.h>

#define NB_MAX_CONNECTIONS 3

pthread_mutex_t mutexIndiceCourant;
pthread_cond_t condIndiceCourant;
int indiceCourant = -1;
pthread_t threads[NB_MAX_CONNECTIONS];
Sockets sockets[NB_MAX_CONNECTIONS];

void* fctThread(void *param);

using namespace std;
int main(int argc, char *argv[])
{
    Sockets socketEcoute;
    Sockets socketService;
    int i;
    int j;
    int ret;

    Affiche("Démarrage du thread principale: \n pid: %d \n tid: %u \n\n", getpid(), pthread_self());
    pthread_mutex_init(&mutexIndiceCourant, NULL);
    pthread_cond_init(&condIndiceCourant, NULL);

    for(i = 0; i < NB_MAX_CONNECTIONS; i++)
        sockets[i].setLibre(true);

    try
    {
        socketEcoute.Create();
        socketEcoute.Bind();
    }
    catch(BaseException& e)
    {
        Error(e.getMessage());
        return 1;
    }

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
        socketEcoute.Listen(SOMAXCONN);

        socketService = socketEcoute.Accept();  

        Affiche("Recherche d'une socket libre\n");

        for(j = 0; j < NB_MAX_CONNECTIONS && sockets[j].esLibre() != true; j++); //j'aime pas

        if(j == NB_MAX_CONNECTIONS)
        {
            printf("Plus de connexion disponible\n");
            //TODO: SEND message
        }
        else
        {
            printf("Connexion sur la socket %d\n", j);
            pthread_mutex_lock(&mutexIndiceCourant);
            sockets[j] = socketService;
            indiceCourant = j;
            pthread_mutex_unlock(&mutexIndiceCourant);
            pthread_cond_signal(&condIndiceCourant);
        }
        
    } while (true);
    
    close(socketEcoute.gethSocket());
    Affiche("Fermeture de la socket d'ecoute et du serveur");   
    
    return 0;
}

void * fctThread(void * param)
{
    int* identite = (int*)malloc(sizeof(int));
    *identite = *((int *)param);
    //int vr = (int)(param);
    Affiche("Thread n° %d demarre a la position %d \n", pthread_self(), *identite);
    Sockets hSocketService;
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
        Affiche("\033[1;36m<TASK>\033[0m Thread n° %d s'occupe du socket %d \n", pthread_self(), indiceClientTraite);

        finDialogue = false;
        do
        {
            /* code */
        } while (!finDialogue);

        pthread_mutex_lock(&mutexIndiceCourant);
        sockets[indiceClientTraite].setLibre(true);
        pthread_mutex_unlock(&mutexIndiceCourant);

    }
    close(hSocketService.gethSocket());
    return identite;
}