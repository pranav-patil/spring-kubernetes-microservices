Storage Service
=============

Storage service provides services to store and fetch documents from [Minio cloud storage](https://min.io/).
Minio cloud storage can be setup using [SDK installations](https://min.io/download#/windows), [Docker](https://min.io/download#/linux) or [Kubernetes](https://min.io/download#/kubernetes). 
   
### Running the Storage Service

Optionally **spring.profiles.active** can be passed with value **production** which enables logback to send all logs to Elastic Stack instead of logging in the console by default.

    $ java -jar storage-service/build/libs/storage-service-0.0.1-SNAPSHOT.jar
		   -Dspring.profiles.active=production

### Running Storage Service on Kubernetes

Storage service requires minio-service to store and fetch docments from Minio Server. Once minio-service is running, launch storage-service using below command.

    $ kubectl apply -f storage-service.yaml
