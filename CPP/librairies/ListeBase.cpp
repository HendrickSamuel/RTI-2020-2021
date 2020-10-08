/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 08/10/2020             */
/***********************************************************/

#include "ListeBase.h"

/********************************/
/*                              */
/*         Constructeurs        */			
/*                              */
/********************************/

//Constructeur par défaut
template <class T> ListeBase<T>::ListeBase(void)
{
	setTete(NULL);
}

//Constructeur de copie
template <class T> ListeBase<T>::ListeBase(const ListeBase& LB)
{
	if(LB.pTete != NULL)
	{
		struct Cellule<T> *suivCellule = NULL;
		struct Cellule<T> *tmpCellule = LB.pTete;
		pTete = new struct Cellule<T>;
		pTete->valeur = tmpCellule->valeur;
		suivCellule = pTete;
		tmpCellule = tmpCellule->suivant;
		while(tmpCellule != NULL)
		{
			suivCellule->suivant = new struct Cellule<T>;
			suivCellule = suivCellule->suivant;
			suivCellule->valeur = tmpCellule->valeur;
			tmpCellule = tmpCellule->suivant;
		}
		suivCellule->suivant = NULL;
	}
	else
		setTete(NULL);
}
		
/********************************/
/*                              */
/*          Destructeurs        */			
/*                              */
/********************************/

template <class T> ListeBase<T>::~ListeBase()
{
	if(!estVide())
	{
		struct Cellule<T> *tmpCellule = pTete->suivant;
		
		while(tmpCellule != NULL)
		{
			delete pTete;
			pTete = tmpCellule;
			tmpCellule = pTete->suivant;
		}
		setTete(NULL);
	}
}

/********************************/
/*                              */
/*           Operators          */			
/*                              */
/********************************/

template <class T>  const ListeBase<T>& ListeBase<T>::operator=(const ListeBase& LB)
{
	/*Pour vider la liste déjà existante s'il y a lieu*/
	if(!estVide())
	{
		struct Cellule<T> *tmpCellule = pTete->suivant;
		
		while(tmpCellule != NULL)
		{
			delete pTete;
			pTete = tmpCellule;
			tmpCellule = pTete->suivant;
		}
		setTete(NULL);
	}
	/*Copier la liste*/
	if(LB.pTete != NULL)
	{
		struct Cellule<T> *suivCellule = NULL;
		struct Cellule<T> *tmpCellule = LB.pTete;
		pTete = new struct Cellule<T>;
		pTete->valeur = tmpCellule->valeur;
		suivCellule = pTete;
		tmpCellule = tmpCellule->suivant;
		while(tmpCellule != NULL)
		{
			suivCellule->suivant = new struct Cellule<T>;
			suivCellule = suivCellule->suivant;
			suivCellule->valeur = tmpCellule->valeur;
			tmpCellule = tmpCellule->suivant;
		}
		suivCellule->suivant = NULL;
	}
	 
	return *this;
}

/********************************/
/*                              */
/*            Getters           */			
/*                              */
/********************************/

template <class T> int ListeBase<T>::getNombreElements() const
{
	int nbr=0;
		
	if(!estVide())
	{
		struct Cellule<T> *tmpCellule = getTete();
		nbr++;
		while(tmpCellule->suivant != NULL)
		{
			nbr++;
			tmpCellule = tmpCellule->suivant;
		}
	}
	return nbr;
}

template <class T> struct Cellule<T>* ListeBase<T>::getTete() const
{
	return pTete;
}
		
/********************************/
/*                              */
/*            Setters           */			
/*                              */
/********************************/
			
template <class T> void ListeBase<T>::setTete(struct Cellule<T> *Tete)
{
	pTete = Tete;
}
		
/********************************/
/*                              */
/*            Methodes          */			
/*                              */
/********************************/
		
template <class T> bool ListeBase<T>::estVide() const
{
	if(pTete == NULL)
		return true;
	else
		return false;
}

template <class T> void ListeBase<T>::Affiche() const
{
	if(estVide())
		cout << "La liste est vide" << endl;
	else
	{
		struct Cellule<T> *tmpCellule = pTete;
		cout << "[" << tmpCellule->valeur << "]" << endl;
		while(tmpCellule->suivant != NULL)
		{
			tmpCellule = tmpCellule->suivant;
			cout << "[" << tmpCellule->valeur << "]" << endl;
		}
	}
}


template class ListeBase<int>;
#include "Output.h"
template class ListeBase<Output>;