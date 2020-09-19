#ifndef BASEEXEPTION_H
#define BASEEXEPTION_H

#include <stddef.h>
#include <string.h>

class BaseException
{
	protected:
		char* message;
	public:
		BaseException();
		BaseException(const char*);
		BaseException(const BaseException&);
		virtual ~BaseException();
			
		void setMessage(const char*);
		const char* getMessage(void) const;
};


#endif