# Linked Map Service: Application of open standards for the transparent use of geographic data and Linked Data

This project offers a service that allows linking different geospatial information sources within the semantic web.
This service allows the visualization of geographic information and linked data on a map, quering and editing tools. It includes a prototype web-client.

It has been developed as the final year project of student of the MS Degree in Computer Science Engineering at the Engineering and Architecture School of this University [EINA](http://eina.unizar.es/).

#Configuration
First you need to have installed Mapserver and PostgreSQL with PostGIS extension. In developing the project I have used the following versions:
	MapServer MS4W for Windows, 
	PostgreSQL 9.2 + PostGIS 2.1.8

####Configuration Web Map Service (WMS)
 1. Donwload [Mapserver] (http://mapserver.org/es/download.html) 
 2. Unzip
 3. In the configuration file "httpd.congf" verify that the default port is 80

####Configuration Database
1. Download [PostgreSQL] (https://www.postgresql.org/download/)
2. Run file and follow the steps
3. At the end of installation, select install extension, select PostGIS
4. Now you have pgAdmin installed. Accessed via pgAdmin and create a new database.
   You can load a back-up existing database (Download from [url](https://www.dropbox.com/s/jm88qcrnznjvu16/NGBE.backup?dl=0) you can find in Ngbe module) or you can create database from  scripts found in documentation (Appendix B) and use java classes in ngbe module.
5. Modidy file "application.properties" whit username and password you use during installation 

####Developer Mode
You can use this project to research, or add new features to it:

1. Load the project in IDE
2. The project use Sprin Boot and you can deploy on a tomcat embedded.
3. Run the TTLfromDB class to generate if there is no TDB
4. Run StartFuseki class for run Fuseki server
5. Run Application class and then put in the browser http://localhost:8080 to run the application

####Generate WAR
You can also generate a war file and put it in an Apache Tomcat Server:
1. Update project in the IDE
2. Export like .war

####
#License

The source code of this application is licensed under the GNU General Public License version 3.
#Credits
####Contributors

    Elena Oliván Salvador

Supervised by Francisco J Lopez-Pellicer
#Software libraries
#Acknowledgements

     
