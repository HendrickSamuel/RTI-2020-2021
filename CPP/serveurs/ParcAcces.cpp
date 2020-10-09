/***********************************************************/
/*Auteurs : HENDRICK Samuel et DELAVAL Kevin               */
/*Groupe : 2302                                            */
/*Labo : R.T.I.                                            */
/*Date de la dernière mise à jour : 19/09/2020             */
/***********************************************************/

#include "ParcAcces.h"
/********************************/
/*                              */
/*         Constructeurs        */
/*                              */
/********************************/

parcAcces::parcAcces(const char* file)
{
    this->fileName = file;
}


/********************************/
/*                              */
/*            Methodes          */
/*                              */
/********************************/

void parcAcces::initFichPark()
{
    FILE* fileHandler;
    struct fich_parc record;

    fileHandler = fopen(this->fileName, "w");

    strcpy(record.id, "0000-00000-000000");
    record.x = 0;
    record.y = 0;
    record.flagemplacement = 0;
    strcpy(record.datereservation, "00/00/0000");
    strcpy(record.datearrivee, "00/00/0000");
    record.poids = 0;
    strcpy(record.destination, "inconnu");
    strcpy(record.moyenTransport, "inconnu");

    for(int x = 1 ; x <= 2 ; x++)
    {
        for(int y = 1 ; y <= 10 ; y++)
        {
            record.x = x;
            record.y = y;
            fwrite(&record, sizeof(struct fich_parc),1,fileHandler);
        }
    }

    fclose(fileHandler);;
}

void parcAcces::setBidon()
{
    struct fich_parc fp1;
    
    strcpy(fp1.id, "YABB-CHARL-A1B2C3");
    fp1.flagemplacement = 2;
    strcpy(fp1.datereservation, "00/00/0000");
    strcpy(fp1.datearrivee, "08/10/2020");
    fp1.poids = 250;
    strcpy(fp1.destination, "Madrid");
    strcpy(fp1.moyenTransport, "Bateau");
    this->addRecord(fp1);

    strcpy(fp1.id, "YABB-SAMUE-A1B2C3");
    fp1.flagemplacement = 1;
    strcpy(fp1.datereservation, "09/10/2020");
    strcpy(fp1.datearrivee, "00/00/0000");
    fp1.poids = 112;
    strcpy(fp1.destination, "Madrid");
    strcpy(fp1.moyenTransport, "Train");
    this->addRecord(fp1);

    strcpy(fp1.id, "YABB-KEVIN-A1B2C3");
    fp1.flagemplacement = 2;
    strcpy(fp1.datereservation, "06/10/2020");
    strcpy(fp1.datearrivee, "06/10/2020");
    fp1.poids = 12;
    strcpy(fp1.destination, "Madrid");
    strcpy(fp1.moyenTransport, "Bateau");
    this->addRecord(fp1);

    strcpy(fp1.id, "YABB-BENED-A1B2C3");
    fp1.flagemplacement = 2;
    strcpy(fp1.datereservation, "07/10/2020");
    strcpy(fp1.datearrivee, "07/10/2020");
    fp1.poids = 1232;
    strcpy(fp1.destination, "Paris");
    strcpy(fp1.moyenTransport, "Bateau");
    this->addRecord(fp1);

    strcpy(fp1.id, "YABB-LLOIC-A1B2C3");
    fp1.flagemplacement = 2;
    strcpy(fp1.datereservation, "07/10/2020");
    strcpy(fp1.datearrivee, "07/10/2020");
    fp1.poids = 1112;
    strcpy(fp1.destination, "Paris");
    strcpy(fp1.moyenTransport, "Train");
    this->addRecord(fp1);
}

bool parcAcces::addRecord(struct fich_parc record)
{
    FILE* fileHandler;
    struct fich_parc tmpRecord;

    if(this->hasRecord(record))
    {
        return this->updateRecord(record);
    }

    fileHandler = fopen(this->fileName, "r+");
    if(fileHandler == NULL)
    {
        initFichPark();
        fileHandler = fopen(this->fileName, "r+");
    }

    if(fileHandler != NULL)
    {
        while (fread(&tmpRecord, sizeof(struct fich_parc), 1, fileHandler))
        {
            if(tmpRecord.flagemplacement == 0)
            {
                fseek(fileHandler, -sizeof(struct fich_parc), SEEK_CUR);
                record.x = tmpRecord.x;
                record.y = tmpRecord.y;
                fwrite(&record, sizeof(struct fich_parc),1,fileHandler);
                fclose(fileHandler);
                return true;
            }
        }
        fclose(fileHandler);
        return false;
    }
    else
    {
        cout << "COULD NOT OPEN FILE " << endl;
        return false; //TODO: throw exception peut etre
    }
    
    
}

bool parcAcces::hasRecord(struct fich_parc record)
{
    FILE* fileHandler;
    struct fich_parc tmpRecord;

    fileHandler = fopen(this->fileName, "r");
    if(fileHandler != NULL)
    {
        while (fread(&tmpRecord, sizeof(struct fich_parc), 1, fileHandler))
        {
            if(strcmp(tmpRecord.id, record.id) == 0)
            {
                fclose(fileHandler);
                return true;
            }
        }
        fclose(fileHandler);
        return false;
    }
    else
    {
        return false; //TODO: throw exception peut etre
    }
}

bool parcAcces::getRecord(struct fich_parc *record, const char* id)
{
    FILE* fileHandler;
    struct fich_parc tmpRecord;

    fileHandler = fopen(this->fileName, "r");
    if(fileHandler != NULL)
    {
        while (fread(&tmpRecord, sizeof(struct fich_parc), 1, fileHandler))
        {
            if(strcmp(tmpRecord.id, id) == 0)
            {
                memcpy(record, &tmpRecord, sizeof(struct fich_parc));
                fclose(fileHandler);
                return true;
            }
        }
        fclose(fileHandler);
        return false;
    }
    else
    {
        return false; //TODO: throw exception peut etre
    }
}


bool parcAcces::removeRecord(struct fich_parc record)
{
    FILE* fileHandler;
    struct fich_parc tmpRecord;

    fileHandler = fopen(this->fileName, "r+");
    if(fileHandler != NULL)
    {
        while (fread(&tmpRecord, sizeof(struct fich_parc), 1, fileHandler))
        {
            if(strcmp(tmpRecord.id, record.id) == 0)
            {
                fseek(fileHandler, -sizeof(struct fich_parc), SEEK_CUR);
                tmpRecord.flagemplacement = 0;
                fwrite(&tmpRecord, sizeof(struct fich_parc),1,fileHandler);
                fclose(fileHandler);
                return true;
            }
        }
        fclose(fileHandler);
        return false;
    }
    else
    {
        return false; //TODO: throw exception peut etre
    }
}

bool parcAcces::updateRecord(struct fich_parc record)
{
    FILE* fileHandler;
    struct fich_parc tmpRecord;

    fileHandler = fopen(this->fileName, "r+");
    if(fileHandler != NULL)
    {
        while (fread(&tmpRecord, sizeof(struct fich_parc), 1, fileHandler))
        {
            if(tmpRecord.x == record.x && tmpRecord.y == record.y)
            {
                fseek(fileHandler, -sizeof(struct fich_parc), SEEK_CUR);
                fwrite(&record, sizeof(struct fich_parc),1,fileHandler);
                fclose(fileHandler);
                return true;
            }
        }
        fclose(fileHandler);
        return false;
    }
    else
    {
        return false; //TODO: throw exception peut etre
    }
}

bool parcAcces::searchPlace(struct fich_parc *record)
{
    FILE* fileHandler;
    struct fich_parc tmpRecord;

    fileHandler = fopen(this->fileName, "r");
    if(fileHandler == NULL)
    {
        initFichPark();
        fileHandler = fopen(this->fileName, "r");
    }

    if(fileHandler != NULL)
    {
        while (fread(&tmpRecord, sizeof(struct fich_parc), 1, fileHandler))
        {
            if(tmpRecord.flagemplacement == 0)
            {
                memcpy(record, &tmpRecord, sizeof(struct fich_parc));
                fclose(fileHandler);
                return true;
            }
        }
        fclose(fileHandler);
        return false;
    }
    else
    {
        return false; //TODO: throw exception peut etre
    }
}

char* parcAcces::searchDestination(const char *destination, int *nombre)
{

    int nb = 0;
    int compteur = 0;
    size_t size = 0;
    char buffer[100];
    char* chaineRetour = NULL;
    FILE* fileHandler;
    struct fich_parc tmpRecord;

    fileHandler = fopen(this->fileName, "r+");
    if(fileHandler != NULL)
    {
        while (fread(&tmpRecord, sizeof(struct fich_parc), 1, fileHandler))
        {
            if(tmpRecord.flagemplacement == 2 && strcmp(tmpRecord.destination, destination) == 0)
            {
                sprintf(buffer, "%s@%d@%d/", tmpRecord.id,tmpRecord.x,tmpRecord.y);
                if(chaineRetour == NULL)
                    size = 0;
                else
                    size = strlen(chaineRetour); 

                chaineRetour = (char*)realloc(chaineRetour, strlen(buffer)+size+1);
                strcat(chaineRetour, buffer); 
                nb++;
            }
        }
        fclose(fileHandler);

        if(nb > 0)  //Si il y a des container pour cette destination
        {
            chaineRetour[strlen(chaineRetour)-1] = '\0';
            *nombre = nb;
            return chaineRetour;
        }
        else        //S'il n'y en a pas
        {
             return NULL;
        }
        
    }
    else
    {
        return NULL; //TODO: throw exception peut etre
    }
}

void parcAcces::debugFichPark()
{
    FILE* fileHandler;
    struct fich_parc tmpRecord;

    fileHandler = fopen(this->fileName, "r");

    if(fileHandler != NULL)
    {
        cout << " -------- DEBUT --------" << endl;
        while (fread(&tmpRecord, sizeof(struct fich_parc), 1, fileHandler))
        {
            cout << " ++ EMPLACEMENT ++ " << endl;
            cout << "id: " << tmpRecord.id << endl
            << "x: " << tmpRecord.x << " || y: " << tmpRecord.y << endl
            << "flagemplacement: " << tmpRecord.flagemplacement << endl
            << "date R: " << tmpRecord.datereservation << " || date A: " << tmpRecord.datearrivee << endl
            << "poids: " << tmpRecord.poids << endl
            << "destination: " << tmpRecord.destination << " || moyen de transport: " << tmpRecord.moyenTransport << endl << endl;
        }
        cout << " -------- FIN --------" << endl;
        fclose(fileHandler);
    }
    else
    {
        cout << "-- Could not open " << this->fileName <<" -- " << endl;
    }
}