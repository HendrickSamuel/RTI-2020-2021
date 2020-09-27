/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#ifndef STRUCTPARC_H
#define STRUCTPARC_H

#define MAX_CHAINE 255

struct fich_parc
{
    int id;
    float x;
    float y;
    int flagemplacement;
    char datereservation[MAX_CHAINE];
    char datearrivee[MAX_CHAINE];
    float poids;
    char destination[MAX_CHAINE];
    char moyenTransport[MAX_CHAINE];
};


#endif //STRUCTPARC_H
