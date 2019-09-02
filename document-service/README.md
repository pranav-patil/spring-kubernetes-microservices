Document Service
=============

Document service provides services to access reporting data and fetching the report documents.
It mainly uses [OpenFeign Clients](https://github.com/OpenFeign/feign) and [Netflix Hystrix](https://github.com/Netflix/Hystrix) circuit breakers to invoke [Data Service](/../data-service/README.md) and [Storage Service](/../storage-service/README.md).
   
### Running the Document Service

Optionally **spring.profiles.active** can be passed with value **production** which enables logback to send all logs to Elastic Stack instead of logging in the console by default.

    $ java -jar document-service/build/libs/document-service-0.0.1-SNAPSHOT.jar
		   -Dspring.profiles.active=production

### Running Document Service on Kubernetes

First setup data-service and storage-service which is prerequisite for running document-service. 
Once data-service and storage-service are running, launch document-service using below command.

    $ kubectl apply -f document-service.yaml
