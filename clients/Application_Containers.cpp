/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include "CMMP.h"
#include <iostream>
#include "Sockets.h"
#include "BaseException.h"

using namespace std;

int main(int argc, char *argv[])
{
    int ret;
    Sockets  socket;
    struct protocole proto;
    struct sockaddr_in adresse;
    
    try
    {
        getchar();
        socket.Create();
        
        adresse = socket.getAdressByName("192.168.23.131");
        adresse.sin_family = AF_INET;
        adresse.sin_port = htons(PORT);
        printf("Adresse IP = %s\n",inet_ntoa(adresse.sin_addr));


        if (( ret = connect(socket.gethSocket(), (struct sockaddr *)&adresse, sizeof(struct sockaddr_in)))== -1)
        {
            printf("Erreur sur connect de la socket %d\n", errno);
            switch(errno)
            {
                case EBADF : 
                    printf("EBADF - hSocketEcouten'existe pas\n");
                    break;

                case ENOTSOCK :
                    printf("ENOTSOCK - hSocketEcouteidentifie un fichier\n");
                    break;

                case EAFNOSUPPORT :
                    printf("EAFNOTSUPPORT - adresse ne correspond pas famille\n");
                    break;

                case EISCONN :
                    printf("EISCONN - socket deja connectee\n");
                    break;

                case ECONNREFUSED :
                    printf("ECONNREFUSED - connexion refusee par le serveur\n");
                    break;

                case ETIMEDOUT :
                    printf("ETIMEDOUT - time out sur connexion \n");
                    break;

                case ENETUNREACH :
                    printf("ENETUNREACH - cible hors d'atteinte\n");
                    break;

                case EINTR :
                    printf("EINTR - interruption par signal\n");
                    break;
                    
                default :
                    printf("Erreur inconnue ?\n");
            }
            //close(hSocketClient); exit(1);
        }
        else 
            printf("Connect socket OK\n");

        getchar();

        proto.commande = LOGIN;

        if(send(socket.gethSocket(), (void*)&proto, sizeof(struct protocole),0) == -1)
        {
            Error("ERREUR");
        }
        else
        {
            Affiche("OK","OK");
        }
        getchar();

        proto.commande = LOGOUT;

        if(send(socket.gethSocket(), (void*)&proto, sizeof(struct protocole),0) == -1)
        {
            Error("ERREUR");
        }
        else
        {
            Affiche("OK","OK");
        }
        getchar();

        socket.Close();
        
    }
    catch(BaseException& e)
    {
        cerr << e.getMessage() << '\n';
    }

    return 0;
}
