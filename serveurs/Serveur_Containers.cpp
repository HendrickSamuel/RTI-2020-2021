/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/
#include "Sockets.h"
#include "BaseException.h"
#include <iostream>

using namespace std;
int main(int argc, char *argv[])
{
    Sockets socket;

    try
    {
        socket.SocketCreate();
    }
    catch(BaseException& e)
    {
        cerr << e.getMessage() << '\n';
    }

}