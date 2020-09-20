#ifndef CMMP_H
#define CMMP_H

#define LOGIN 1
#define INPUTTRUCK 2
#define INPUTDONE 3
#define OUTPUTREADY 4
#define OUTPUTONE 5
#define OUTPUTDONE 6
#define LOGOUT 7

#define MAXSTRING 255

struct protocole{
    int commande;
    char nom[MAXSTRING];
    char pwd[MAXSTRING];
};

#endif