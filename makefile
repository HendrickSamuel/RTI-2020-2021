.SILENT:

PROGRAM = Application_Containers Serveur_Containers

LIBRAIRIE = ./librairies
SERVEURS = ./serveurs
CLIENT = ./clients

INCL = -I $(LIBRAIRIE)/ -I $(SERVEURS)/
GPP = g++ -m64 -Wall -lnsl -lsocket -lpthread $(INCL) -D CPP -D SUN #-D DEV -D DEVPLUS
LIBRAIRIE_O = Sockets.o SocketsServeur.o SocketsClient.o BaseException.o Trace.o Configurator.o

SERVERCONTAINERS_OBJS = $(LIBRAIRIE_O) ./ParcAcces.o
CLIENTCONTAINERS_OBJS = $(LIBRAIRIE_O)

all: $(PROGRAM)

Serveur_Containers: $(SERVERCONTAINERS_OBJS) $(SERVEURS)/Serveur_Containers.cpp ParcAcces.o
	echo creation du Serveur_Containers
	$(GPP) $(SERVEURS)/Serveur_Containers.cpp -o ./Serveur_Containers $(SERVERCONTAINERS_OBJS) 

Application_Containers: $(CLIENTCONTAINERS_OBJS) $(CLIENT)/Application_Containers.cpp
	echo creation du Application_Containers
	$(GPP) $(CLIENT)/Application_Containers.cpp -o ./Application_Containers $(CLIENTCONTAINERS_OBJS)

Sockets.o: $(LIBRAIRIE)/Sockets.cpp $(LIBRAIRIE)/Sockets.h
	echo creation de Socket.o
	$(GPP) -c $(LIBRAIRIE)/Sockets.cpp -o Sockets.o

SocketsServeur.o: $(LIBRAIRIE)/SocketsServeur.cpp $(LIBRAIRIE)/SocketsServeur.h
	echo creation de SocketsServeur.o
	$(GPP) -c $(LIBRAIRIE)/SocketsServeur.cpp -o SocketsServeur.o

SocketsClient.o: $(LIBRAIRIE)/SocketsClient.cpp $(LIBRAIRIE)/SocketsClient.h
	echo creation de SocketsClient.o
	$(GPP) -c $(LIBRAIRIE)/SocketsClient.cpp -o SocketsClient.o

Trace.o: $(LIBRAIRIE)/Trace.cpp $(LIBRAIRIE)/Trace.h
	echo creation de Trace.o
	$(GPP) -c $(LIBRAIRIE)/Trace.cpp -o Trace.o

BaseException.o: $(LIBRAIRIE)/BaseException.cpp $(LIBRAIRIE)/BaseException.h
	echo creation de BaseException.o
	$(GPP) -c $(LIBRAIRIE)/BaseException.cpp -o BaseException.o

Configurator.o: $(LIBRAIRIE)/Configurator.cpp $(LIBRAIRIE)/Configurator.h
	echo creation de Configurator.o
	$(GPP) -c $(LIBRAIRIE)/Configurator.cpp -o Configurator.o

ParcAcces.o: $(SERVEURS)/ParcAcces.cpp $(SERVEURS)/ParcAcces.h
	echo creation de ParcAcces.o
	$(GPP) -c $(SERVEURS)/ParcAcces.cpp -o ParcAcces.o
