/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 08/10/2020             */
/***********************************************************/

#include "Liste.h"

/********************************/
/*                              */
/*         Constructeurs        */			
/*                              */
/********************************/

//Constructeur par défaut
template <class T> Liste<T>::Liste(void):ListeBase<T>()
{

}

//Constructeur de copie
template <class T> Liste<T>::Liste(const Liste& L):ListeBase<T>(L)
{

}
		
/********************************/
/*                              */
/*          Destructeurs        */			
/*                              */
/********************************/

template <class T> Liste<T>::~Liste()
{

}

/********************************/
/*                              */
/*           Operators          */			
/*                              */
/********************************/



/********************************/
/*                              */
/*            Getters           */			
/*                              */
/********************************/


		
/********************************/
/*                              */
/*            Setters           */			
/*                              */
/********************************/
			

		
/********************************/
/*                              */
/*            Methodes          */			
/*                              */
/********************************/
		
template <class T> void Liste<T>::insere(const T &val)
{
	Cellule<T> *nouv = NULL;
	nouv = new Cellule<T>;
	
	if(nouv != NULL)
	{
		nouv->valeur = val;
		nouv->suivant = NULL;
		if(this->estVide())
		{
			this->setTete(nouv);
		}
		else
		{
			Cellule<T> *tmp = this->getTete();
			while(tmp->suivant != NULL)
			{
				tmp = tmp->suivant;
			}
			tmp->suivant = nouv;
		}
	}
}

template class Liste<int>;
#include "Output.h"
template class Liste<Output>;