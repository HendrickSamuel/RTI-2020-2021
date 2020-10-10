/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include "CMMP.h"
#include "Trace.h"
#include "liste.h"
#include <stdio.h>
#include "Output.h"
#include <iostream>
#include <unistd.h> 
#include <stdlib.h>
#include <pthread.h>
#include "ParcAcces.h"
#include "StructParc.h"
#include "Configurator.h"
#include "BaseException.h"
#include "SocketsServeur.h"

#include <netinet/in.h>
#include <netinet/tcp.h>

//#define NB_MAX_CONNECTIONS 1

using namespace std;

/********************************/
/*          Structure           */
/********************************/

typedef struct
{
	bool connect;                   //Booléen pour voir si le client est bien connecte
    char* message;                  //Message que l'on crée dans la fonction switchThread et qui sera envoyée sur le réseau
	bool finDialog;                 //Booléen pour la fin de dialogue avec le client
    char nom[MAXSTRING];            //Nom du client
    char idTrans[MAXSTRING];        //Id du transporteur
    typeRequete dernOpp;            //Dernière oppération effectuée par le client
    Liste<char*> idCont;            //Liste des containers chargé sur le transporteur
    struct fich_parc tmpContaineur; //Containeur temporaire le temps de l'ajout dans le fichier (fait en plusieurs étapes)
    int capacite;                   //Capacite du transporteur avec Output ready
    int nbrEnv;                     //Nombre de container envoyé après le Output ready
}S_THREAD;


/********************************/
/*      Variables globales      */
/********************************/

int indiceCourant = -1;

pthread_mutex_t mutexFichParc;
pthread_mutex_t mutexIndiceCourant;

pthread_cond_t condIndiceCourant;

pthread_t* threads;
SocketsServeur* sockets;

// Déclaration de la clé et du controleur
pthread_key_t cle;
pthread_once_t controleur = PTHREAD_ONCE_INIT;

/********************************/
/*          Prototypes          */
/********************************/

//Threads
void* fctThread(void *param);

//Fonctions
void InitCle();
void freePTMess();
void destructeur(void *p);
void switchThread(protocole &proto);


/********************************/
/*             Main             */
/********************************/

int main(int argc, char *argv[])
{
    int i;
    int j;
    SocketsServeur socketEcoute;
    SocketsServeur socketService;
    char *ThreadsMaxTmp;
    int ThreadsMax;
    char *portTmp;
    int port;
    char *adresse;

    //debut bidon
    parcAcces pa = parcAcces("FICH_PARC.dat");
    pa.setBidon();


    //fin bidon

    /* lecture des parametres sur fichier */
    try
    {
        ThreadsMaxTmp = Configurator::getProperty("test.conf","MAX-THREADS");
        cout << "Test " << ThreadsMaxTmp << endl;
        ThreadsMax = atoi(ThreadsMaxTmp);

        cout << "APPLICATION: " << ThreadsMax << " Threads au démarrage " << endl;

        threads = new pthread_t[ThreadsMax];
        sockets = new SocketsServeur[ThreadsMax];
        
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
    pthread_mutex_init(&mutexFichParc, NULL);
    pthread_mutex_init(&mutexIndiceCourant, NULL);
    pthread_cond_init(&condIndiceCourant, NULL);

    for(i = 0; i < ThreadsMax; i++)
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
    for(i = 0; i < ThreadsMax; i++)
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

        for(j = 0; j < ThreadsMax && sockets[j].esLibre() != true; j++); //j'aime pas

        if(j == ThreadsMax)
        {
            printf("Plus de connexion disponible\n");
            char* msgRetour;
            msgRetour = (char*)malloc(255);
            strcpy(msgRetour, "0#false#Plus de ressources disponibles#%");
            socketService.sendString(msgRetour, strlen(msgRetour));
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


/********************************/
/*          Fonctions           */
/********************************/

//Fonction d'initialisation de la clé pour la zone spécifique
void InitCle()
{
	printf("Initialisation de cle (zone specifique)\n");
	pthread_key_create(&cle, destructeur);
}


//Fonction qui libère la mémoire alouée sur laquelle pointe la zone spécifique
void destructeur(void *p)
{
	printf("Liberation d'une zone specifique\n");
	free(p);
}


//Fonction qui libère la mémoire du message dans la zone spécifique
void freePTMess()
{
    S_THREAD *PT = NULL;
	
	PT = (S_THREAD*)pthread_getspecific(cle);

    if(PT->message != NULL)
    {
        free(PT->message);
    }
}


//Fonction qui traite le protocole reçu par réseau dans le switch
void switchThread(protocole &proto)
{
    S_THREAD *PT = NULL;
	
	PT = (S_THREAD*)pthread_getspecific(cle);

    cout << "requete recue : " << proto.type << " - ancienne requete : " << PT->dernOpp << endl;

    //vérification pour voir si la requête reçue correspond ou non au choix de la requête précédente
    if((proto.type == Login && (PT->dernOpp != InputTruck && PT->dernOpp != OutputReady && PT->dernOpp != OutputOne )) ||
        (proto.type == InputTruck && (PT->dernOpp != InputTruck && PT->dernOpp != OutputReady && PT->dernOpp != OutputOne )) || 
        (proto.type == InputDone && PT->dernOpp == InputTruck ) ||
        (proto.type == OutputReady && (PT->dernOpp != InputTruck && PT->dernOpp != OutputReady && PT->dernOpp != OutputOne )) ||
        (proto.type == OutputOne && (PT->dernOpp == OutputReady || PT->dernOpp == OutputOne )) ||
        (proto.type == OutputDone && PT->dernOpp == OutputOne ) ||
        (proto.type == Logout && (PT->dernOpp != InputTruck && PT->dernOpp != OutputReady && PT->dernOpp != OutputOne )))
    { 

        PT->dernOpp = proto.type;

        switch(proto.type)
        {
            case Login:
                {
                    if(PT->connect == false)
                    {
                        if(Configurator::getLog("login.csv", proto.donnees.login.nom, proto.donnees.login.pwd))
                        {
                            freePTMess();
                            strcpy(PT->nom, proto.donnees.login.nom);
                            PT->message = new char[strlen(PT->nom)+10];
                            strcpy(PT->message, "1#true#");
                            strcat(PT->message, PT->nom);
                            strcat(PT->message, "#%");
                            PT->connect = true;
                        }
                        else
                        {
                            freePTMess();
                            PT->message = new char[strlen("1#false#Login ou mot de passe incorrect#%")+1];
                            strcpy(PT->message, "1#false#Login ou mot de passe incorrect#%");
                            PT->dernOpp = Init;
                        }
                    }
                    else
                    {
                        freePTMess();
                        PT->message = new char[strlen("1#false#Vous etes deja connecte#%")+1];
                        strcpy(PT->message, "1#false#Vous etes deja connecte#%");    
                        PT->dernOpp = Init;       
                    }  
                }  
                break;

            case InputTruck:
                {
                    parcAcces fich_parc("FICH_PARC.dat");
                    
                    //recherche si emplacement libre
                    pthread_mutex_lock(&mutexFichParc);
                    if(fich_parc.searchPlace(&(PT->tmpContaineur)))
                    {
                        strcpy(PT->tmpContaineur.id, proto.donnees.inputTruck.idContainer);
                        PT->tmpContaineur.flagemplacement = 1;
                        
                        //enregistrement dans FICH_PARC
                        fich_parc.updateRecord(PT->tmpContaineur);
                        pthread_mutex_unlock(&mutexFichParc);

                        proto.donnees.reponse.x = PT->tmpContaineur.x;
                        proto.donnees.reponse.y = PT->tmpContaineur.y;
                        strcpy(proto.donnees.reponse.message, "Voice la place reservee en ");

                        freePTMess();
                        PT->message = new char[strlen("2#true#XXX/YYY#%")+1];
                        strcpy(PT->message, "2#true#");
                        char xy [10];
                        sprintf(xy, "%d", PT->tmpContaineur.x);
                        strcat(PT->message, xy);
                        strcat(PT->message, "/");
                        sprintf(xy, "%d", PT->tmpContaineur.y);
                        strcat(PT->message, xy);
                        strcat(PT->message, "#%");
                    }
                    else
                    {
                        pthread_mutex_unlock(&mutexFichParc);
                        PT->dernOpp = Init;
                        freePTMess();
                        PT->message = new char[strlen("2#false#Pas d'emplacements libres#%")+1];
                        strcpy(PT->message, "2#false#Pas d'emplacements libres#%");        
                    }
                }
                break;

            case InputDone:
                {  
                    parcAcces fich_parc("FICH_PARC.dat");
                    
                    if(proto.donnees.inputDone.etat == true)
                    {
                        //verif si le poids du container est OK
                        if(proto.donnees.inputDone.poids <= atof(Configurator::getProperty("test.conf","POIDS"))) 
                        {
                            
                            PT->tmpContaineur.poids = proto.donnees.inputDone.poids;
                            //enregistrement dans FICH_PARC
                            pthread_mutex_lock(&mutexFichParc);
                            fich_parc.updateRecord(PT->tmpContaineur);
                            pthread_mutex_unlock(&mutexFichParc);

                            freePTMess();
                            PT->message = new char[strlen("3#true#Container enregistre#%")+1];
                            strcpy(PT->message, "3#true#Container enregistre#%");
                        }
                        else
                        {
                            PT->tmpContaineur.flagemplacement = 0;
                            
                            //libere la place dans FICH_PARC
                            pthread_mutex_lock(&mutexFichParc);
                            fich_parc.updateRecord(PT->tmpContaineur);
                            pthread_mutex_unlock(&mutexFichParc);                       
                            
                            freePTMess();
                            PT->message = new char[strlen("3#false#Container non conforme#%")+1];
                            strcpy(PT->message, "3#false#Container non conforme#%");
                            PT->dernOpp = Init;
                        }
                    }
                    else
                    {
                        PT->tmpContaineur.flagemplacement = 0;
                        
                        //libere la place dans FICH_PARC
                        pthread_mutex_lock(&mutexFichParc);
                        fich_parc.updateRecord(PT->tmpContaineur);
                        pthread_mutex_unlock(&mutexFichParc);
                        
                        freePTMess();
                        PT->message = new char[strlen("3#false##%")+strlen(PT->nom)+1];
                        strcpy(PT->message, "3#false#");
                        strcat(PT->message, PT->nom);
                        strcat(PT->message, "#%");
                        PT->dernOpp = Init;
                    }
                }
                break;

            case OutputReady:
                {
                    char *liste = NULL;
                    parcAcces fich_parc("FICH_PARC.dat");

                    // recherche dans FICH_PARC
                    pthread_mutex_lock(&mutexFichParc);
                    liste = fich_parc.searchDestination(proto.donnees.outputReady.dest, &(PT->nbrEnv));
                    pthread_mutex_unlock(&mutexFichParc);

                    cout << "test" << endl;

                    if(liste != NULL) 
                    {
                        freePTMess();
                        PT->message = new char[strlen("4#true#%")+strlen(liste)+1];
                        strcpy(PT->message, "4#true#");
                        strcat(PT->message, liste);
                        strcat(PT->message, "#%");
                        PT->capacite = proto.donnees.outputReady.capacite;
                        strcpy(PT->idTrans, proto.donnees.outputReady.id);
                        free(liste);
                    }
                    else
                    {
                        freePTMess();
                        PT->message = new char[strlen("4#false#Pas de container pour cette destination#%")+1];
                        strcpy(PT->message, "4#false#Pas de container pour cette destination#%");
                        PT->dernOpp = Init;
                    } 
                }
                break;

            case OutputOne:
                {
                    if(PT->capacite == PT->idCont.getNombreElements())
                    {
                        freePTMess();
                        PT->message = new char[strlen("5#false#Vous n'avez plus de place#%")+1];
                        strcpy(PT->message, "5#false#Vous n'avez plus de place#%");
                    }
                    else
                    {
                        char *id = (char*)malloc(18);
                        strcpy(id, proto.donnees.outputOne.idContainer);
                        PT->idCont.insere(id);
                        freePTMess();
                        PT->message = new char[strlen("5#true#Deplacement de container enregistre#%")+1];
                        strcpy(PT->message, "5#true#Deplacement de container enregistre#%");       
                    }
                    
                }
                break;

            case OutputDone:
                {
                    if(PT->idCont.getNombreElements() == PT->capacite || PT->idCont.getNombreElements() == PT->nbrEnv) 
                    {
                        char *id;
                        struct fich_parc record;
                        parcAcces fich_parc("FICH_PARC.dat"); 

                        struct Cellule<char*> *cel = PT->idCont.getTete();

                        //On retire les containers de FICH_PARC
                        pthread_mutex_lock(&mutexFichParc);                     
                        do
                        {
                            id = cel->valeur;
                            cel = cel->suivant;

                            strcpy(record.id, id);
                            free(id);
                            fich_parc.removeRecord(record);
                        }while(cel != NULL);

                        pthread_mutex_unlock(&mutexFichParc);

                        PT->idCont.removeAll();

                        freePTMess();
                        PT->message = new char[strlen("6#true#Chargement termine correctement#%")+1];
                        strcpy(PT->message, "6#true#Chargement termine correctement#%");
                    }
                    else
                    {
                        cout << "getNombrElem = " << PT->idCont.getNombreElements() << " capacite = " << PT->capacite << " nbrEnv = " << PT->nbrEnv << endl;
                        if(PT->idCont.getNombreElements() < PT->capacite && PT->idCont.getNombreElements() < PT->nbrEnv)
                        {
                            freePTMess();
                            PT->message = new char[strlen("6#false#Incoherence detectee : place encore disponible#%")+1];
                            strcpy(PT->message, "6#false#Incoherence detectee : place encore disponible#%");
                            PT->dernOpp = OutputOne;
                        }
                        else
                        {
                            freePTMess();
                            PT->message = new char[strlen("6#false#Incoherence detectee#%")+1];
                            strcpy(PT->message, "6#false#Incoherence detectee#%");
                            PT->dernOpp = OutputOne;
                        }
                    }
                }
                break;

            case Logout:
                {
                    if(PT->connect == true)
                    {
                        if(strcmp(proto.donnees.login.nom, PT->nom) == 0 && Configurator::getLog("login.csv", proto.donnees.login.nom, proto.donnees.login.pwd))
                        {
                            PT->connect = false;
                            PT->finDialog = true;

                            freePTMess();
                            PT->message = new char[strlen("7#true##%")+strlen(PT->nom)+1];
                            strcpy(PT->message, "7#true#");
                            strcat(PT->message, PT->nom);
                            strcat(PT->message, "#%");
                        }
                        else
                        {
                            freePTMess();
                            PT->message = new char[strlen("7#false#Nom d'utilisateur ou mot de passe incorrect#%")+1];
                            strcpy(PT->message, "7#false#Nom d'utilisateur ou mot de passe incorrect#%");  
                        }  
                    }
                }
                break;
        }
    }
    else
    {  
        freePTMess();
        PT->message = new char[strlen("#false#Type de requete non attendue#%")+2];
        char type[10];
        sprintf(type, "%d", proto.type);
        strcpy(PT->message, type);        
        strcat(PT->message, "#false#Type de requete non attendue#%");  
    }
}

/********************************/
/*           Thread             */
/********************************/

void * fctThread(void * param)
{
    struct protocole proto;
    int* identite = (int*)malloc(sizeof(int));
    *identite = *((int *)param);
    //int vr = (int)(param);

    Affiche("THREAD","DEMARRAGE DU THREAD \n");
    
    //Initialisation de la structure Thread
    S_THREAD *PT = new S_THREAD;
    PT->connect = false;
    PT->finDialog = false;
    PT->dernOpp = Init;
    PT->message = NULL;

	//Initialisation de la cle avec la fonction
	pthread_once(&controleur , InitCle);

	//On met la structure fantome dans la zone spécifique
	pthread_setspecific(cle, PT);

    SocketsServeur hSocketService;
    int indiceClientTraite;

    char* msgConfirmation = NULL;
    msgConfirmation = (char*)malloc(255);
    strcpy(msgConfirmation, "0#true#Mise en place de la connection#%");

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

        
        hSocketService.sendString(msgConfirmation, strlen(msgConfirmation));
        Affiche("test","\033[1;36m<TASK>\033[0m Thread n° %d s'occupe du socket %d \n", pthread_self(), indiceClientTraite);

        do
        {
            try
            {
                hSocketService.receiveStruct(&proto, sizeof(struct protocole));

                /*condition pour voir si le login à deja été effectué*/
                if(PT->connect == true || proto.type == 1)
                {
                    switchThread(proto);
                }
                else
                {
                    freePTMess();
                    PT->message = new char[strlen("#false#Vous devez etre connecte pour cette action#%")+2];
                    char type[10];
                    sprintf(type, "%d", proto.type);
                    strcpy(PT->message, type);
                    strcat(PT->message, "#false#Vous devez etre connecte pour cette action#%");
                }
            
                hSocketService.sendString(PT->message, strlen(PT->message));
            }
            catch(BaseException e)
            {
                PT->finDialog = true;
                PT->connect = false;
                std::cerr << e.getMessage() << endl;
            }

        } while (!PT->finDialog);

        hSocketService.closeSocket();

        pthread_mutex_lock(&mutexIndiceCourant);
        sockets[indiceClientTraite].setLibre(true);
        pthread_mutex_unlock(&mutexIndiceCourant);
    }
    hSocketService.closeSocket();
    return identite;
}
