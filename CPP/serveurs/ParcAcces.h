/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 25/09/2020             */
/***********************************************************/

#ifndef PACRACCES_H
#define PACRACCES_H

#include "StructParc.h"
#include <list>
#include <stdio.h>     // FILE structure
#include <iostream>
#include <string.h>

using namespace std;
class parcAcces
{
    private: 
        const char* fileName;
    public: 
        //constructeurs
        parcAcces(const char* parcAcces);

        //méthodes
        void initFichPark();
        bool addRecord(struct fich_parc record);
        bool hasRecord(struct fich_parc record);
        bool getRecord(struct fich_parc *record, int id);
        bool removeRecord(struct fich_parc record);
        bool updateRecord(struct fich_parc record);
        bool searchPlace(struct fich_parc *record);
        char* searchDestination(const char *destination);
        void debugFichPark();
};
#endif //PACRACCES_H