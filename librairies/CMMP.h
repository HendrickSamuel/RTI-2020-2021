#ifndef CMMP_H
#define CMMP_H

#define LOGIN		1
#define INPUTTRUCK	2
#define INPUTDONE	3
#define OUTPUTREADY 4
#define OUTPUTONE	5
#define OUTPUTDONE	6
#define LOGOUT		7

#define MAXSTRING 255

/*enum pour le type de requête*/
enum typeRequete
{
	Login		= 1,
	InputTruck	= 2,
	InputDone	= 3,
	OutputReady	= 4,
	OutputOne	= 5,
	OutputDone	= 6,
	Logout		= 7
};


/*structure login*/
struct login
{
    char nom[MAXSTRING];
    char pwd[MAXSTRING];
};


/*structure inputTruck*/
struct inputTruck
{
	int imCamion;
	int idContainer;
};


/*structure inputDone*/
struct inputDone
{
	bool etat; // false = KO et true = OK
	int  poids;
};


/*structure outputReady*/
struct outputReady
{
	int  id;
	int  capacite;
	char dest[MAXSTRING];
};


/*structure outputOne*/
struct outputOne
{
	int idContainer;
};


/*structure outputDone*/
struct outputDone
{
	int id;
	int	nbContainers;
};


/*structure logout*/
struct logout
{
    char nom[MAXSTRING];
    char pwd[MAXSTRING];
};


/*structure reponse*/
struct reponse
{
	enum typeRequete type;
    bool succes;
    char message[MAXSTRING];
};


/*structure protocole avec un enum et une union*/
struct protocole
{
    enum typeRequete type;
    union
    {
    	struct login		login;
    	struct inputTruck	inputTruck;
    	struct inputDone	inputDone;
    	struct outputReady	outputReady;
    	struct outputOne	outputOne;
    	struct outputDone	outputDone;
    	struct logout		logout;
		struct reponse		reponse;
    }donnees;
};

#endif //CMMP_H
