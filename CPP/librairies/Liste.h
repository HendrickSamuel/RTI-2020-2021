/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 08/10/2020             */
/***********************************************************/

#ifndef LISTE_H
#define LISTE_H

#include <iostream>
#include "ListeBase.h"

using namespace std;

template<class T> class Liste : public ListeBase<T>
{	

	private:
		
	public:
		//constructeurs
		Liste(void);
		Liste(const Liste& L);
		//destructeur
		~Liste();
				
		//operators

		//getters
		
		//setters
		
		//methodes
		void insere(const T &val);

};

#endif //LISTE_H
