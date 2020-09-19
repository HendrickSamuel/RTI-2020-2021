#ifndef TRACE_H
#define TRACE_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/varargs.h>

#define ROUGE "\033[0;31m"
#define JAUNE "\033[0;33m"
#define BLEU "\033[0;34m"

void Affiche_hidden(const char* couleur, const char* entete ,const char* ficher, int ligne, const char *pMessage, ...);
#define Affiche(...) Affiche_hidden(BLEU,"MESSAGE",__FILE__,__LINE__,__VA_ARGS__)
#define Warning(...) Affiche_hidden(JAUNE,"WARNING",__FILE__,__LINE__,__VA_ARGS__)
#define Error(...) Affiche_hidden(ROUGE,"ERROR",__FILE__,__LINE__,__VA_ARGS__)

#endif