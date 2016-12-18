###################################################################################################
# this readme file contains information about Smart Home project like the todos and stufn.        #
#                                                                                                 #
# *** CONTENTS: ***                                                                               #
# 1. TODO                                                                                         #
#    1.1 SOFTWARE                                                                                 #
#        1.1.1 Functionality                                                                      #
#    1.2 BUGS                                                                                     #
# 2. HOWTO INSTALL                                                                                #
# 3. HISTORY                                                                                      #
#                                                                                                 #
# version 20120611                                                                                #
# Contact: andreas.langenhagen@dai-labor.de                                                       #
###################################################################################################

###################################################################################################
1. TODO ###########################################################################################


1.1 SOFTWARE: =====================================================================================

1.1.1 Functionality =================================================

- Positionen automatisch auslesen (reinschreiben kannst du erst einmal weglassen)

- 3 Zustaende fuer das Rollo: <= 25% ganz klein (also oben), 25% > x < 75% Mittelstellung und >= 75% Rollo unten (evtl. ist die Prozentangabe im Kontextmodell auch genau anders herum, also dass 100% gleich oben/eingefahren heisst.)

-Textbreiten bei Buttons etc cutten


1.2 BUGS: =========================================================================================


###################################################################################################
2. HOWTO INSTALL ##################################################################################

VORBEREITENDE SCHRITTE: ===================================

1. MT4j installieren und Projekt in Eclipse rein gniddeln
2. Maven 2 installieren
3. Context Model installieren
4. Jdom installieren

INSTALLATION: =============================================

1. Dann Smart Home GUI mvn eclipse:eclipse-en

2. Im Build-Path hinzufügen:

2.1    Externe Variablen:

MT4j\mt4jLibs\core.jar
MT4j\mt4jLibs\jogl.jogl.jar
jdom\lib\jaxen.jar
jdom\build\jdom.jar
jdom\lib\saxpath.jar

2.2 Projekte:

MT4j


*** FERTIG ***


###################################################################################################
3. HISTORY ########################################################################################

2012-06-27:
	-adding of this file to the project