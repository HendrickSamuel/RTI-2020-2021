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
#include "SocketsClient.h"
#include "BaseException.h"
#include "Configurator.h"

#define MTU 5000

using namespace std;


/********************************/
/*          Prototypes          */
/********************************/

void    afficheMenu();
void    afficheEntete();
int     getType(char *retour);
char*   getSucces(char *retour);
char*   getMessage(char *retour);
void    createListe(char *donnees);
void    switchReceive(char *retour);
void    getCoordonees(char *retour, int *coordonees);
void    switchSend(int choix, struct protocole &proto);
char*   myTokenizer(char *tampon, char token, int *place);
void    getData(char *retour, int *type, char **succes, char **message);


/********************************/
/*      Variables globales      */
/********************************/

Liste<Output>	listeOut;

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

        cout << "Appuyez sur une touche" << endl;
        getchar();
    }
    catch(BaseException& e)
    {
        cerr << e.getMessage() << '\n';
        exit(0);
    }

    while(run)
    {
        afficheEntete();
        afficheMenu();
        cin >> choix;

        if(choix == OutputOne && listeOut.estVide())
        {
            cout << "il n'y a pas d'elements dans la liste" << endl;
        }
        else
        {
            switchSend(choix, proto);

            if(choix >= 1 && choix <= 7)
            {
                try
                {
                    socket.sendStruct((void*)&proto, sizeof(struct protocole));

                    retour = socket.receiveString(MTU, '#', '%');
                }
                catch(BaseException& e)
                {
                    std::cerr << e.getMessage() << '\n';
                }

                switchReceive(retour);
                
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
                cout << "\tEntrez la capacite maximale : ";
                cin >> proto.donnees.outputReady.capacite;
                cout << "\tEntrez la destination : ";
                cin >> proto.donnees.outputReady.dest;
            }
            break;

        case OutputOne:
            {
                //TODO: voir comment recupérer le container le plus ancien
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
                cout << "\tEntrez l'identifiant du train ou du bateau : ";
                cin >> proto.donnees.outputDone.id;
                cout << "\tEntrez le nombre de containers chargé : ";
                cin >> proto.donnees.outputDone.nbContainers;
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

    getData(retour, &type, &succes, &message);

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
                getCoordonees(message, coord);
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

                createListe(message);
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
}


//Fonction qui retourne le type de requête hors de la trame
int getType(char *retour)
{
    char *pch;
    int place = 0;
    
    pch = myTokenizer(retour, '#', &place);

    return atoi(pch);
}


//Fonction qui retourne la "validite" de la requête hors de la trame
char* getSucces(char *retour)
{
    char *pch;
    int place = 0;
    
    pch = myTokenizer(retour, '#', &place);
    free(pch);

    pch = myTokenizer(retour, '#', &place);
    return pch;
}


//Fonction qui retourne le message de la trame (peut contenir des données)
char* getMessage(char *retour)
{
    char *pch;
    int place = 0;

    pch = myTokenizer(retour, '#', &place);
    free(pch);
    pch = myTokenizer(retour, '#', &place);
    free(pch);

    pch = myTokenizer(retour, '#', &place);
    return pch;
}


//Fonction qui récupere les coodronées dans le message
void getCoordonees(char *retour, int *coordonees)
{
    char *pch;
    int place = 0;

    pch = myTokenizer(retour, '/', &place);
    coordonees[0] = atoi(pch);
    free(pch);

    pch = myTokenizer(retour, '\0', &place);
    coordonees[1] = atoi(pch);
    free(pch);
}


//Fonction qui permet d'avoir le type, la validité et le message d'un coup
void getData(char *retour, int *type, char **succes, char **message)
{
    char *pch;
    int place = 0;

    pch = myTokenizer(retour, '#', &place);
    *type = atoi(pch);
    free(pch);

    pch = myTokenizer(retour, '#', &place);
    *succes = pch;

    pch = myTokenizer(retour, '#', &place);
    *message = pch;  
}


//Séparateur de chaine de caractère
char* myTokenizer(char *tampon, char token, int *place)
{
    bool search = true;
    char *retour = NULL;
    char *pT = NULL;
    int taille = 0;
    pT = tampon;

    while(search)
    {
        if(pT[*place] == token)
        {
            search = false;
        }
        else if(pT[*place] == '\0')
        {
            search = false;
        }
        else
        {
            (*place)++;
            taille++;
        }
    }

    if(taille > 0)
    {
        retour = (char*)malloc(taille+1);
        memcpy(retour, &pT[*place-taille], (taille));
        retour[taille] = '\0';

        if(pT[*place] != '\0')
        {
            (*place)++;
        }
        return retour;
    }
    else
    {
        return NULL;
    }
}


//Fonction qui crée la liste de container 
//    à afficher pour le Output One
void createListe(char *donnees)
{
    char *pch;
    int place = 0;
    Output out;

    while( (pch = myTokenizer(donnees, '@', &place)) != NULL)
    {
        out.setId(pch);

        pch = myTokenizer(donnees, '@', &place);
        out.setX(atoi(pch));
        free(pch);

        pch = myTokenizer(donnees, '/', &place);
        out.setY(atoi(pch));
        free(pch);

        listeOut.insere(out);
    }
}