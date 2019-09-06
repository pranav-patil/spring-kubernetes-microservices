Storage Service
=============

Storage service provides services to store and fetch documents from [Minio cloud storage](https://min.io/).
Minio cloud storage can be setup using [SDK installations](https://min.io/download#/windows), [Docker](https://min.io/download#/linux) or [Kubernetes](https://min.io/download#/kubernetes). 
   
### Running the Storage Service

Optionally **spring.profiles.active** can be passed with value **production** which enables logback to send all logs to Elastic Stack instead of logging in the console by default.

    $ java -jar storage-service/build/libs/storage-service-0.0.1-SNAPSHOT.jar
		   -Dspring.profiles.active=production

### Running Storage Service on Kubernetes

Storage service requires [minio-service](/../minio-service/README.md) to store and fetch documents from Minio Server. 

Create a docker image for storage-service, tagging it with name storage-service, and push the image to [docker registry](/../readme/Docker_Registry.md). Login into docker registry using username `admin` and password `docker123`.

    $ sudo docker build --tag=storage-service storage-service
    $ sudo docker tag storage-service docker.registry.com:5000/storage-service:latest
    $ sudo docker login docker.registry.com:5000
    $ sudo docker push docker.registry.com:5000/storage-service
    
If minio-service is running then deploy and create the storage-service using below command.

    $ kubectl apply -f storage-service.yaml
