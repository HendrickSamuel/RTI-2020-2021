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
	Init		= 0,
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
	char imCamion[10];
	char idContainer[18];
	char societe[50];
	char destination[50];
};


/*structure inputDone*/
struct inputDone
{
	bool etat; // false = KO et true = OK
	float  poids;
};


/*structure outputReady*/
struct outputReady
{
	char  id[MAXSTRING];
	int  capacite;
	char dest[MAXSTRING];
};


/*structure outputOne*/
struct outputOne
{
	char idContainer[18];
};


/*structure outputDone*/
struct outputDone
{
	char  id[MAXSTRING];
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
    bool succes;
    char message[MAXSTRING];
	int x;
	int y;
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
