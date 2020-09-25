/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/
#include "Configurator.h"

/********************************/
/*                              */
/*            Methodes          */
/*                              */
/********************************/
char* Configurator::getProperty(const char* fileName, const char* property)
{
    char* token;
    char Tampon[800];
    char* valeur; 

    cout << "valeur a rechercher: [" << property << "]" << endl;

    ifstream fb;
    fb.open(fileName,std::ifstream::in);
	if(!fb.is_open())
		throw BaseException("le fichier n'a pas pu etre ouvert");
	
    while (fb.getline(Tampon,80))
    {
        token = strtok(Tampon,"=");
		if(token == NULL)
			throw BaseException("la colonne selectionnee est soit incomplete soit inexistante");

        cout << "token: [" << token << "]" << endl;

        if(strcmp(token, property) == 0)
        {
            token = strtok(NULL, "\n");
            cout << "trouvé: " << token << endl;
            fb.close();
            valeur = new char [strlen(token)];
            strcpy(valeur,token);
            return valeur;
        }
    }

    fb.close();
    return NULL;
}
