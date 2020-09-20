#ifndef TRACE_H
#define TRACE_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#ifdef CPP
#include <typeinfo>
#include <iostream>
using namespace std;
#else
#endif

#ifdef SUN
#include <sys/varargs.h>
#endif

#ifdef LINUX
#include <stdarg.h>
#endif

#define ROUGE "\033[0;31m"
#define JAUNE "\033[0;33m"
#define BLEU "\033[0;34m"

void Affiche_hidden(const char* couleur, const char* entete ,const char* ficher, int ligne, const char *pMessage, ...);
#define Affiche(message, ...) Affiche_hidden(BLEU,message,__FILE__,__LINE__,__VA_ARGS__)
#define Warning(...) Affiche_hidden(JAUNE,"WARNING",__FILE__,__LINE__,__VA_ARGS__)
#define Error(...) Affiche_hidden(ROUGE,"ERROR",__FILE__,__LINE__,__VA_ARGS__)

#endif