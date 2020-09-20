/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#ifndef BASEEXEPTION_H
#define BASEEXEPTION_H

#include <stddef.h>
#include <string.h>

class BaseException
{
	protected:
		char* message;

	public:
	    //constructeurs
		BaseException();
		BaseException(const char*);
		BaseException(const BaseException&);

		//destructeurs
		virtual ~BaseException();

        //operators

        //getters
		const char* getMessage(void) const;

        //setters
		void setMessage(const char*);

        //méthodes

};


#endif //BASEEXEPTION_H