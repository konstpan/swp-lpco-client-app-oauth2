#### Intro
Prototype client for PROTEUS published web services consumed by external organizations.  

#### Tech stack
A java desktop application built with maven thats uses REST client to authenticate to an OAuth2 authorization server and perform authorized requests to portal web services.  
WindowBuilder was used to create GUI design.  
ICEpdf engine used for viewing LPCO documents. 
For distribution an uber jar with dependencies is created.  
A workaround with an all-trusting http client was required to avoid issues with self-signed certificates and communication over https.

#### Run
mvn clean package  
java -jar target\client-app-0.0.1-SNAPSHOT-jar-with-dependencies.jar  
