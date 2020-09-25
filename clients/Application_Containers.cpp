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
void afficheMenu1();
void afficheMenu2();

int main(int argc, char *argv[])
{
    int port, choix;
    bool connecte = true;
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

    do
    {
        EffEcran();
        afficheEntete();
        afficheMenu1();

        cin >> choix;

        if(choix == 1)
        {
            connecte = true;
        }

        while(connecte)
        {
            char nom[MAXSTRING];
            char pwd[MAXSTRING];

            EffEcran();
            afficheEntete();
            afficheMenu2();
            cin >> choix;

            switch(choix)
			{
				case 1:
					cout << "\tEntrer votre nom : " << endl;
                    cin >> nom;
                    cout << "\tEntrer votre mot de passe : " << endl;
                    cin >> pwd;

                    proto.type = Login;
                    strcpy(proto.donnees.login.nom, nom);
                    strcpy(proto.donnees.login.pwd, pwd);
					break;

                case 2:
					
					break;

                case 3:
					
					break;

                case 4:
					
					break;

                case 5:
					
					break;

                case 6:
					cout << "\tEntrer votre nom : " << endl;
                    cin >> nom;
                    cout << "\tEntrer votre mot de passe : " << endl;
                    cin >> pwd;

                    proto.type = Logout;
                    strcpy(proto.donnees.login.nom, nom);
                    strcpy(proto.donnees.login.pwd, pwd);	
					break;

                default:

                    break;
            }

            try
            {
                socket.sendStruct((void*)&proto, sizeof(proto));
            }
            catch(BaseException& e)
            {
                cerr << e.getMessage() << '\n';
            }
            cout << "Appuyez sur une touche" << endl;
            getchar();
        }

    }while (1);
    

    try
    {
        socket.closeSocket();
    }
    catch(BaseException& e)
    {
        cerr << e.getMessage() << '\n';
    }

    return 0;
}


void afficheEntete()
{
	cout << "********************************************************************************************************" << endl;
	cout << "******* Application Containers *************************************************************************" << endl;
	cout << "********************************************************************************************************" << endl;
	cout << endl << endl;
}


void afficheMenu1()
{
    cout << "1. Connexion" << endl;
    cout << "2. Quitter" << endl;
    cout << "Faites votre choix : ";
}


void afficheMenu2()
{
    cout << "1. Input Truck" << endl;
    cout << "2. Input Done" << endl;
    cout << "3. Output Ready" << endl;
    cout << "4. Output One" << endl;
    cout << "5. Output Done" << endl;
    cout << "6. Logout" << endl;
    cout << "Faites votre choix : ";  
}