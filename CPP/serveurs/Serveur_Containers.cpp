/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include "CMMP.h"
#include "Trace.h"
#include "liste.h"
#include "Output.h"
#include <stdio.h>     
#include <sstream>
#include <iostream>
#include <unistd.h> 
#include <stdlib.h>
#include <pthread.h>
#include "ParcAcces.h"
#include "StructParc.h"
#include "Configurator.h"
#include "ParcourChaine.h"
#include "BaseException.h"
#include "SocketsClient.h"
#include "SocketsServeur.h"

#include <netinet/in.h>
#include <netinet/tcp.h>

#define MTU 1000

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
pthread_t threadAdm;
SocketsServeur* sockets;

// Déclaration de la clé et du controleur
pthread_key_t cle;
pthread_once_t controleur = PTHREAD_ONCE_INIT;

char* clients[20] = {NULL};

/********************************/
/*          Prototypes          */
/********************************/

//Threads
void* fctThread(void *param);
void* threadAdmin(void *param);

//Fonctions
void InitCle();
void freePTMess();
void destructeur(void *p);
void switchThread(protocole &proto, SocketsClient &socketMouv);
char* decodeEtIsereMessage(char* message, bool* finDial, int* time);


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

    //creation du threadAdmin
    pthread_create(&threadAdm, NULL, threadAdmin, NULL);

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
            clients[j] = (char*)malloc(strlen(inet_ntoa(socketEcoute.getAdresse().sin_addr))+1);
            strcpy(clients[j], inet_ntoa(socketEcoute.getAdresse().sin_addr));
            clients[j][strlen(inet_ntoa(socketEcoute.getAdresse().sin_addr))] = '\0';
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
void switchThread(protocole &proto, SocketsClient &socketMouv)
{
    char* retour = NULL;

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
                    //Construction du message
                    int tail = strlen("protocol.PLAMAP.DonneeGetXY#societe=#immatriculationCamion=#idContainer=#destination=") + 1 + strlen(proto.donnees.inputTruck.idContainer) + strlen(proto.donnees.inputTruck.imCamion) + strlen(proto.donnees.inputTruck.societe) + strlen(proto.donnees.inputTruck.destination);
                    char * mes = (char*)malloc(tail);
                    strcpy(mes, "protocol.PLAMAP.DonneeGetXY#societe=");
                    strcat(mes, proto.donnees.inputTruck.societe);
                    strcat(mes, "#immatriculationCamion=");
                    strcat(mes, proto.donnees.inputTruck.imCamion);
                    strcat(mes, "#idContainer=");
                    strcat(mes, proto.donnees.inputTruck.idContainer);      
                    strcat(mes, "#destination=");
                    strcat(mes, proto.donnees.inputTruck.destination);
                    mes[tail-1] = '\n';


                    socketMouv.sendString(mes, tail);

                    retour = socketMouv.receiveString(MTU, '#', '%');

                    cout << "message recu de serveur mouvement [" << retour << "]" << endl;

                    //verification si la requete est OK
                    if(strcmp(ParcourChaine::getSuccesServeur(retour), "true") == 0)
                    {               

                        strcpy(PT->tmpContaineur.id, proto.donnees.inputTruck.idContainer);
                        PT->tmpContaineur.flagemplacement = 1;                 

                        int coo[2];

                        ParcourChaine::getCoordoneesServeur(retour, coo);

                        PT->tmpContaineur.x = coo[0];
                        PT->tmpContaineur.y = coo[1];

                        proto.donnees.reponse.x = coo[0];
                        proto.donnees.reponse.y = coo[1];
                        strcpy(proto.donnees.reponse.message, "Voice la place reservee en ");

                        freePTMess();
                        PT->message = new char[strlen("2#true#XXX/YYY#%")+1];
                        strcpy(PT->message, "2#true#");
                        char xy [10];
                        sprintf(xy, "%d", proto.donnees.reponse.x);
                        strcat(PT->message, xy);
                        strcat(PT->message, "/");
                        sprintf(xy, "%d", proto.donnees.reponse.y);
                        strcat(PT->message, xy);
                        strcat(PT->message, "#%");
                    }
                    else
                    {
                        pthread_mutex_unlock(&mutexFichParc);
                        PT->dernOpp = Init;
                        freePTMess();
                        PT->message = new char[strlen("2#false#Un des renseignements envoyés ne correspond pas#%")+1];
                        strcpy(PT->message, "2#false#Un des renseignements envoyés ne correspond pas#%");        
                    }
                }
                break;

            case InputDone:
                {                     
                    if(proto.donnees.inputDone.etat == true)
                    {
                        //verif si le poids du container est OK
                        if(proto.donnees.inputDone.poids <= atof(Configurator::getProperty("test.conf","POIDS"))) 
                        {
                            
                            PT->tmpContaineur.poids = proto.donnees.inputDone.poids;

                            std::ostringstream mess;

                            mess << "protocol.PLAMAP.DonneeSendWeight#idContainer=" << PT->tmpContaineur.id << "#x=" << PT->tmpContaineur.x << "#y=" << PT->tmpContaineur.y << "#poids=" << proto.donnees.inputDone.poids << '\n';

                            socketMouv.sendString((char*)mess.str().c_str(), strlen(mess.str().c_str())+1);

                            retour = socketMouv.receiveString(MTU, '#', '%');

                            cout << "message recu de serveur mouvement [" << retour << "]" << endl;

                            if(strcmp(ParcourChaine::getSuccesServeur(retour), "true") == 0)
                            {              
                                freePTMess();
                                PT->message = new char[strlen("3#true#Container enregistre#%")+1];
                                strcpy(PT->message, "3#true#Container enregistre#%");
                            }
                            else
                            {
                                freePTMess();
                                PT->message = new char[strlen("3#false#Container non conforme#%")+1];
                                strcpy(PT->message, "3#false#Container non conforme#%");
                                PT->dernOpp = Init; 
                            }
                        }
                        else
                        {
                            freePTMess();
                            PT->message = new char[strlen("3#false#Container non conforme#%")+1];
                            strcpy(PT->message, "3#false#Container non conforme#%");
                            PT->dernOpp = Init;
                        }
                    }
                    else
                    {   
                        freePTMess();
                        PT->message = new char[strlen("3#false#Container non conforme#%")+1];
                        strcpy(PT->message, "3#false#Container non conforme#%");
                        PT->dernOpp = Init;
                    }
                }
                break;

            case OutputReady:
                {
                    char cap [10];
                    sprintf(cap, "%d", proto.donnees.outputReady.capacite);

                    //Construction du message
                    int tail = strlen("protocol.PLAMAP.DonneeGetList#idTransporteur=#destination=#nombreMax=") + 1 + strlen(proto.donnees.outputReady.id) + strlen(proto.donnees.outputReady.dest) + strlen(cap);
                    char * mes = (char*)malloc(tail);
                    strcpy(mes, "protocol.PLAMAP.DonneeGetList#idTransporteur=");
                    strcat(mes, proto.donnees.outputReady.id);
                    strcat(mes, "#destination=");
                    strcat(mes, proto.donnees.outputReady.dest);
                    strcat(mes, "#nombreMax=");
                    strcat(mes, cap);
                    mes[tail-1] = '\n';

                    socketMouv.sendString(mes, tail);

                    retour = socketMouv.receiveString(MTU, '#', '%');

                    cout << "message recu de serveur mouvement [" << retour << "]" << endl;

                    if(strcmp(ParcourChaine::getSuccesServeur(retour), "true") == 0) 
                    {
                        char* liste = ParcourChaine::getMessage(retour);

                        PT->nbrEnv = ParcourChaine::getTailleList(liste);

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
                    cout << "getNbrElem : " << PT->idCont.getNombreElements() << endl;
                    cout << "capacite : " << PT->capacite << endl;
                    cout << "nbrEnv : " << PT->nbrEnv << endl;
                    if(PT->idCont.getNombreElements() == PT->capacite || PT->idCont.getNombreElements() == PT->nbrEnv) 
                    {
                        char* id = NULL;
                        char* liste = NULL;

                        struct Cellule<char*> *cel = PT->idCont.getTete();
                        
                        do
                        {
                            char* listeTmp;
                            id = cel->valeur;
                            cel = cel->suivant;

                            if(liste == NULL)
                            {
                                listeTmp = (char*) malloc(strlen(id) + 1);
                                strcpy(listeTmp, id);
                                strcat(listeTmp, "/");
                            }
                            else
                            {
                                listeTmp = (char*) malloc(strlen(id) + strlen(liste) + 1);
                                strcpy(liste, id);
                                strcpy(listeTmp, id);
                                strcat(listeTmp, "/");
                            }

                            free(id);
                            free(liste);
                            liste = listeTmp;

                        }while(cel != NULL);
               
                        //Construction du message
                        int tail = strlen("protocol.PLAMAP.DonneeSignalDep#idTransporteur=#ListIdCont=") + 1 + strlen(proto.donnees.outputDone.id) + strlen(liste);
                        char * mes = (char*)malloc(tail);
                        strcpy(mes, "protocol.PLAMAP.DonneeSignalDep#idTransporteur=");
                        strcat(mes, proto.donnees.outputDone.id);
                        strcat(mes, "#ListIdCont=");
                        strcat(mes, liste);
                        mes[tail-1] = '\n';

                        socketMouv.sendString(mes, tail);

                        retour = socketMouv.receiveString(MTU, '#', '%');

                        if(strcmp(ParcourChaine::getSuccesServeur(retour), "true") == 0) 
                        {
                            freePTMess();
                            PT->message = new char[strlen("6#true#Chargement termine correctement#%")+1];
                            strcpy(PT->message, "6#true#Chargement termine correctement#%");
                        }
                        else
                        {
                            freePTMess();
                            PT->message = new char[strlen("6#false#Un probleme est survenu, veuillez recommencer#%")+1];
                            strcpy(PT->message, "6#false#Un probleme est survenu, veuillez recommencer#%");
                        }

                        PT->idCont.removeAll();

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

    if(retour != NULL)
    {
        free(retour);
    }
}

/********************************/
/*           Thread             */
/********************************/

void * fctThread(void * param)
{
    int port;
    char *portMouv = NULL;
    char *adresseMouv = NULL;
    char *pwdMouv = NULL;
    char *loginMouv = NULL;
    char* retour = NULL;
    SocketsClient  socketMouv;

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
    strcpy(msgConfirmation, "0#true#Mise en place de la connexion#%");

    portMouv = Configurator::getProperty("test.conf","MOUVEMENT-PORT");
    adresseMouv = Configurator::getProperty("test.conf","MOUVEMENT-ADRESS");
    if(portMouv == NULL || adresseMouv == NULL)
    {
        exit(0);
    }
    
    //Connexion a la socket du serveur MouvementPLAMAP
    port = atoi(portMouv);
    try
    {
       socketMouv.initSocket(adresseMouv, port);
    }
    catch(BaseException ex)
    {
        cout << ex.getMessage() << endl;
        exit(0);
    }
    
    //Récupération du login et du mdp pour le login au serveur mouvement 
    pwdMouv = Configurator::getProperty("test.conf","SERVEUR-PASSWORD");
    loginMouv = Configurator::getProperty("test.conf","SERVEUR-NAME");
    if(pwdMouv == NULL || loginMouv == NULL)
    {
        exit(0);
    }

    //Construction du message
    int tail = strlen("protocol.PLAMAP.DonneeLoginCont#username=#password=") + 1 + strlen(pwdMouv) + strlen(loginMouv);
    char * mes = (char*)malloc(tail);
    strcpy(mes, "protocol.PLAMAP.DonneeLoginCont#username=");
    strcat(mes, loginMouv);
    strcat(mes, "#password=");
    strcat(mes, pwdMouv);
    mes[tail-1] = '\n';


    socketMouv.sendString(mes, tail);

    cout << "Attente de connexion avec le serveur mouvement" << endl;
    retour = socketMouv.receiveString(MTU, '#', '%');

    //verification si la connexion est OK
    if(strcmp(ParcourChaine::getSuccesServeur(retour), "true") == 0)
    {
        cout << "Connexion avec le serveur mouvement" << endl;
    }
    else
    {
        cout << "Erreur de connexion avec le serveur mouvement" << endl;
        exit(-1);
    }
    
    

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
                    switchThread(proto, socketMouv);
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
        free(clients[indiceClientTraite]);
        clients[indiceClientTraite] = NULL;
        pthread_mutex_unlock(&mutexIndiceCourant);
    }
    hSocketService.closeSocket();
    return identite;
}


void* threadAdmin(void *param)
{
    SocketsServeur sockEcoute;
    SocketsServeur sockService;

    char *portTmp;
    int port;
    char *adresse;

    try
    {
        portTmp = Configurator::getProperty("test.conf","PORT-ADMIN");
        adresse = Configurator::getProperty("test.conf","HOSTNAME");
        if(portTmp == NULL || adresse == NULL)
        {
            exit(0);
        }
        
        port = atoi(portTmp);

        sockEcoute.initSocket(adresse, port);
    }
    catch(BaseException& e)
    {
        Error("Error","%s\n",e.getMessage());
        exit(0);
    }

    do
    {
        bool finDial = false;
        int time;
        sockEcoute.listenSocket(SOMAXCONN);

        sockService = sockEcoute.acceptSocket();

        do
        {
            try
            {
                char* message = sockService.receiveString(MTU, '#', '%');

                char* retour = decodeEtIsereMessage(message, &finDial, &time);

                free(message);
                message = NULL;

                cout << retour << endl;

                sockService.sendString(retour, strlen(retour));
                if(finDial)
                {
                    char *portTmp = Configurator::getProperty("test.conf","PORT-URGENCE");
                    int port = atoi(portTmp);

                    for(int i = 0 ; i < 20 ; i++)
                    {
                        if(clients[i] != NULL)
                        {
                            SocketsClient socket;
                            socket.initSocket(clients[i], port);
                            char dure[10];
                            sprintf(dure, "%d", time);

                            int tail = strlen("Le serveur va se couper dans  seconde(s)") + 3 + strlen(dure);
                            char * message = (char*)malloc(tail);
                            strcpy(message, "Le serveur va se couper dans ");
                            strcat(message, dure);
                            strcat(message, " seconde(s)#%");

                            socket.sendString(message, strlen(message));
                            free(message);
                        }
                    }


                    


                    cout << "extinction dans : " << time << " seconde(s)" << endl;
                    sleep(time);
                    exit(1);
                }
            }
            catch(BaseException e)
            {
                finDial = true;
                std::cerr << e.getMessage() << endl;
            }
            
        } while (!finDial);
        
    }while(true);

    sockEcoute.closeSocket();

    cout << "Arret du thread admin" << endl;
   
}


char* decodeEtIsereMessage(char* message, bool* finDial, int* time)
{
    int place = 0;
    char * comp = NULL;

    comp = ParcourChaine::myTokenizer(message, '#', &place);
    if(strcmp(comp, "protocol.CSA.DonneeLoginA") == 0)
    {
        place = 0;
        int tail;

        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '}', &place);
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '#', &place);
        tail = strlen(comp) + 1;
        char * username = (char*)malloc(tail);
        strcpy(username, comp);
        username[tail] = '\0';
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '}', &place);
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '#', &place);
        tail = strlen(comp) + 1;
        char * password = (char*)malloc(tail);
        strcpy(password, comp);
        password[tail] = '\0';
        free(comp);
        comp = NULL;

        char* retour = NULL;

        if(Configurator::getLog("login.csv", username, password))
        {
            tail = strlen("protocol.CSA.ReponseCSA##codeRetour==200") + 1;
            retour = (char*)malloc(tail);
            strcpy(retour, "protocol.CSA.ReponseCSA##codeRetour==200");
            retour[tail-1] = '\n';
        }
        else
        {
            tail = strlen("protocol.CSA.ReponseCSA##codeRetour==400") + 1;
            retour = (char*)malloc(tail);
            strcpy(retour, "protocol.CSA.ReponseCSA##codeRetour==400");
            retour[tail-1] = '\n';
        }

        free(username);
        username = NULL;
        free(password);
        password = NULL;

        return retour;
    }
    else if(strcmp(comp, "protocol.CSA.DonneeLCients") == 0)
    {
        place = 0;
        int tail;
        char* retour = NULL;

        free(comp);
        comp = NULL;

        tail = strlen("protocol.CSA.ReponseCSA##codeRetour==200##chargeUtile==protocol.CSA.DonneeLCients#listClient{=}") + 1;
        retour = (char*)malloc(tail);
        strcpy(retour, "protocol.CSA.ReponseCSA##codeRetour==200##chargeUtile==protocol.CSA.DonneeLCients#listClient{=}");

        for(int i = 0 ; i < 20 ; i++)
        {
            if(clients[i] != NULL)
            {
                tail = strlen(retour);
                char* tmp = retour;
                retour = (char*)malloc(tail + strlen(clients[i]) + 1);
                strcpy(retour, tmp);
                strcat(retour, clients[i]);
                strcat(retour, "/");
                free(tmp);
            }
        }
        tail = strlen(retour);
        retour[tail-1] = '\n';

        return retour;
    }
    else if(strcmp(comp, "protocol.CSA.DonneeStop") == 0)
    {
        place = 0;
        int tail;

        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '}', &place);
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '#', &place);
        *time = atoi(comp);
        free(comp);
        comp = NULL;

        *finDial = true;

        tail = strlen("protocol.CSA.ReponseCSA##codeRetour==200") + 1;
        char * retour = (char*)malloc(tail);
        strcpy(retour, "protocol.CSA.ReponseCSA##codeRetour==200");
        retour[tail-1] = '\n';

        return retour;
    }
}