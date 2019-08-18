Discovery Service (Netflix Eureka)
=============

Discovery discovery allows services to find and communicate with other services regardless of their IP address. 
[Netflix Eureka](http://cloud.spring.io/spring-cloud-netflix/single/spring-cloud-netflix.html#_service_discovery_eureka_clients) Server is a service registry while Eureka Client is a Discovery client. 
It provides a REST API for managing service instance registration and for querying available instances.

NOTE: The discovery service is used **ONLY FOR LOCAL TESTING**. For production Spring Cloud Kubernetes enables service discovery.    
   
### Running the Discovery Service (For Local Testing Only)

Optionally **spring.profiles.active** can be passed with value **production** which enables logback to send all logs to Elastic Stack instead of logging in the console by default.

    $ java -jar discovery-service/build/libs/discovery-service-0.0.1-SNAPSHOT.jar
		   -Dspring.profiles.active=production
