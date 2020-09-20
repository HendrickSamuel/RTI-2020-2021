#include "Trace.h"

void Affiche_hidden(const char* couleur, const char* entete ,const char* ficher, int ligne, const char *pMessage, ...)
{
    char Buffer[255];
    va_list arg;
    va_start(arg,pMessage);
    vsprintf(Buffer,pMessage,arg);
    #ifdef CPP
    cout << "(" << ficher << " - " << ligne << ") " << couleur << "<" << entete <<"> \033[0m" << Buffer;
    #else
    printf("(%s - %d) %s<%s>\033[0m %s", ficher, ligne, couleur, entete, Buffer);
    va_end(arg);
    #endif
    return ;
}