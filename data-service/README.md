Data Service
=============

Data service provides various services to save and fetch data from [MongoDB](https://www.mongodb.com/) database.
[Spring WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) framework is used for reactive programming in order to stream the result records from MongoDB.

### Installation and Running of MongoDB

* Download latest [Windows MongoDB release](https://www.mongodb.org/dl/win32/x86_64-2008plus-ssl) and extract the zip file.
* Create directories **data** and **logs** in MONGODB_HOME directory, were MONGODB_HOME is the path to the unzipped mongodb directory.
* Create file **mongo.log** in MONGODB_HOME/logs directory.
* Create **mongod.cfg** file using [MongoDB configuration options](https://docs.mongodb.com/v3.2/reference/configuration-options/) in MONGODB_HOME/bin directory. Alternatively copy **mongod.cfg** file from spring-microservices/data-service/config and add to MONGODB_HOME/bin directory.
* Find & edit the token **@logs@** with path "MONGODB_HOME\logs" and **@data@** with path "MONGODB_HOME\data".
* Go to MONGODB_HOME\bin directory and execute the command "mongod --config mongod.cfg" to run mongodb.
* MongoDB runs on default port 27017.

All the above steps can be executed directly by running below runMongodb gradle task for windows.

    $ gradle runMongodb


### Running the Data Service

Optionally **spring.profiles.active** can be passed with value **production** which enables logback to send all logs to Elastic Stack instead of logging in the console by default.

    $ java -jar data-service/build/libs/data-service-0.0.1-SNAPSHOT.jar
		   -Dspring.profiles.active=production

### Running Data Service on Kubernetes

First setup the mongodb-service which is required for running the data-service.

    $ kubectl apply -f mongodb-service.yaml

Launch the data-service using below command.

    $ kubectl apply -f minio-service.yaml
    
### Notes

* [Install MongoDB Shell](/../readme/Mongo_Shell.md) to view the collections and records within MongoDB database. 
* Data service uses [MapStruct](http://mapstruct.org/) for mapping between domain object to DTO object. MapStruct requires [mapstruct-processor](https://github.com/mapstruct/mapstruct) to be configured in gradle to generate the corresponding Mapper implementation for defined MapStruct interface. Hence it is highly recommended to **run gradle build before running data-service** to avoid Spring NoSuchBeanDefinitionException for MapStruct autowirings.     
