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
# version 20120801                                                                                #
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

1. install MT4j and put the project into Eclipse
2. Install Maven 2 (2!)
3. install Context Model
4. install Jdom

INSTALLATION: =============================================

1. Do the "mvn eclipse:eclipse" on the Smart Home GUI

2. Add to build path:

2.1    External variables:

MT4j\mt4jLibs\core.jar
MT4j\mt4jLibs\jogl.jogl.jar
jdom\lib\jaxen.jar
jdom\build\jdom.jar
jdom\lib\saxpath.jar

2.2 Projects:

MT4j


*** DONE ***


###################################################################################################
3. HISTORY ########################################################################################

2012-06-27:
	-adding of this file to the project

	2012-07-07:
	-dynamic context model 3d model 2nd prototype
	
2012-08-01:
	-choose between a jfilechooser dialog or the mt4j file chooser dialog
	
2012-08-28:
	-fixed some minor bugs in the rendering of the walls (walls where outside the ground area)
	
2012-09-05:
	-added standalones for 32 & 64 bit machines
	-implemented simple system for file-based global variables for easy maintenance of the software
2012-09-07:
	-added dynamic change of config-files by giving them as command line arguments