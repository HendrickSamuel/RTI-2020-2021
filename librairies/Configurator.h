/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#ifndef CONFIGURATOR_H
#define CONFIGURATOR_H

#include <stdio.h>
#include <iostream>
#include <string.h>
#include <fstream>
#include "BaseException.h"

using namespace std;
class Configurator
{
    private:

    public:
        //constructeurs

        //destructeurs

        //operators

        //getters

        //setters

        //méthodes
        static char* getProperty(const char* fileName, const char* property);
        static bool getLog(const char* filename, char* name, char* pwd);
};

#endif