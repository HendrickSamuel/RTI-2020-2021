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

    record.id = 0;
    record.x = 0;
    record.y = 0;
    record.flagemplacement = 0;
    strcpy(record.datereservation, "00/00/0000");
    strcpy(record.datearrivee, "00/00/0000");
    record.poids = 0;
    strcpy(record.destination, "inconnu");
    strcpy(record.moyenTransport, "inconnu");

    for(int x = 0 ; x < 2 ; x++)
    {
        for(int y = 0 ; y < 10 ; y++)
        {
            record.x = x;
            record.y = y;
            fwrite(&record, sizeof(struct fich_parc),1,fileHandler);
        }
    }

    fclose(fileHandler);;
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
        fileHandler = fopen(this->fileName, "r");
    }

    if(fileHandler != NULL)
    {
        while (fread(&tmpRecord, sizeof(struct fich_parc), 1, fileHandler))
        {
            if(tmpRecord.id == -1)
            {
                fseek(fileHandler, -sizeof(struct fich_parc), SEEK_CUR);
                fwrite(&record, sizeof(struct fich_parc),1,fileHandler);
                fclose(fileHandler);
                return true;
            }
        }
        fwrite(&record, sizeof(struct fich_parc),1,fileHandler);
        fclose(fileHandler);
        return true;
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
            if(tmpRecord.id == record.id)
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

bool parcAcces::getRecord(struct fich_parc *record, int id)
{
    FILE* fileHandler;
    struct fich_parc tmpRecord;

    fileHandler = fopen(this->fileName, "r");
    if(fileHandler != NULL)
    {
        while (fread(&tmpRecord, sizeof(struct fich_parc), 1, fileHandler))
        {
            if(tmpRecord.id == id)
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
            if(tmpRecord.id == record.id)
            {
                fseek(fileHandler, -sizeof(struct fich_parc), SEEK_CUR);
                record.id = -1;
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

list<fich_parc>* parcAcces::searchDestination(char *destination)
{
    list<fich_parc> liste;

    FILE* fileHandler;
    struct fich_parc tmpRecord;

    fileHandler = fopen(this->fileName, "r+");
    if(fileHandler != NULL)
    {
      /*  while (fread(&tmpRecord, sizeof(struct fich_parc), 1, fileHandler))
        {
            if(true)
            {
                fseek(fileHandler, -sizeof(struct fich_parc), SEEK_CUR);
                fwrite(&record, sizeof(struct fich_parc),1,fileHandler);
                fclose(fileHandler);

            }
        }*/
        fclose(fileHandler);
        return &liste;
    }
    else
    {
        return NULL; //TODO: throw exception peut etre
    }
    return &liste;
}