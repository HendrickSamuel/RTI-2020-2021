/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include "CMMP.h"
#include <iostream>
#include <string.h>
#include "SocketsClient.h"
#include "BaseException.h"
#include "Configurator.h"

using namespace std;

int main(int argc, char *argv[])
{
    int ret;
    SocketsClient  socket;
    struct protocole proto;

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
        socket.initSocket(adresse, port);
        
        getchar();




        proto.type = Login;
        strcpy(proto.donnees.login.nom, "Test");
        strcpy(proto.donnees.login.pwd, "1234");
        
        socket.sendStruct(&proto);

        getchar();

        proto.type = Logout;
        strcpy(proto.donnees.logout.nom, "Test");
        strcpy(proto.donnees.logout.pwd, "1234");        
        
        socket.sendStruct(&proto);

        getchar();

        socket.closeSocket();
     
    }
    catch(BaseException& e)
    {
        cerr << e.getMessage() << '\n';
    }

    return 0;
}
