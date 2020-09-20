.SILENT:
LIBRAIRIE = ./librairies
SERVEURS = ./serveurs
CLIENT = ./clients

INCL = -I $(LIBRAIRIE)
GPP = g++ -m64 -Wall -lnsl -lsocket -lpthread $(INCL) -D CPP -D SUN #-D DEV -D DEVPLUS
LIBRAIRIE_O = Sockets.o BaseException.o Trace.o

SERVERCONTAINERS_OBJS = $(LIBRAIRIE_O)
CLIENTCONTAINERS_OBJS = $(LIBRAIRIE_O)

ALL: Serveur_Containers Application_Containers

Serveur_Containers: $(SERVERCONTAINERS_OBJS) $(SERVEURS)/Serveur_Containers.cpp
	echo creation du Serveur_Containers;
	$(GPP) $(SERVEURS)/Serveur_Containers.cpp -o ./Serveur_Containers $(SERVERCONTAINERS_OBJS);

Application_Containers: $(SERVERCONTAINERS_OBJS) $(CLIENT)/Application_Containers.cpp
	echo creation du Application_Containers;
	$(GPP) $(CLIENT)/Application_Containers.cpp -o ./Application_Containers $(CLIENTCONTAINERS_OBJS);

Sockets.o: $(LIBRAIRIE)/Sockets.cpp $(LIBRAIRIE)/Sockets.h
	echo creation de Socket.o;
	$(GPP) -c $(LIBRAIRIE)/Sockets.cpp -o Sockets.o;

Trace.o: $(LIBRAIRIE)/Trace.cpp $(LIBRAIRIE)/Trace.h
	echo creation de Trace.o;
	$(GPP) -c $(LIBRAIRIE)/Trace.cpp -o Trace.o;

BaseException.o: $(LIBRAIRIE)/BaseException.cpp $(LIBRAIRIE)/BaseException.h
	echo creation de BaseException.o;
	$(GPP) -c $(LIBRAIRIE)/BaseException.cpp -o BaseException.o;