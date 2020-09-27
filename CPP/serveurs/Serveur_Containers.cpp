/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include "CMMP.h"
#include "Trace.h"
#include "Configurator.h"
#include <stdio.h>     
#include <iostream>
#include <unistd.h> 
#include <stdlib.h>
#include <pthread.h>
#include "BaseException.h"
#include "SocketsServeur.h"

#define NB_MAX_CONNECTIONS 3

int indiceCourant = -1;
pthread_cond_t condIndiceCourant;
pthread_mutex_t mutexIndiceCourant;
pthread_t threads[NB_MAX_CONNECTIONS];
SocketsServeur sockets[NB_MAX_CONNECTIONS];

void* fctThread(void *param);

using namespace std;

int main(int argc, char *argv[])
{
    int i;
    int j;
    SocketsServeur socketEcoute;
    SocketsServeur socketService;
    char *portTmp;
    int port;
    char *adresse;

    /* lecture des parametres sur fichier */
    try
    {
        portTmp = Configurator::getProperty("test.conf","PORT");
        adresse = Configurator::getProperty("test.conf","HOSTNAME");
        if(portTmp == NULL || adresse == NULL)
        {
            exit(0);
        }
        
        port = atoi(portTmp);
    }
    catch(BaseException e)
    {
        std::cerr << e.getMessage() << '\n';
        exit(0);
    }
    

    Affiche("1","Démarrage du thread principale: \n pid: %d \n tid: %u \n\n", getpid(), pthread_self());
    pthread_mutex_init(&mutexIndiceCourant, NULL);
    pthread_cond_init(&condIndiceCourant, NULL);

    for(i = 0; i < NB_MAX_CONNECTIONS; i++)
        sockets[i].setLibre(true);

    try
    {
        socketEcoute.initSocket(adresse, port);
    }
    catch(BaseException& e)
    {
        Error("Error","%s\n",e.getMessage());
        return 1;
    }

    /* ---- DEMARRAGE DES THREADS ---- */
    for(i = 0; i < NB_MAX_CONNECTIONS; i++)
    {
        pthread_create(&threads[i], NULL, fctThread, (void*)&i);
        Affiche("TEST","Démarrage du thread secondaire %d\n",i);
        pthread_detach(threads[i]);
    }

    do
    {
        socketEcoute.listenSocket(SOMAXCONN);

        socketService = socketEcoute.acceptSocket();  

        Affiche("","Recherche d'une socket libre\n");

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
    
    socketEcoute.closeSocket();
    Affiche("","Fermeture de la socket d'ecoute et du serveur");   
    
    return 0;
}

void * fctThread(void * param)
{
    struct protocole proto;
    int* identite = (int*)malloc(sizeof(int));
    *identite = *((int *)param);
    //int vr = (int)(param);

    Affiche("thread","Thread n° %d demarre a la position %d \n", pthread_self(), *identite);
    
    SocketsServeur hSocketService;
    bool finDialogue = false;
    bool connect = false;
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
        Affiche("test","\033[1;36m<TASK>\033[0m Thread n° %d s'occupe du socket %d \n", pthread_self(), indiceClientTraite);

        finDialogue = false;
        do
        {
            try
            {
                hSocketService.receiveStruct(&proto, sizeof(struct protocole));
            }
            catch(BaseException e)
            {
                finDialogue = true;
                connect = false;
                std::cerr << e.getMessage() << '\n';
            }

            /*condition pour voir si le login à deja été effectué*/
            if(connect == true || proto.type == 1)
            {
                switch(proto.type)
                {
                    case 1:
                        if(Configurator::getLog("login.csv", proto.donnees.login.nom, proto.donnees.login.pwd))
                        {
                            proto.donnees.reponse.succes = true;
                            connect = true;
                        }
                        else
                        {
                            proto.donnees.reponse.succes = false;
                            strcpy(proto.donnees.reponse.message, "Login ou mot de passe incorrect");
                        }
                    break;

                    case 2:
                        //TODO:apres ce message le serveur attendra un INPUT-DONE !!! et pas un autre
                        //TODO:recherche si emplacement libre
                        if(true)
                        {
                            //TODO:enregistrement dans FICH_PARK
                            proto.donnees.reponse.succes = true;
                            cout << "X : ";
                            cin >> proto.donnees.reponse.x;
                            cout << "Y : ";
                            cin >> proto.donnees.reponse.y;
                        }
                        else
                        {
                            proto.donnees.reponse.succes = false;
                            strcpy(proto.donnees.reponse.message, "Pas d'emplacements libres");
                        }
                        break;

                    case 3:
                        //TODO:si container OK
                        if(true) 
                        {
                            //TODO:enregistrement dans FICH_PARK
                            proto.donnees.reponse.succes = true;
                        }
                        else
                        {
                            proto.donnees.reponse.succes = false;
                            strcpy(proto.donnees.reponse.message, "Container non conforme");

                        }
                        break;

                    case 4:
                        //TODO:si il y a des container pour cette destination
                        // recherche dans FICH_PARK
                        if(true) 
                        {
                            //TODO:renvoyer la liste des containers d'apres FICH_PARK
                            proto.donnees.reponse.succes = true;
                        }
                        else
                        {
                            proto.donnees.reponse.succes = false;
                            strcpy(proto.donnees.reponse.message, "Pas de container pour cette destination");

                        }  
                        break;

                    case 5:
                        //TODO:recherche du container s'il existe
                        // recherche dans FICH_PARK
                        if(true) 
                        {
                            //TODO:mise a jour de FICH_PARK
                            proto.donnees.reponse.succes = true;
                        }
                        else
                        {
                            proto.donnees.reponse.succes = false;
                            strcpy(proto.donnees.reponse.message, "Container inconnu");

                        }
                        break;

                    case 6:
                        //TODO:verifier que le transporteur est bien plein
                        if(true) 
                        {
                            proto.donnees.reponse.succes = true;
                        }
                        else
                        {
                            proto.donnees.reponse.succes = false;
                            strcpy(proto.donnees.reponse.message, "Incoherence detectee : place encore disponible");

                        }
                        break;

                    case 7:
                        if(Configurator::getLog("login.csv", proto.donnees.login.nom, proto.donnees.login.pwd))
                        {
                            proto.donnees.reponse.succes = true;
                            connect = false;
                            finDialogue = true;
                        }
                        else
                        {
                            proto.donnees.reponse.succes = false;
                            strcpy(proto.donnees.reponse.message, "Logout ou mot de passe incorrect");
                        }  
                        break;
                }
            }
            else
            {
                proto.donnees.reponse.succes = false;
                strcpy(proto.donnees.reponse.message, "Vous devez etre connecte pour cette action");
            }
            


            try
            {
                hSocketService.sendStruct((void*)&proto, sizeof(struct protocole));
            }
            catch(BaseException e)
            {
                std::cerr << e.getMessage() << '\n';
            }

        } while (!finDialogue);

        hSocketService.closeSocket();

        pthread_mutex_lock(&mutexIndiceCourant);
        sockets[indiceClientTraite].setLibre(true);
        pthread_mutex_unlock(&mutexIndiceCourant);
    }
    hSocketService.closeSocket();
    return identite;
}
