#include "BaseException.h"

/*---------------------------------Constructeurs -----------------------------------*/

BaseException::BaseException()
{
	message = NULL;
}

BaseException::BaseException(const char* chaineget)
{
	message = NULL;
	setMessage(chaineget);
}
BaseException::BaseException(const BaseException& old)
{
	message = NULL;
	setMessage(old.getMessage());
}

BaseException::~BaseException()
{
	if(message)
		delete message;
}
/*---------------------------------Setteurs -----------------------------------*/

void BaseException::setMessage(const char* chaineget)
{
	if(message)
	{
		delete message;
	}
	if(chaineget)
	{
		message = new char[strlen(chaineget)+1];
		strcpy(message,chaineget);
	}
	else
	{
	 message = NULL;
	}
} 

/*---------------------------------Getteurs -----------------------------------*/

const char* BaseException::getMessage() const
{
	return message;
}