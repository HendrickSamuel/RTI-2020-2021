/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include "CMMP.h"
//#include "Trace.h"
#include "liste.h"
#include <stdio.h> 
#include "Output.h"
#include <iostream>
#include <string.h>
#include "iterateur.h"
#include "Configurator.h"
#include "ParcourChaine.h"
#include "SocketsClient.h"
#include "BaseException.h"
#include "SocketsServeur.h"

#define MTU 1000

using namespace std;


/********************************/
/*          Prototypes          */
/********************************/

void    afficheMenu();
void    afficheEntete();
void    switchReceive(char *retour);
void    switchSend(int choix, struct protocole &proto);

void*   threadUrgence(void *param);


/********************************/
/*      Variables globales      */
/********************************/

Liste<Output>	listeOut;
char            idTransport[MAXSTRING];

pthread_t threadUrg;

/********************************/
/*             Main             */
/********************************/

int main(int argc, char *argv[])
{
    int port, choix;

    bool run = true;
    char *portTmp = NULL;
    char *adresse = NULL;
    char* retour = NULL;
    SocketsClient  socket;
    struct protocole proto;


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
        socket.initSocket(adresse, port);
        
        retour = socket.receiveString(MTU, '#', '%');

        int type;
        char *succes = NULL;
        char *message = NULL;

        ParcourChaine::getData(retour, &type, &succes, &message);

        if(strcmp(succes, "true") == 0)
        {
                cout << "Succes: " << message << endl;
        }
        else
        {
            cout << "Echec: " << message << endl
            << "Veuillez réessayer plus tard" <<endl;
            socket.closeSocket();
            exit(-1);
        }
        


        cout << "Appuyez sur une touche" << endl;
        getchar();
    }
    catch(BaseException& e)
    {
        cerr << e.getMessage() << '\n';
        exit(0);
    }

    //creation du threadUrgence
    pthread_create(&threadUrg, NULL, threadUrgence, NULL);

    while(run)
    {
        afficheEntete();
        afficheMenu();
        cin >> choix;

        if(choix == OutputOne && listeOut.estVide())
        {
            cout << endl << "OutputOne NOK : il n'y a pas d'elements dans la liste!" << endl << endl;
        }
        else
        {
            switchSend(choix, proto);

            if(choix >= 1 && choix <= 7)
            {
                try
                {
                    cout << "avant envoie" << endl;
                    socket.sendStruct((void*)&proto, sizeof(struct protocole));
cout << "apres envoie" << endl;
                    retour = socket.receiveString(MTU, '#', '%');
                    cout << "apres recieve" << endl;
                }
                catch(BaseException& e)
                {
                    std::cerr << e.getMessage() << '\n';
                }

                switchReceive(retour);
                
                if(retour != NULL)
                    free(retour);

                cout << endl << endl;
            }
        }
        
    }

    try
    {
        socket.closeSocket();
    }
    catch(BaseException& e)
    {
        std::cerr << e.getMessage() << '\n';
    }

    return 0;
}


/********************************/
/*          Fonctions           */
/********************************/

void afficheEntete()
{
	cout << "********************************************************************************************************" << endl;
	cout << "******* Application Containers *************************************************************************" << endl;
	cout << "********************************************************************************************************" << endl;
	cout << endl;
}


void afficheMenu()
{
    cout << "1. Login" << endl;
    cout << "2. Input Truck" << endl;
    cout << "3. Input Done" << endl;
    cout << "4. Output Ready" << endl;
    cout << "5. Output One" << endl;
    cout << "6. Output Done" << endl;
    cout << "7. Logout" << endl << endl;
    cout << "Faites votre choix : ";  
}


//Fonction avec un switch qui crée le protocole qui sera envoyé au serveur
void switchSend(int choix, struct protocole &proto)
{
    switch(choix)
    {
        case Login:
            {
                char nom[MAXSTRING];
                char pwd[MAXSTRING];

                cout << "\tEntrez votre nom : ";
                cin >> nom;
                cout << "\tEntrez votre mot de passe : ";
                cin >> pwd;

                proto.type = Login;
                strcpy(proto.donnees.login.nom, nom);
                strcpy(proto.donnees.login.pwd, pwd);
            }
            break;

        case InputTruck:
            {
                proto.type = InputTruck;
                cout << "\tEntrez l'immatriculation du camion : ";
                cin >> proto.donnees.inputTruck.imCamion;
                cout << "\tEntrez l'identifiant du container : ";
                cin >> proto.donnees.inputTruck.idContainer;
                cout << "\tEntrez le nom de la societe : ";
                cin >> proto.donnees.inputTruck.societe;
                cout << "\tEntrez la destination : ";
                cin >> proto.donnees.inputTruck.destination;
            }
            break;

        case InputDone:
            {
                int reponse;
                proto.type = InputDone;
                cout << "\tLe container est il bien en place : " << endl;
                cout << "\t 1. OUI" << endl;
                cout << "\t 2. NON" << endl;
                cout << "\t Choix : ";
                cin >> reponse;

                if(reponse == 1)
                {
                    proto.donnees.inputDone.etat = true;
                    cout << "\tPoids du container : ";
                    cin >> proto.donnees.inputDone.poids;
                }
                else
                {
                    proto.donnees.inputDone.etat = false;
                }
            }
            break;

        case OutputReady:
            {
                proto.type = OutputReady;
                cout << "\tEntrez l'identifiant du train ou du bateau : ";
                cin >> proto.donnees.outputReady.id;
                strcpy(idTransport, proto.donnees.outputReady.id);
                cout << "\tEntrez la capacite maximale : ";
                cin >> proto.donnees.outputReady.capacite;
                cout << "\tEntrez la destination : ";
                cin >> proto.donnees.outputReady.dest;
            }
            break;

        case OutputOne:
            {
                
                proto.type = OutputOne;
                if(!listeOut.estVide())
                {
                    int choix = 0;
                    Output out;

                    Iterateur<Output> it(listeOut);
                    it.reset();
                    listeOut.Aff();
                    cout << "\n\tEntrez le numero : ";
                    cin >> choix;

                    for(int i = 0 ; i < (choix-1) ; i++)
                    {
                        it++;
                    }

                    out = it.remove();

                    strcpy(proto.donnees.outputOne.idContainer, out.getId());
                }
                else
                {
                    cout << "\nIl n'y a plus de container !";
                }
                
            }
            break;

        case OutputDone:
            {
                proto.type = OutputDone;
                strcpy(proto.donnees.outputDone.id, idTransport);
                proto.donnees.outputDone.nbContainers = listeOut.getNombreElements();
            }
            break;

        case Logout:
            {
                char nom[MAXSTRING];
                char pwd[MAXSTRING];

                cout << "\tEntrer votre nom : ";
                cin >> nom;
                cout << "\tEntrer votre mot de passe : ";
                cin >> pwd;


                proto.type = Logout;
                strcpy(proto.donnees.login.nom, nom);
                strcpy(proto.donnees.login.pwd, pwd);	
            }
            break;

        default:
            cout << "\tChoix incorrecte !!!" << endl;
            break;
    }
}


//Fonction avec un switch qui "décode" le message reçu âr le serveur
void switchReceive(char *retour)
{
    int type;
    char *succes = NULL;
    char *message = NULL;

    ParcourChaine::getData(retour, &type, &succes, &message);

    switch(type)
    {
        case Login:
            /*si connexion acceptée*/
            if(strcmp("true",succes) == 0)
            {
                cout << endl << "Connexion reussie bienvenue " << message << endl << endl;
            }
            else
            {
                cout << endl << "Connexion echouee : " << message << endl << endl;
            }                       
            break;

        case InputTruck:
            /*si InputTruck OK*/
            if(strcmp("true",succes) == 0)
            {
                int coord[2];
                ParcourChaine::getCoordonees(message, coord);
                cout << endl << "InputTruck OK : place du container " << "[" << coord[0] << "] [" << coord[1] << "]" << endl << endl;
            }
            else
            {
                cout << endl << "InputTruck NOK : " << message << endl << endl;  
            }    
            break;

        case InputDone:
            /*si InputDone OK*/
            if(strcmp("true",succes) == 0)
            {
                cout << endl << "InputDone OK : " << message << endl << endl;
            }
            else
            {
                cout << endl << "InputDone NOK : au revoir " << message << endl << endl;
                exit(0);
            }    
            break;

        case OutputReady:
            /*si OutputReady OK*/
            if(strcmp("true",succes) == 0)
            {
                cout << endl << "OutputReady OK : vous trouverez la liste des container sous Output One" << endl << endl;

                ParcourChaine::createListe(message, listeOut);
            }
            else
            {
                cout << endl << "OutputReady NOK : " << message << endl << endl;
            }   
            break;

        case OutputOne:
            /*si OutputOne OK*/
            if(strcmp("true",succes) == 0)
            {
                cout << endl << "OutputOne OK : " << message << endl << endl;
            }
            else
            {
                cout << endl << "OutputOne NOK : " << message << endl << endl;
            }   
            break;

        case OutputDone:
            /*si OutputDone OK*/
            if(strcmp("true",succes) == 0)
            {
                cout << endl << "OutputDone OK : " << message << endl << endl;
                //Comme le chargement est terminé correctement on vide la liste reçue
                listeOut.removeAll();
            }
            else
            {
                cout << endl << "OutputDone NOK : " << message << endl << endl;
            }   
            break;

        case Logout:
            /*si déconnexion acceptée*/
            if(strcmp("true",succes) == 0)
            {
                cout << endl << "Deconnexion reussie au revoir " << message << endl << endl;
                exit(0);
            }
            else
            {
                cout << endl << "Deconnexion echouee :" << message << endl << endl;
            }
            break;
    }

    if(succes != NULL)
        free(succes);

    if(message != NULL)
        free(message);
}



void* threadUrgence(void *param)
{
    SocketsServeur sockEcoute;
    SocketsServeur sockService;

    char *portTmp;
    int port;
    char *adresse;

    try
    {
        portTmp = Configurator::getProperty("test.conf","PORT-URGENCE");
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

    sockEcoute.listenSocket(SOMAXCONN);

    sockService = sockEcoute.acceptSocket();

    char* message = sockService.receiveString(MTU, '#', '%');

    char * comp = NULL;
    int place = 0;

    comp = ParcourChaine::myTokenizer(message, '#', &place);

    cout << endl << comp << endl;
    cout << "merci de fermer l'application" << endl;

    free(comp);
    comp = NULL;
    free(message);
    message = NULL;
}