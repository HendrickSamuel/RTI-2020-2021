/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 11/12/2020             */
/***********************************************************/

#include <ctime>

#include <sys/types.h> 
#include <sys/socket.h>      /* pour les types de socket */ 
#include <sys/time.h>        /* pour les types de socket */ 
#include <netdb.h>           /* pour la structure hostent */ 
#include <errno.h> 
#include <netinet/in.h>      /* pour la conversion adresse reseau->format dot ainsi que le conversion format local/format reseau */
 #include <netinet/tcp.h>    /* pour la conversion adresse reseau->format dot */ 
 #include <arpa/inet.h>      /* pour la conversion adresse reseau->format dot */ 

#include "CMMP.h"
#include <random>
#include "liste.h"
#include <sys/time.h>
#include <stdio.h>
#include <iostream>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <pthread.h>
//#include "cryptlib.h"
#include "iterateur.h"
#include "Configurator.h"
#include "ParcourChaine.h"
#include "BaseException.h"
#include "SocketsClient.h"



#define MTU 1000

using namespace std;

/********************************/
/*          Prototypes          */
/********************************/

//Threads
void* threadReception(void *param);


// Prototype SigHandler
void HandlerMsg(int);


//Fonctions
void    effEcran();
void    affChaine(const char *,int,int);
void    afficheEntete();
void    afficheEnteteChat();
void    afficheEnteteLogin();
void    afficheChat();
void    afficheTest();
bool    afficheMenu(int* choix);
char*   postQuestion(char*);
char*   answerQuestion(char*);
char*   postEvent(char*);


char*   createDigest(char* pdw, long time, double random);
bool    lireNombre(int *pN, int min, int max);
short   LireChaine(char *, int);
char*   genAleat(int len);
void    decodeEtIsereMessage(char* message);


/********************************/
/*      Variables globales      */
/********************************/

pthread_mutex_t mutexListes;
pthread_mutex_t mutexCOut;

pthread_t thread;

Liste<char*> listQuestions;
Liste<char*> listChat;




/********************************/
/*             Main             */
/********************************/


int main(int argc, char *argv[])
{
    int port;
    int tail;
     
    char nom[MAXSTRING];
    char pwd[MAXSTRING];
    char* digestBase64 = NULL;

    long time;
    double random;

    char *portTmp = NULL;
    char *adresse = NULL;
    char *message = NULL;
    SocketsClient  socketCli;

    //Armement du signal
    struct sigaction Msg;
	Msg.sa_handler = HandlerMsg;
	sigemptyset(&Msg.sa_mask);
	Msg.sa_flags = 0;
	if(sigaction(SIGUSR2,&Msg,NULL))
	{
		perror("Erreur d'armement du signal SIGUSR2");
		exit(1);
	}

	//Masquage des signaux
	sigset_t mask;
	sigfillset(&mask);
    sigdelset(&mask, SIGINT);
	sigprocmask(SIG_SETMASK, &mask,NULL);	

    /* lecture des parametres sur fichier */
    portTmp = Configurator::getProperty("test.conf","PORT-FOR-US-ONLY");
    adresse = Configurator::getProperty("test.conf","IP-CHAT-LOGIN");
    if(portTmp == NULL || adresse == NULL)
    {
        exit(0);
    }


    //Connexion a la socket du serveur Chat
    port = atoi(portTmp);
    try
    {
       socketCli.initSocket(adresse, port);
    }
    catch(...)
    {
        cout << "Erreur création socket" << endl;
        exit(0);
    }
    printf("Creation de la socket TCP OK\n");

    //do
    //{
        effEcran();
        afficheEntete();
        afficheEnteteLogin();

        cout << endl << endl;
        cout << "\t" << "Entrez votre username : ";
        cin >> nom;
        cout << "\t" << "Entrez votre mot de passe : ";
        cin >> pwd;


        //récupération de time
        struct timeval tp;
        gettimeofday(&tp, NULL);
        time = tp.tv_sec * 1000 + tp.tv_usec / 1000;

        //récupération de random
        random = (double)rand();

        //creation du digest
        digestBase64 = createDigest(&pwd[0], time, random);
        strcpy(digestBase64, "8R00/XUIJJVPi5LMCT9a3IJF2Hmpam9bW7yPBWIrNIw=");

        //creation du message
        char temps [20];
        sprintf(temps, "%ld", time);
        char aleat [20];
        sprintf(aleat, "%lf", random);
        
        tail = strlen("protocol.PFMCOP.DonneeLoginGroup##username{=}#pwdDigest{=}#temps{=}#aleatoire{=}#adresse{=}null#port{=}0") + 1 + strlen(nom) + strlen(digestBase64) + strlen(temps) + strlen(aleat);
        message = (char*)malloc(tail);
        strcpy(message, "protocol.PFMCOP.DonneeLoginGroup##username{=}");
        strcat(message, nom);
        strcat(message, "#pwdDigest{=}");
        strcat(message, digestBase64);
        strcat(message, "#temps{=}");
        strcat(message, temps);
        strcat(message, "#aleatoire{=}");
        strcat(message, aleat);
        strcat(message, "#adresse{=}null#port{=}0");
        message[tail-1] = '\n';

        //envoie et réception
        socketCli.sendString(message, tail);

        //retour = socket.receiveString(MTU, '#', '%');

        free(digestBase64);
        digestBase64 = NULL;
        free(message);
        message = NULL;

    //} while ();
 
    socketCli.closeSocket();

    //init mutex
    pthread_mutex_init(&mutexListes, NULL);
    pthread_mutex_init(&mutexCOut, NULL);

    pthread_mutex_lock(&mutexCOut);

    //creation du thread
    pthread_create(&thread, NULL, threadReception, NULL);

    //connexion au chat
    int hSocket;    /* Handle de la socket */ 
    hSocket = socket(AF_INET,  SOCK_DGRAM, IPPROTO_UDP);
    if (hSocket == -1) 
    { 
        printf("Erreur de creation de la socket %d\n", errno); 
        exit(1); 
    } 
    else 
        printf("Creation de la socket UDP OK\n");

    unsigned char multicastTTL = 10;
    setsockopt(hSocket, IPPROTO_IP, IP_MULTICAST_TTL, (void *) &multicastTTL, sizeof(multicastTTL));

    
    struct sockaddr_in groupSock;
    memset((char*)&groupSock, 0, sizeof(groupSock));
    groupSock.sin_family = AF_INET;
    groupSock.sin_addr.s_addr = inet_addr("234.5.5.9");
    groupSock.sin_port = htons(5011);

    struct ip_mreq mreq;
    mreq.imr_multiaddr.s_addr = inet_addr("234.5.5.9");
    mreq.imr_interface.s_addr = (inet_addr("192.168.1.47"));

    if (setsockopt(hSocket, IPPROTO_IP, IP_ADD_MEMBERSHIP, &mreq, sizeof(mreq)) < 0)
    {
        perror("setsockopt -_-");
        exit(1);
    }

    tail = strlen("protocol.PFMCOP.DonneeBaseUDP##username{=} rejoint le groupe") + strlen(nom) + 1;
    message = (char*)malloc(tail);
    strcpy(message, "protocol.PFMCOP.DonneeBaseUDP##username{=}");
    strcat(message, nom);
    strcat(message, " rejoint le groupe");
    message[tail-1] = '\0';

    int cnt = sendto(hSocket, message, strlen(message), 0, (struct sockaddr*) &groupSock, sizeof(groupSock));
  	if(cnt < 0) 
    {
 	    perror("sendto");
	    exit(1);
	}
    free(message);  
    message = NULL; 

    //on réarme le signal SIGUSR2 pour le main
    sigdelset(&mask, SIGUSR2);
    sigprocmask(SIG_SETMASK, &mask,NULL);

avantBoucle:
    int stop = 0;
    int choix;
    while (stop == 0)
    {
        effEcran();
        afficheEntete();
        afficheEnteteChat();
        afficheChat();

        if(!afficheMenu(&choix))
        {
            goto avantBoucle;
        }
        //on masque le signal SIGUSR2 pour le main
        sigaddset(&mask, SIGUSR2);
        sigprocmask(SIG_SETMASK, &mask,NULL);

        switch(choix)
        {
            case 0:
                stop = 1;
                break;

            case 1:
                message = postQuestion(nom);
                cnt = sendto(hSocket, message, strlen(message), 0, (struct sockaddr*) &groupSock, sizeof(groupSock));
                free(message);
                message = NULL;
                break;

            case 2:
                message = answerQuestion(nom);
                cnt = sendto(hSocket, message, strlen(message), 0, (struct sockaddr*) &groupSock, sizeof(groupSock));
                free(message);
                message = NULL;
                break;

            case 3:
                message = postEvent(nom);
                cnt = sendto(hSocket, message, strlen(message), 0, (struct sockaddr*) &groupSock, sizeof(groupSock));
                free(message);
                message = NULL;
                break;

            default:
                break;
        }     

        //on réarme le signal SIGUSR2 pour le main
        sigdelset(&mask, SIGUSR2);
        sigprocmask(SIG_SETMASK, &mask,NULL);
    }
    
    if(stop == 0)
    {
        goto avantBoucle;
    }

    tail = strlen("protocol.PFMCOP.DonneeBaseUDP##username{=} rejoint le groupe") + strlen(nom) + 1;
    message = (char*)malloc(tail);
    strcpy(message, "protocol.PFMCOP.DonneeBaseUDP##username{=}");
    strcat(message, nom);
    strcat(message, " quitte le groupe");
    message[tail-1] = '\n';
    
    cnt = sendto(hSocket, message, strlen(message), 0, (struct sockaddr*) &groupSock, sizeof(groupSock));
  	if(cnt < 0) 
    {
 	    perror("sendto");
	    exit(1);
	}
    free(message);
    message = NULL;

    close(hSocket);

    return 0;
}



/********************************/
/*          Fonctions           */
/********************************/

void effEcran()
{
    cout << "\033[2J";
    affChaine("", 1, 1);
}

void affChaine(const char *pChaine,int Lig,int Col)
{
    cout << "\033[" <<  Lig <<  ";" <<  Col <<  "H\033[" <<  pChaine << "\033[0m";
}

void afficheEntete()
{
	cout << "***************************************************************************************************************************************************" << endl;
	cout << "*                              Application Chat PFM                                                                                               *" << endl;
	cout << "***************************************************************************************************************************************************" << endl;
}

void afficheEnteteChat()
{
	cout << "*            Questions                                      *                                  Chat                                               *" << endl;
    cout << "***************************************************************************************************************************************************" << endl;
}

void afficheEnteteLogin()
{
	cout << "*                                                                     Login                                                                       *" << endl;
    cout << "***************************************************************************************************************************************************" << endl;
}

void afficheChat()
{
    pthread_mutex_lock(&mutexListes);

    Iterateur<char*> itQ(listQuestions);
    Iterateur<char*> itC(listChat);
    itQ.reset();
    itC.reset();

    for(int i = 0 ; i < listQuestions.getNombreElements() || i < listChat.getNombreElements() ; i++)
    {
        if(!itQ.end())
        {
            affChaine("**", i+6, 1);
            cout << (itQ);

            itQ++;
        }
        else
        {
            affChaine("**", i+6, 1);
        }

        if(!itC.end())
        {
            affChaine("**", i+6, 61);
            cout << (itC);

            itC++;
        }
        else
        {
            affChaine("**", i+6, 61);
        }    
        affChaine("**", i+6, 147);
    }
    cout << endl << "*                                                           *                                                                                     *" << endl;
    cout << "***************************************************************************************************************************************************" << endl;

    pthread_mutex_unlock(&mutexListes);
}

void afficheTest()
{
    pthread_mutex_lock(&mutexListes);

    Iterateur<char*> itQ(listQuestions);
    Iterateur<char*> itC(listChat);
    itQ.reset();
    itC.reset();

    cout << endl << "Questions : " << endl;
    while(!itQ.end())
    {
        cout << (itQ) << endl;

        itQ++;
    }

    cout << endl << "Chat : " << endl;
    while(!itC.end())
    {
        cout << (itC) << endl;

        itC++;
    }
    pthread_mutex_unlock(&mutexListes);
}

bool afficheMenu(int* choix)
{
    cout << endl << endl;
    cout << "\t" << "0 : Quitter" << endl;
    cout << "\t" << "1 : Post question" << endl;
    cout << "\t" << "2 : Answer question" << endl;
    cout << "\t" << "3 : Post event" << endl;
    cout << "\t\t" << "Faites votre choix : ";

    pthread_mutex_unlock(&mutexCOut);

    if(!lireNombre(choix, 0, 4))
    {
        pthread_mutex_lock(&mutexCOut);
        return false; 
    }

    pthread_mutex_lock(&mutexCOut);
    return true;
}

char* postQuestion(char* nom)
{
    int tail;
    char *message = NULL;
    char question[50];

    cout << endl << endl;
    cout << "\t" << "Quel est votre question : ";
    LireChaine(question, 50);

    tail = strlen("protocol.PFMCOP.DonneePostQuestion##tag{=}FRDMS#msgDigest{=}H1CH25Gc7VwSPH9QfT/M6BjLDPbnfC+VqKNelR4D/bk=#user{=}#message{=}") + strlen(nom) + strlen(question) + 2;
    message = (char*)malloc(tail);
    strcpy(message, "protocol.PFMCOP.DonneePostQuestion##tag{=}");
    char* alea = genAleat(5) ;
    strcat(message, alea);
    strcat(message, "#msgDigest{=}H1CH25Gc7VwSPH9QfT/M6BjLDPbnfC+VqKNelR4D/bk=#user{=}");
    strcat(message, nom);
    strcat(message, "#message{=}");
    strcat(message, question);
    message[tail] = '\0';

    free(alea);
    alea = NULL;

    return message;
}

char* answerQuestion(char* nom)
{
    int tail;
    char *message = NULL;
    char idQuest[10];
    char reponse[50];

    cout << endl << endl;
    cout << "\t" << "Quel est l'id de la question : ";
    LireChaine(idQuest, 10);
    cout << "\t" << "Quel est la reponse : ";
    LireChaine(reponse, 50);


    tail = strlen("protocol.PFMCOP.DonneeAnswerQuestion##tag{=}GGSNT#user{=}#message{=}") + strlen(nom) + strlen(reponse) + 2;
    message = (char*)malloc(tail);
    strcpy(message, "protocol.PFMCOP.DonneeAnswerQuestion##tag{=}");
    strcat(message, idQuest);
    strcat(message, "#user{=}");
    strcat(message, nom);
    strcat(message, "#message{=}");
    strcat(message, reponse);
    message[tail] = '\0';

    return message;
}

char* postEvent(char* nom)
{
    int tail;
    char *message = NULL;
    char event[50];

    cout << endl << endl;
    cout << "\t" << "Quel est l'evenement : ";
    LireChaine(event, 50);

    tail = strlen("protocol.PFMCOP.DonneePostEvent##tag{=}IVKJTKZQ#user{=}#message{=}") + strlen(nom) + strlen(event) + 2;
    message = (char*)malloc(tail);
    strcpy(message, "protocol.PFMCOP.DonneePostEvent##tag{=}");
    char* alea = genAleat(8) ;
    strcat(message, alea);
    strcat(message, "#user{=}");
    strcat(message, nom);
    strcat(message, "#message{=}");
    strcat(message, event);
    message[tail] = '\0';

    free(alea);
    alea = NULL;

    return message;
}

char* createDigest(char* pdw, long time, double random)
{
    char *digest = NULL;

    digest = (char*)malloc(50);
    return digest;
}

bool lireNombre(int *pN, int min, int max)
{
    char c;
    int i=0, s=1;
    bool ok=true;
    *pN=0;

    fflush(stdin);

    c=getchar();
    if(c=='-')
    {
        s=-1;
        c=getchar();
    }
    else
    {
        if(c=='+')
        {
            c=getchar();
        }
    }

    while(*pN<=3200 && ok==1 && c!='\n')
    {
        i++;
        if(c>='0' && c<='9')
        {
            *pN=*pN*10+(c-48);
            c=getchar();
        }
        else
        {
            ok=false;
        }
    }

    *pN=*pN*s;

    if(*pN<min || *pN>max)
    {
        ok=false;
    }
    if(i==0)
    {
        ok=false;
    }

    return ok;
}

short LireChaine(char *pc, int max)
{
    char c;
    char v[50];
    char *pv=&v[0];
    short ok;
    int i=0;

    fflush(stdin);
    c=getchar();
    while ((i < 49) && (c != '\n'))
    {
        *pv=c;
        pv++;
        i++;
        c=getchar();
    }
    *pv='\0';

    i = strlen(v);
    if(i > max-1)
    {
        ok=0;
    }
    else
    {
        strcpy(pc,v);
        ok=1;
    }
    return ok;
}

char* genAleat(int len)
{
    char* generatedString = NULL;
    long aleat;

    if(len == 5)
    {
        aleat = rand()%100000;
    }
    else
    {
        aleat = rand()%100000000;
    }
    

    generatedString = (char*)malloc(len+1);

    sprintf(generatedString, "%ld", aleat);

    int size = strlen(generatedString);

    while(size != len)
    {
        strcat(generatedString, "0");
        size = strlen(generatedString);
    }

    generatedString[len] = '\0';


    return generatedString;
}


/********************************/
/*           Thread             */
/********************************/

void* threadReception(void *param)
{
    //cout << "le thread est cree" << endl;

    //Masquage des signaux
	sigset_t mask;
	sigfillset(&mask);
    sigdelset(&mask, SIGINT);
	sigprocmask(SIG_SETMASK, &mask,NULL);	

    struct sockaddr_in addr;
    unsigned int addrlen;
    int sock, cnt;
    char message[MTU];

    /* set up socket */
    sock = socket(AF_INET, SOCK_DGRAM, 0);
    if(sock < 0)
    {
        perror("socket");
        exit(1);
    }
    bzero((char *)&addr, sizeof(addr));
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = inet_addr("234.5.5.9");
    addr.sin_port = htons(5011);
    addrlen = sizeof(addr);

    if(bind(sock, (struct sockaddr *) &addr, sizeof(addr)) < 0)
    {        
        perror("bind");
	    exit(1);
    }    
     
    while (1) 
    {
 	    cnt = recvfrom(sock, message, sizeof(message), 0, (struct sockaddr *) &addr, &addrlen);
        if(cnt < 0) 
        {
            perror("recvfrom");
            exit(1);
        } 
        else if(cnt == 0) 
        {
            
            break;
        }
        message[cnt] = '\0';
		
        decodeEtIsereMessage(message);

        pthread_mutex_lock(&mutexCOut);
        kill(getpid(),SIGUSR2);
        pthread_mutex_unlock(&mutexCOut);
    }

    return NULL;
}

void decodeEtIsereMessage(char* message)
{
    int place = 0;
    char * comp = NULL;

    pthread_mutex_lock(&mutexListes);

    comp = ParcourChaine::myTokenizer(message, '#', &place);
    if(strcmp(comp, "protocol.PFMCOP.DonneeBaseUDP") == 0)
    {
        place = 0;
        int tail;
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '}', &place);
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '#', &place);
        tail = strlen(message) + 1;
        char * mes = (char*)malloc(tail);
        strcpy(mes, comp);
        mes[tail] = '\0';

        free(comp);
        comp = NULL;

        listChat.insere(mes);
    }
    else if(strcmp(comp, "protocol.PFMCOP.DonneePostQuestion") == 0)
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
        char * tag = (char*)malloc(tail);
        strcpy(tag, comp);
        tag[tail] = '\0';
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '}', &place);
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '#', &place);
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '}', &place);
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '#', &place);
        tail = strlen(comp) + 1;
        char * user = (char*)malloc(tail);
        strcpy(user, comp);
        user[tail] = '\0';
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '}', &place);
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '#', &place);
        tail = strlen(comp) + 1;
        char * mes = (char*)malloc(tail);
        strcpy(mes, comp);
        mes[tail] = '\0';
        free(comp);
        comp = NULL;

        tail = strlen(tag) + strlen(mes) + 4;
        char* quest = (char*)malloc(tail);
        strcpy(quest, tag);
        strcat(quest, " : ");
        strcat(quest, mes);
        quest[tail] = '\0';

        tail = strlen(user) + strlen(mes) + 4;
        char* chat = (char*)malloc(tail);
        strcpy(chat, user);
        strcat(chat, " : ");
        strcat(chat, mes);
        chat[tail] = '\0';


        listQuestions.insere(quest);
        listChat.insere(chat);

        free(tag);
        tag = NULL;
        free(user);
        user = NULL;
        free(mes);
        mes = NULL;
    }
    else if(strcmp(comp, "protocol.PFMCOP.DonneeAnswerQuestion") == 0)
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
        char * tag = (char*)malloc(tail);
        strcpy(tag, comp);
        tag[tail] = '\0';
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '}', &place);
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '#', &place);
        tail = strlen(comp) + 1;
        char * user = (char*)malloc(tail);
        strcpy(user, comp);
        user[tail] = '\0';
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '}', &place);
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '#', &place);
        tail = strlen(comp) + 1;
        char * mes = (char*)malloc(tail);
        strcpy(mes, comp);
        mes[tail] = '\0';
        free(comp);
        comp = NULL;

        tail = strlen(tag) + strlen(mes) + strlen(user) + 9;
        char* quest = (char*)malloc(tail);
        strcpy(quest, user);
        strcat(quest, " --> ");
        strcat(quest, tag);
        strcat(quest, " : ");
        strcat(quest, mes);
        quest[tail] = '\0';

        listChat.insere(quest);

        free(tag);
        tag = NULL;
        free(user);
        user = NULL;
        free(mes);
        mes = NULL;
    }
    else if(strcmp(comp, "protocol.PFMCOP.DonneePostEvent") == 0)
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
        char * tag = (char*)malloc(tail);
        strcpy(tag, comp);
        tag[tail] = '\0';
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '}', &place);
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '#', &place);
        tail = strlen(comp) + 1;
        char * user = (char*)malloc(strlen(comp));
        strcpy(user, comp);
        user[tail] = '\0';
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '}', &place);
        free(comp);
        comp = NULL;

        comp = ParcourChaine::myTokenizer(message, '#', &place);
        tail = strlen(comp) + 1;
        char * mes = (char*)malloc(strlen(comp));
        strcpy(mes, comp);
        mes[tail] = '\0';
        free(comp);
        comp = NULL;

        tail = strlen(tag) + strlen(user) + strlen(mes) + 16;
        char* event = (char*)malloc(tail);
        strcpy(event, "/!\\EVENT [");
        strcat(event, tag);
        strcat(event, "] ");
        strcat(event, user);
        strcat(event, " : ");
        strcat(event, mes);
        event[tail] = '\0';

        listChat.insere(event);

        free(tag);
        tag = NULL;
        free(user);
        user = NULL;
        free(mes);
        mes = NULL;
    }


    pthread_mutex_unlock(&mutexListes);
}

void HandlerMsg(int Sig)
{
	//cout << "Réception du signal SIGUSR2" << endl;
    //goto avantBoucle;
}
