/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 28/11/2020             */
/***********************************************************/

#include "ParcourChaine.h"

/********************************/
/*                              */
/*            Methodes          */
/*                              */
/********************************/

//Fonction qui retourne le type de requête hors de la trame
int ParcourChaine::getType(char *retour)
{
    char *pch;
    int place = 0;
    
    pch = myTokenizer(retour, '#', &place);

    return atoi(pch);
}


//Fonction qui retourne la "validite" de la requête hors de la trame
char* ParcourChaine::getSucces(char *retour)
{
    char *pch;
    int place = 0;
    
    pch = myTokenizer(retour, '#', &place);
    free(pch);

    pch = myTokenizer(retour, '#', &place);
    return pch;
}


//Fonction qui retourne le message de la trame (peut contenir des données)
char* ParcourChaine::getMessage(char *retour)
{
    char *pch;
    int place = 0;

    pch = myTokenizer(retour, '#', &place);
    free(pch);
    pch = myTokenizer(retour, '#', &place);
    free(pch);

    pch = myTokenizer(retour, '#', &place);
    return pch;
}


//Fonction qui récupere les coodronées dans le message
void ParcourChaine::getCoordonees(char *retour, int *coordonees)
{
    char *pch;
    int place = 0;

    pch = myTokenizer(retour, '/', &place);
    coordonees[0] = atoi(pch);
    free(pch);

    pch = myTokenizer(retour, '\0', &place);
    coordonees[1] = atoi(pch);
    free(pch);
}


//Fonction qui permet d'avoir le type, la validité et le message d'un coup
void ParcourChaine::getData(char *retour, int *type, char **succes, char **message)
{
    char *pch;
    int place = 0;

    pch = myTokenizer(retour, '#', &place);
    *type = atoi(pch);
    free(pch);

    pch = myTokenizer(retour, '#', &place);
    *succes = pch;

    pch = myTokenizer(retour, '#', &place);
    *message = pch;  
}


//Séparateur de chaine de caractère
char* ParcourChaine::myTokenizer(char *tampon, char token, int *place)
{
    bool search = true;
    char *retour = NULL;
    char *pT = NULL;
    int taille = 0;
    pT = tampon;

    while(search)
    {
        if(pT[*place] == token)
        {
            search = false;
        }
        else if(pT[*place] == '\0')
        {
            search = false;
        }
        else
        {
            (*place)++;
            taille++;
        }
    }

    if(taille > 0)
    {
        retour = (char*)malloc(taille+1);
        memcpy(retour, &pT[*place-taille], (taille));
        retour[taille] = '\0';

        if(pT[*place] != '\0')
        {
            (*place)++;
        }
        return retour;
    }
    else
    {
        return NULL;
    }
}


//Fonction qui crée la liste de container 
//    à afficher pour le Output One
void ParcourChaine::createListe(char *donnees, Liste<Output> &listeOut)
{
    char *pch;
    int place = 0;
    Output out;

    while( (pch = myTokenizer(donnees, '@', &place)) != NULL)
    {
        out.setId(pch);

        pch = myTokenizer(donnees, '@', &place);
        out.setX(atoi(pch));
        free(pch);

        pch = myTokenizer(donnees, '/', &place);
        out.setY(atoi(pch));
        free(pch);

        listeOut.insere(out);
    }
}