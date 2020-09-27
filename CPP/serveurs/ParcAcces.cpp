#include "ParcAcces.h"

parcAcces::parcAcces(const char* file)
{
    this->fileName = file;
}

bool parcAcces::addRecord(struct fich_parc record)
{
    FILE* fileHandler;
    struct fich_parc tmpRecord;

    if(this->hasRecord(record))
    {
        return this->updateRemove(record);
    }

    fileHandler = fopen(this->fileName, "r+");
    if(fileHandler == NULL)
    {
        fileHandler = fopen(this->fileName, "w");
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

bool parcAcces::updateRemove(struct fich_parc record)
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