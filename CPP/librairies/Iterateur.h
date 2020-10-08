/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 08/10/2020             */
/***********************************************************/

#ifndef ITERATEUR_H
#define ITERATEUR_H


#include "ListeBase.h"
#include "BaseException.h"


using namespace std;

template<class T> class Iterateur
{	

	private:
		ListeBase<T> &lis;
		struct Cellule<T> *pCur;
		
	public:
		//constructeurs
		Iterateur(ListeBase<T> &l):lis(l),pCur(l.getTete()) {};

		//destructeur	
				
		//operators
		void operator++();
		void operator++(int);
		operator T() const;
		T& operator&();
		
		//getters
		
		//setters
		
		//methodes
		void reset();
		bool end() const;
		T remove();
		Cellule<T>* getpCur() const;

};

#endif //ITERATEUR_H
