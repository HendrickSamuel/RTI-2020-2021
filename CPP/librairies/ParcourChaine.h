/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 28/11/2020             */
/***********************************************************/

#ifndef PARCOURCHAINE_H
#define PARCOURCHAINE_H

#include <stdio.h> 
#include <iostream>
#include <string.h>
#include "Output.h"
#include "liste.h"

using namespace std;

class ParcourChaine
{	

	private:
		
	public:
		//constructeurs

		//destructeur
				
		//operators

		//getters
		
		//setters
		
		//methodes
        static int getType(char *retour);
        static char* getSucces(char *retour);
        static char* getMessage(char *retour);
        static void getCoordonees(char *retour, int *coordonees);
        static void getData(char *retour, int *type, char **succes, char **message);
        static char* myTokenizer(char *tampon, char token, int *place);
        static void createListe(char *donnees, Liste<Output> &listeOut);

		static char* getSuccesServeur(char *retour);
};

#endif //PARCOURCHAINE_H
