/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 08/10/2020             */
/***********************************************************/

#include "Output.h"

/********************************/
/*                              */
/*         Constructeurs        */			
/*                              */
/********************************/

//Constructeur par défaut
Output::Output(void)
{

}

//Constructeur d'initialisation
Output::Output(char *idTmp, int xTmp, int yTmp)
{
	setId(idTmp);
	setX(xTmp);
	setY(yTmp);
}

//Constructeur de copie
Output::Output(const Output& o)
{
	setId(o.getId());
	setX(o.getX());
	setY(o.getY());
}

/********************************/
/*                              */
/*          Destructeurs        */			
/*                              */
/********************************/

/********************************/
/*                              */
/*           Operators          */			
/*                              */
/********************************/

//Opérateur << de cout			
ostream& operator<<(ostream& s, const Output& o)
{
	s<< " Id : " << o.getId() << " X :[" << o.getX() << "]   Y :[" << o.getY() << "]";
	return s;
}

/********************************/
/*                              */
/*            Getters           */			
/*                              */
/********************************/

const char* Output::getId() const
{
	return idContainer;
}

int Output::getX() const
{
	return x;
}

int Output::getY() const
{
	return y;
}

/********************************/
/*                              */
/*            Setters           */			
/*                              */
/********************************/

void Output::setId(const char *idTmp)
{
    strcpy(idContainer, idTmp);
}

void Output::setX(int xTmp)
{
	x = xTmp;
}

void Output::setY(int yTmp)
{
	y = yTmp;
}