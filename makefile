.SILENT:
LIBRAIRIE = ./librairies
SERVEURS = ./serveurs

INCL = -I $(LIBRAIRIE)
GPP = g++ -m64 -Wall -lnsl -lsocket $(INCL) #-D DEV -D DEVPLUS
LIBRAIRIE_O = Sockets.o

SERVERCONTAINERS_OBJS = $(LIBRAIRIE_O)

ALL: Serveur_Containers

Serveur_Containers: $(SERVERCONTAINERS_OBJS) $(SERVEURS)/Serveur_Containers.cpp
	echo creation du Serveur_Containers;
	$(GPP) $(SERVEURS)/Serveur_Containers.cpp -o ./Serveur_Containers $(SERVERCONTAINERS_OBJS);
	
Sockets.o:	$(LIBRAIRIE)/Sockets.cpp $(LIBRAIRIE)/Sockets.h
	echo creation de Socket.o;
	$(GPP) -c $(LIBRAIRIE)/Sockets.cpp -o $(LIBRAIRIE)/Sockets.o;
