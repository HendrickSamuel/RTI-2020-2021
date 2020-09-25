/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

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
#endif //CPP

#ifdef SUN
#include <sys/varargs.h>
#endif //SUN

#ifdef LINUX
#include <stdarg.h>
#endif //LINUX

#define ROUGE "\033[0;31m"
#define JAUNE "\033[0;33m"
#define BLEU "\033[0;34m"

void Affiche_hidden(const char* couleur, const char* entete ,const char* ficher, int ligne, const char *pMessage, ...);
#define Affiche(message, ...) Affiche_hidden(BLEU,message,__FILE__,__LINE__,__VA_ARGS__)
#define Warning(message,...) Affiche_hidden(JAUNE,message,__FILE__,__LINE__,__VA_ARGS__)
#define Error(message,...) Affiche_hidden(ROUGE,message,__FILE__,__LINE__,__VA_ARGS__)

void EffEcran(void);

#endif //TRACE_H