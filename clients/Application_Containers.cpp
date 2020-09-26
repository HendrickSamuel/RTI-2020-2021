/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include "CMMP.h"
#include "Trace.h"
#include <iostream>
#include <string.h>
#include "SocketsClient.h"
#include "BaseException.h"
#include "Configurator.h"

using namespace std;

void afficheEntete();
void afficheMenu();

int main(int argc, char *argv[])
{
    int port, choix;
    bool run = true;
    char *portTmp, *adresse;
    SocketsClient  socket;
    struct protocole proto;


//    memset(&proto, 0, sizeof(struct protocole));
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
    }

    while(run)
    {
        EffEcran();
        afficheEntete();
        afficheMenu();
        cin >> choix;

        /*switch du choix client*/
        switch(choix)
        {
            case 1:
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

            case 2:
                {
                    proto.type = InputTruck;
                    cout << "\tEntrez l'immatriculation du camion : ";
                    cin >> proto.donnees.inputTruck.imCamion;
                    cout << "\tEntrez l'identifiant du container : ";
                    cin >> proto.donnees.inputTruck.idContainer;
                }
                break;

            case 3:
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

            case 4:
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

            case 5:
                {
                    //TODO: voir comment recupérer le container le plus ancien
                    proto.type = OutputOne;
                    cout << "\tEntrez l'identifiant du container : ";   // provisoire
                    cin >> proto.donnees.outputOne.idContainer;         // provisoire
                }
                break;

            case 6:
                {
                    //TODO: A mon avis message automatique, avec une validation d'envoie quand meme
                    proto.type = OutputDone;
                    cout << "\tEntrez l'identifiant du train ou du bateau : ";
                    cin >> proto.donnees.outputDone.id;
                    cout << "\tEntrez le nombre de containers chargé : ";
                    cin >> proto.donnees.outputDone.nbContainers;
                }
                break;

            case 7:
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

        if(choix >= 1 && choix <= 7)
        {
            try
            {
                socket.sendStruct((void*)&proto, sizeof(struct protocole));

                socket.receiveStruct((void*)&proto, sizeof(struct protocole));
            }
            catch(BaseException& e)
            {
                std::cerr << e.getMessage() << '\n';
            }

            /*switch de la réponse du serveur*/
            switch(proto.type)
            {
                case 1:
                    /*si connexion acceptée*/
                    if(proto.donnees.reponse.succes)
                    {
                        cout << "Connexion reussie" << endl;
                    }
                    else
                    {
                        cout << "Connexion echouee : " << proto.donnees.reponse.message << endl;
                    }                       
                    break;

                case 2:
                    /*si InputTruck OK*/
                    if(proto.donnees.reponse.succes)
                    {
                        cout << "InputTruck OK : " << proto.donnees.reponse.message << endl;
                    }
                    else
                    {
                        cout << "InputTruck NOK : " << proto.donnees.reponse.message << endl;
                    }    
                    break;

                case 3:
                    /*si InputDone OK*/
                    if(proto.donnees.reponse.succes)
                    {
                        cout << "InputDone OK : " << proto.donnees.reponse.message << endl;
                    }
                    else
                    {
                        cout << "InputDone NOK : " << proto.donnees.reponse.message << endl;
                    }    
                    break;

                case 4:
                    /*si OutputReady OK*/
                    if(proto.donnees.reponse.succes)
                    {
                        cout << "OutputReady OK : " << proto.donnees.reponse.message << endl;
                    }
                    else
                    {
                        cout << "OutputReady NOK : " << proto.donnees.reponse.message << endl;
                    }   
                    break;

                case 5:
                    /*si OutputOne OK*/
                    if(proto.donnees.reponse.succes)
                    {
                        cout << "OutputOne OK : " << proto.donnees.reponse.message << endl;
                    }
                    else
                    {
                        cout << "OutputOne NOK : " << proto.donnees.reponse.message << endl;
                    }   
                    break;

                case 6:
                    /*si OutputDone OK*/
                    if(proto.donnees.reponse.succes)
                    {
                        cout << "OutputDone OK : " << proto.donnees.reponse.message << endl;
                    }
                    else
                    {
                        cout << "OutputDone NOK : " << proto.donnees.reponse.message << endl;
                    }   
                    break;

                case 7:
                    /*si déconnexion acceptée*/
                    if(proto.donnees.reponse.succes)
                    {
                        cout << "Deconnexion reussie :" << proto.donnees.reponse.message << endl;
                        run = false;
                    }
                    else
                    {
                        cout << "Deconnexion echouee :" << proto.donnees.reponse.message << endl;
                    }
                    break;
            } 

            cout << "\tAppuyez sur une touche" << endl;
            cin >> choix;
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