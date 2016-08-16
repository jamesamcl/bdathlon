Problem 2 - Developing a conversion between BioPAX and SBOL

Note: As instructed I created a Maven project, and tried to install Paxtools 5.0.0, however the jars are not available on Maven - thus I used Paxtools 4.1.0


I created a set of classes in order to convert BioPAX (.owl/.rdf) models to SBOL (.sbol)
Bellow I ellaborate on the functionality of each class.

SBOLModel
	A class to represent an SBOL model, it contains the SBOLDocument and has utility functions in order to readily create objects

SBOLConverter
	An interface for developed the SBOL converter

SBOLBiopaxConverter
	A class which implements the SBOLConverter. It contains a function which takes a BioPAX model inputstream and converts it to an SBOLModel








Explained:
	The answer was not fully complete, as a full mapping could not be determined, however a large amount of conversion was achieved and an SBOL file was successfully written out.
	All PhysicalEntities and Interactions were done, however no Pathways could be achieved

