/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include "BaseException.h"

/********************************/
/*                              */
/*         Constructeurs        */
/*                              */
/********************************/

BaseException::BaseException()
{
	message = NULL;
}

BaseException::BaseException(const char* chaineget)
{
	message = NULL;
	setMessage(chaineget);
}

BaseException::BaseException(const BaseException& old)
{
	message = NULL;
	setMessage(old.getMessage());
}


/********************************/
/*                              */
/*          Destructeurs        */
/*                              */
/********************************/

BaseException::~BaseException()
{
	if(message)
		delete message;
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

const char* BaseException::getMessage() const
{
	return message;
}


/********************************/
/*                              */
/*            Setters           */
/*                              */
/********************************/

void BaseException::setMessage(const char* chaineget)
{
	if(message)
	{
		delete message;
	}
	if(chaineget)
	{
		message = new char[strlen(chaineget)+1];
		strcpy(message,chaineget);
	}
	else
	{
	 message = NULL;
	}
} 


/********************************/
/*                              */
/*            Methodes          */
/*                              */
/********************************/