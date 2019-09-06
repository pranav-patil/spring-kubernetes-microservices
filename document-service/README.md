Document Service
=============

Document service provides services to access reporting data and fetching the report documents.
It mainly uses [OpenFeign Clients](https://github.com/OpenFeign/feign) and [Netflix Hystrix](https://github.com/Netflix/Hystrix) circuit breakers to invoke [Data Service](/../data-service/README.md) and [Storage Service](/../storage-service/README.md).
   
### Running the Document Service

Optionally **spring.profiles.active** can be passed with value **production** which enables logback to send all logs to Elastic Stack instead of logging in the console by default.

    $ java -jar document-service/build/libs/document-service-0.0.1-SNAPSHOT.jar
		   -Dspring.profiles.active=production

### Running Document Service on Kubernetes

First setup [data-service](/../data-service/README.md) and [storage-service](/../storage-service/README.md) which is prerequisite for running document-service.
 
Then create a docker image for document-service, tagging it with name document-service, and push the image to [docker registry](/../readme/Docker_Registry.md). Login into docker registry using username `admin` and password `docker123`.

    $ sudo docker build --tag=document-service document-service
    $ sudo docker tag document-service docker.registry.com:5000/document-service:latest
    $ sudo docker login docker.registry.com:5000
    $ sudo docker push docker.registry.com:5000/document-service
    
If data-service and storage-service are running then deploy and launch document-service using below command.

    $ kubectl apply -f document-service.yaml
