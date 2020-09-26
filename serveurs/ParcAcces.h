/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 25/09/2020             */
/***********************************************************/

#ifndef PACRACCES_H
#define PACRACCES_H

#include "StructParc.h"
#include <stdio.h>     // FILE structure
#include <iostream>
#include <string.h>

using namespace std;
class parcAcces
{
    private: 
        const char* fileName;
    public: 
        parcAcces(const char* parcAcces);

        bool addRecord(struct fich_parc record);
        bool hasRecord(struct fich_parc record);
        bool getRecord(struct fich_parc *record, int id);
        bool removeRecord(struct fich_parc record);
        bool updateRemove(struct fich_parc record);
};
#endif //PACRACCES_H