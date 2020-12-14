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
int     afficheMenu();

char*   createDigest(char* pdw, long time, double random);
int     lireNombre(int *pN, int min, int max);


/********************************/
/*      Variables globales      */
/********************************/

pthread_mutex_t mutexListes;

pthread_t thread;

Liste<char*> listQuestions;
Liste<char*> listChat;




/********************************/
/*             Main             */
/********************************/


int main(int argc, char *argv[])
{
    int port;
     
    char nom[MAXSTRING];
    char pwd[MAXSTRING];
    char* digestBase64;

    long time;
    double random;

    char *portTmp = NULL;
    char *adresse = NULL;
    char* retour = NULL;
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
    catch(BaseException ex)
    {
        cout << ex.getMessage() << endl;
        exit(0);
    }

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
        
        int tail = strlen("protocol.PFMCOP.DonneeLoginGroup##username{=}#pwdDigest{=}#temps{=}#aleatoire{=}#adresse{=}null#port{=}0") + 1 + strlen(nom) + strlen(digestBase64) + strlen(temps) + strlen(aleat);
        char * mes = (char*)malloc(tail);
        strcpy(mes, "protocol.PFMCOP.DonneeLoginGroup##username{=}");
        strcat(mes, nom);
        strcat(mes, "#pwdDigest{=}");
        strcat(mes, digestBase64);
        strcat(mes, "#temps{=}");
        strcat(mes, temps);
        strcat(mes, "#aleatoire{=}");
        strcat(mes, aleat);
        strcat(mes, "#adresse{=}null#port{=}0");
        mes[tail-1] = '\n';

        //envoie et réception
        socketCli.sendString(mes, tail);

        //retour = socket.receiveString(MTU, '#', '%');

    //} while ();
 
    socketCli.closeSocket();

    //init mutex
    pthread_mutex_init(&mutexListes, NULL);

    //creation du thread
    pthread_create(&thread, NULL, threadReception, NULL);

    //on réarme le signal SIGUSR2 pour le main
    sigdelset(&mask, SIGUSR2);
	sigprocmask(SIG_SETMASK, &mask,NULL);

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

    unsigned char multicastTTL = 1;
    setsockopt(hSocket, IPPROTO_IP, IP_MULTICAST_TTL, (void *) &multicastTTL, sizeof(multicastTTL));

    
    struct sockaddr_in groupSock;
    memset((char*)&groupSock, 0, sizeof(groupSock));
    groupSock.sin_family = AF_INET;
    groupSock.sin_addr.s_addr = inet_addr("234.5.5.9");
    groupSock.sin_port = htons(5011);

    struct in_addr localInterface;
    struct hostent *infosHost;
    infosHost = gethostbyname("localhost");
    memcpy(&localInterface.s_addr, infosHost->h_addr, infosHost->h_length);
    //localInterface.s_addr = inet_addr("234.5.5.9");
    if(setsockopt(hSocket, IPPROTO_IP, IP_MULTICAST_IF, (char *)&localInterface, sizeof(localInterface)) < 0)
    {
        perror("Setting local interface error");
        exit(1);
    }
    else
      printf("Setting the local interface...OK\n");


cout << "avant creation message " << endl;
    char *message = NULL;
    message = (char*)malloc(strlen("protocol.PFMCOP.DonneeBaseUDP##username{=} rejoint le groupe") + strlen(nom));
    strcpy(message, "protocol.PFMCOP.DonneeBaseUDP##username{=}");
    strcat(message, nom);
    strcat(message, " rejoint le groupe");

    int cnt = sendto(hSocket, message, strlen(message), 0, (struct sockaddr*) &groupSock, sizeof(groupSock));
  	if(cnt < 0) 
    {
 	    perror("sendto");
	    exit(1);
	}
    else  
        cout << "message envoyé : " << message << endl;
    
    sleep(5);


avantBoucle:
    int choix = 1;
    while (choix != 0)
    {
        effEcran();

        afficheEntete();
        afficheEnteteChat();
        afficheChat();
        choix = afficheMenu();
        
    }
    
    if(choix != 0)
        goto avantBoucle;

    return 0;
}



/********************************/
/*          Fonctions           */
/********************************/

void effEcran()
{
    cout << "\033[2J" ;
}

void affChaine(const char *pChaine,int Lig,int Col)
{
    cout << "\033[" <<  Lig <<  ";" <<  Col <<  "H\033[" <<  pChaine << "\033[0m";
}

void afficheEntete()
{
	affChaine("****************************************************************************************************************************************************", 1, 1);
	cout << endl << "*                              Application Chat PFM                                                                                               *" << endl;
	cout << "***************************************************************************************************************************************************" << endl;
}

void afficheEnteteChat()
{
	affChaine("**            Questions                                      *                                  Chat                                               *", 4, 1);
    cout << endl << "***************************************************************************************************************************************************" << endl;
}

void afficheEnteteLogin()
{
	affChaine("**                                                                     Login                                                                       *",4 , 1);
    cout << endl << "***************************************************************************************************************************************************" << endl;
}

void afficheChat()
{
    affChaine("**question", 6, 1);
    affChaine("**messages", 6, 61);
    affChaine("**", 6, 147);
    cout << endl << "*                                                           *                                                                                     *" << endl;
    cout << "***************************************************************************************************************************************************" << endl;
}

int afficheMenu()
{
    int choix;
    cout << endl << endl;
    cout << "\t" << "0 : Quitter" << endl;
    cout << "\t" << "1 : Post question" << endl;
    cout << "\t" << "2 : Answer question" << endl;
    cout << "\t" << "3 : Post event" << endl;
    cout << "\t\t" << "Faites votre choix : ";
    while(!lireNombre(&choix, 0, 4))
        cout << "erreur" << endl << "\t\t" << "Faites votre choix : "; 
    return choix;
}

char* createDigest(char* pdw, long time, double random)
{
    char *digest = NULL;

    digest = (char*)malloc(50);
    return digest;
}

int lireNombre(int *pN, int min, int max)
{
    char c;
    int i=0, s=1, ok=1;
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
            ok=0;
        }
    }

    *pN=*pN*s;

    if(*pN<min || *pN>max)
    {
        ok=0;
    }
    if(i==0)
    {
        ok=0;
    }

    return ok;

}


/********************************/
/*           Thread             */
/********************************/

void* threadReception(void *param)
{
    //cout << "le thread est cree" << endl;

    struct sockaddr_in addr;
    unsigned int addrlen;
    int sock, cnt;
    struct ip_mreq mreq;
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
    mreq.imr_multiaddr.s_addr = inet_addr("234.5.5.9");         
    mreq.imr_interface.s_addr = htonl(5011);         
   /* if(setsockopt(sock, IPPROTO_IP, IP_ADD_MEMBERSHIP, &mreq, sizeof(mreq)) < 0) 
    {
	    perror("setsockopt mreq");
	    exit(1);
    }   */      
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
	    printf("%s: message = \"%s\"\n", inet_ntoa(addr.sin_addr), message);

        listChat.insere(message);
    }

    sleep(10);
    kill(getpid(),SIGUSR2);

    return NULL;
}

void HandlerMsg(int Sig)
{
	//cout << "Réception du signal SIGUSR2" << endl;
	
}
