/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 08/10/2020             */
/***********************************************************/


#ifndef OUTPUT_H
#define OUTPUT_H

#include <iostream>
#include <string.h>


using namespace std;

class Output
{	
		//fonctions amies
		friend ostream& operator<<(ostream& s, const Output& o);

	private:
		char idContainer[18];
		int x;
		int y;

	public:
		//constructeurs
		Output(void);
		Output(char *idTmp, int xTmp, int yTmp);
		Output(const Output& o);
	
		//getters
		const char* getId() const;
		int getX() const;
		int getY() const;
		
		//setters
		void setId(const char *idTmp);
		void setX(int xTmp);
		void setY(int yTmp);
};

#endif //OUTPUT_H