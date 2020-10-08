/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#ifndef STRUCTPARC_H
#define STRUCTPARC_H

#define MAX_CHAINE 255
#define ID_LENGTH 18

struct fich_parc
{
    char id[ID_LENGTH];
    int x;
    int y;
    int flagemplacement;
    char datereservation[MAX_CHAINE];
    char datearrivee[MAX_CHAINE];
    int poids;
    char destination[MAX_CHAINE];
    char moyenTransport[MAX_CHAINE];
};

#endif //STRUCTPARC_H
