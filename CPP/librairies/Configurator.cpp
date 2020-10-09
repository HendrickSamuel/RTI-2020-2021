/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 09/10/2020             */
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

    ifstream fb;
    fb.open(fileName,std::ifstream::in);
	if(!fb.is_open())
		throw BaseException("le fichier n'a pas pu etre ouvert");
	
    while (fb.getline(Tampon,800))
    {
        token = strtok(Tampon,"=");
		if(token == NULL)
			throw BaseException("Le fichier comporte des lignes vides");

        if(strcmp(token, property) == 0)
        {
            token = strtok(NULL, "\n");
            fb.close();
            valeur = new char [strlen(token)+1];
            strcpy(valeur,token);
            return valeur;
        }
    }
    fb.close();
    return NULL;
}


bool Configurator::getLog(const char* filename, char* name, char* pwd)
{
	char *pNom, *pMdp;
	char Tampon[255];

	
	std::string Tampon1; //variable string pour le getline()
	
	ifstream fichier(filename, ios::in); //ouverture du fichier
		
	while(fichier >> Tampon1)  //avoir les ligne de valeur
	{
		strcpy(Tampon, Tampon1.c_str()); //Copie de string dans char
		pNom = strtok(Tampon, ";");

        if(strcmp(name, pNom) == 0)
        {
            pMdp = strtok (NULL, "\0");

            if(strcmp(pwd, pMdp) == 0)
            {
                fichier.close(); 
                return true;     
            }
        }
	}

	fichier.close(); 
    return false;   
}