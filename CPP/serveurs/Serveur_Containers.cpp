/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include "CMMP.h"
#include "Trace.h"
#include "ParcAcces.h"
#include "StructParc.h"
#include "Configurator.h"
#include <stdio.h>     
#include <iostream>
#include <unistd.h> 
#include <stdlib.h>
#include <pthread.h>
#include "BaseException.h"
#include "SocketsServeur.h"

#include <netinet/in.h>
#include <netinet/tcp.h>

#define NB_MAX_CONNECTIONS 3

using namespace std;

/********************************/
/*          Structure           */
/********************************/

typedef struct
{
	bool connect;
    char* message;
	bool finDialog;
    char nom[MAXSTRING];
    typeRequete dernOpp;
    struct fich_parc tmpContaineur;
}S_THREAD;


/********************************/
/*      Variables globales      */
/********************************/

int indiceCourant = -1;
pthread_cond_t condIndiceCourant;
pthread_mutex_t mutexIndiceCourant;
pthread_t threads[NB_MAX_CONNECTIONS];
SocketsServeur sockets[NB_MAX_CONNECTIONS];

// Déclaration de la clé et du controleur
pthread_key_t cle;
pthread_once_t controleur = PTHREAD_ONCE_INIT;

/********************************/
/*          Prototypes          */
/********************************/

void* fctThread(void *param);

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
    char *portTmp;
    int port;
    char *adresse;
    parcAcces pa = parcAcces("FICH_PARC.dat");

    //debut bidon

    struct fich_parc fp1;
    //pa.debugFichPark(); 
    
    strcpy(fp1.id, "YABB-CHARL-A1B2C3");
    fp1.flagemplacement = 2;
    strcpy(fp1.datereservation, "00/00/0000");
    strcpy(fp1.datearrivee, "08/10/2020");
    fp1.poids = 250;
    strcpy(fp1.destination, "Madrid");
    strcpy(fp1.moyenTransport, "Bateau");
    bool ret = pa.addRecord(fp1);

    cout << "bool " << ret << endl;

    strcpy(fp1.id, "YABB-KEVIN-A1B2C3");
    fp1.flagemplacement = 1;
    strcpy(fp1.datereservation, "09/10/2020");
    strcpy(fp1.datearrivee, "00/00/0000");
    fp1.poids = 112;
    strcpy(fp1.destination, "Madrid");
    strcpy(fp1.moyenTransport, "Train");
    pa.addRecord(fp1);

    strcpy(fp1.id, "YABB-SAMUE-A1B2C3");
    fp1.flagemplacement = 2;
    strcpy(fp1.datereservation, "06/10/2020");
    strcpy(fp1.datearrivee, "06/10/2020");
    fp1.poids = 12;
    strcpy(fp1.destination, "Madrid");
    strcpy(fp1.moyenTransport, "Bateau");
    pa.addRecord(fp1);

    pa.debugFichPark(); 
    getchar();

    char* res = pa.searchDestination("Madrid");

    cout << "RES : " << res << endl;

    getchar();


    //fin bidon

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


/********************************/
/*          Fonctions           */
/********************************/

void InitCle()
{
	//Pour la zone spécifique
	printf("Initialisation d'une cle\n");
	pthread_key_create(&cle, destructeur);
}

void destructeur(void *p)
{
	//On libère la mémoire alouée sur laquelle pointe la zone spécifique
	printf("Liberation d'une zone specifique\n");
	free(p);
}

void freePTMess()
{
    S_THREAD *PT = NULL;
	
	PT = (S_THREAD*)pthread_getspecific(cle);

    if(PT->message != NULL)
    {
        free(PT->message);
    }
}

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
                    }
                }
                else
                {
                    freePTMess();
                    PT->message = new char[strlen("1#false#Vous etes deja connecte#%")+1];
                    strcpy(PT->message, "1#false#Vous etes deja connecte#%");           
                }    
                break;

            case InputTruck:
                {
                    //TODO:a proteger par un mutex
                    parcAcces fich_parc("FICH_PARC.dat");
                    
                    //recherche si emplacement libre
                    if(fich_parc.searchPlace(&(PT->tmpContaineur)))
                    {
                        strcpy(PT->tmpContaineur.id, proto.donnees.inputTruck.idContainer);
                        PT->tmpContaineur.flagemplacement = 1;
                        
                        //enregistrement dans FICH_PARC
                        fich_parc.updateRecord(PT->tmpContaineur);

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
                        PT->dernOpp = Init;
                        freePTMess();
                        PT->message = new char[strlen("2#false#Pas d'emplacements libres#%")+1];
                        strcpy(PT->message, "2#false#Pas d'emplacements libres#%");           
                    }
                }
                break;

            case InputDone:
                {  
                    //TODO:a proteger par un mutex
                    parcAcces fich_parc("FICH_PARC.dat");
                    
                    if(proto.donnees.inputDone.etat == true)
                    {

                        //verif si le poids du container est OK
                        if(proto.donnees.inputDone.poids <= atof(Configurator::getProperty("test.conf","POIDS"))) 
                        {
                            
                            PT->tmpContaineur.poids = proto.donnees.inputDone.poids;
                            //enregistrement dans FICH_PARC
                            fich_parc.updateRecord(PT->tmpContaineur);
                            

                            freePTMess();
                            PT->message = new char[strlen("3#true#Container enregistre#%")+1];
                            strcpy(PT->message, "3#true#Container enregistre#%");
                        }
                        else
                        {
                            PT->tmpContaineur.flagemplacement = 0;
                            
                            //libere la place dans FICH_PARC
                            fich_parc.updateRecord(PT->tmpContaineur);                       
                            
                            freePTMess();
                            PT->message = new char[strlen("3#false#Container non conforme#%")+1];
                            strcpy(PT->message, "3#false#Container non conforme#%");
                        }
                    }
                    else
                    {
                        PT->tmpContaineur.flagemplacement = 0;
                        
                        //libere la place dans FICH_PARC
                        fich_parc.updateRecord(PT->tmpContaineur);
                        
                        freePTMess();
                        PT->message = new char[strlen("3#false##%")+strlen(PT->nom)+1];
                        strcpy(PT->message, "3#false#");
                        strcat(PT->message, PT->nom);
                        strcat(PT->message, "#%");
                    }
                }
                break;

            case OutputReady:

                //TODO:a proteger par un mutex
                //TODO:si il y a des container pour cette destination
                // recherche dans FICH_PARK
                if(true) 
                {
                    //TODO:renvoyer la liste des containers d'apres FICH_PARK
                    //dans le texte

                    freePTMess();
                    PT->message = new char[strlen("4#true#%")+1];
                    strcpy(PT->message, "4#true#%");
                    //strcat(PT->message, PT->nom);
                    //strcat(PT->message, "#%");

                    //strcpy(proto.donnees.reponse.message, "Voici la liste des containers");
                }
                else
                {
                    freePTMess();
                    PT->message = new char[strlen("4#false#Pas de container pour cette destination#%")+1];
                    strcpy(PT->message, "4#false#Pas de container pour cette destination#%");
                } 

                break;

            case OutputOne:
                //TODO:a proteger par un mutex
                //TODO:recherche du container s'il existe
                // recherche dans FICH_PARK
                if(true) 
                {
                    //TODO:mise a jour de FICH_PARK
                    freePTMess();
                    PT->message = new char[strlen("5#true#Deplacement de container enregistre#%")+1];
                    strcpy(PT->message, "5#true#Deplacement de container enregistre#%");
                }
                else
                {
                    freePTMess();
                    PT->message = new char[strlen("5#false#Container inconnu#%")+1];
                    strcpy(PT->message, "5#false#Container inconnu#%");
                }

                break;

            case OutputDone:

                //TODO:verifier que le transporteur est bien plein
                if(true) 
                {
                    freePTMess();
                    PT->message = new char[strlen("6#true#Chargement termine correctement#%")+1];
                    strcpy(PT->message, "6#true#Chargement termine correctement#%");
                }
                else
                {
                    freePTMess();
                    PT->message = new char[strlen("6#false#Incoherence detectee : place encore disponible#%")+1];
                    strcpy(PT->message, "6#false#Incoherence detectee : place encore disponible#%");
                }

                break;

            case Logout:

                if(PT->connect == true)
                {
                    if(strcmp(proto.donnees.login.nom, PT->nom) == 0 && Configurator::getLog("login.csv", proto.donnees.login.nom, proto.donnees.login.pwd))
                    {
                        PT->connect = false;
                        PT->finDialog = true;

                        freePTMess();
                        PT->message = new char[strlen("6#true##%")+strlen(PT->nom)+1];
                        strcpy(PT->message, "6#true#");
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

    Affiche("thread","Thread n° %d demarre a la position %d \n", pthread_self(), *identite);
    
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

        do
        {
            try
            {
                hSocketService.receiveStruct(&proto, sizeof(struct protocole));
                cout << "Apres receive" << endl;
            }
            catch(BaseException e)
            {
                PT->finDialog = true;
                PT->connect = false;
                std::cerr << e.getMessage() << '\n';
            }

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
            
            try
            {
                hSocketService.sendString(PT->message, strlen(PT->message));
            }
            catch(BaseException e)
            {
                std::cerr << e.getMessage() << '\n';
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
