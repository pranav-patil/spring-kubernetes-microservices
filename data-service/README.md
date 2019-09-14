Data Service
=============

Data service provides various services to save and fetch data from [Apache Cassandra](http://cassandra.apache.org/) database.
[Spring WebFlux](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) framework is used for reactive programming in order to stream the result records from Cassandra database.

### Installation and Running of Apache Cassandra

* Download latest [Apache Cassandra release](http://cassandra.apache.org/download/) or [archive release](http://archive.apache.org/dist/cassandra/) and extract the .gz or .tar file.
* Find `conf/cassandra.yaml` file in CASSANDRA_HOME directory, were CASSANDRA_HOME is the path to the unzipped apache-cassandra directory. 
* Replace all the paths starting with `/var/lib/cassandra/` with `../var/lib/cassandra/`. This should update the properties `data_file_directories`, `commitlog_directory` and `saved_caches_directory`.
* Go to `CASSANDRA_HOME\bin` directory and execute the command `cassandra.bat` to run cassandra.
* Cassandra runs on default port 9042.
* To enabled Authentication for Cassandra database, open `conf/cassandra.yaml` configuration file and update the below properties with these values. 
    * **authenticator: PasswordAuthenticator**
    * **authorizer: CassandraAuthorizer**

All the above steps can be executed directly by running below runCassandra gradle task for windows.

    $ gradle runCassandra

To run cassandra [as a windows service](https://dzone.com/articles/running-cassandra-as-a-windows-service)
* Download [Apache commons daemon](http://archive.apache.org/dist/commons/daemon/binaries/windows/) and extract to any given location.
* Create a directory called daemon inside the `CASSANDRA_HOME\bin` directory.
* Copy the `prunsrv.exe` from Apache commons daemon extracted directory, based on architecture (32bit/64bit) to the `CASSANDRA_HOME\bin\daemon` directory created earlier.
* Go to `CASSANDRA_HOME\bin` directory and execute the command `cassandra.bat install` to install cassandra as a windows service.

### Cassandra Client

* To connect with cassandra database and create new keyspace (database) we use [CSQL](http://cassandra.apache.org/doc/latest/tools/cqlsh.html) (Cassandra Query Language) Shell.
* The cqlsh shell is compatible with Python 2.7 and requires it to be installed in the system to execute.
* In order to setup python and cqlsh shell without admin privileges, download [Standalone Python 2.7.9](http://www.orbitals.com/programs/pyexe.html) for windows and extract it to C drive.
* Then edit `CASSANDRA_HOME\bin\cqlsh.bat` file to add below set path command.


    setlocal
    SET PATH=%PATH%;c:\python 2.7.6

* Go to `CASSANDRA_HOME\bin` directory and execute the command `cqlsh.bat -u cassandra -p cassandra` to connect to cassandra database.
* Create simple keyspace using the command: `CREATE KEYSPACE testdb WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};`
* To use the `testdb` keyspace use the command: `USE testdb;`.

[RazorSQL](https://razorsql.com/docs/cassandra_database_browser.html) is Cassandra Database Browser which allows to view Keyspaces, Tables and Views.


### Running the Data Service

Optionally **spring.profiles.active** can be passed with value **production** which enables logback to send all logs to Elastic Stack instead of logging in the console by default.

    $ java -jar data-service/build/libs/data-service-0.0.1-SNAPSHOT.jar
		   -Dspring.profiles.active=production

### Running Data Service on Kubernetes

First setup the cassandra-service which is required for running the data-service.

    $ kubectl apply -f cassandra-service.yaml

Then create a docker image for data-service, tagging it with name data-service, and push the image to [docker registry](/../readme/Docker_Registry.md). Login into docker registry using username `admin` and password `docker123`.

    $ sudo docker build --tag=data-service data-service
    $ sudo docker tag data-service docker.registry.com:5000/data-service:latest
    $ sudo docker login docker.registry.com:5000
    $ sudo docker push docker.registry.com:5000/data-service
    
Finally deploy and create the data-service using below command.

    $ kubectl apply -f data-service.yaml
    
### Notes

* Data service uses [MapStruct](http://mapstruct.org/) for mapping between domain object to DTO object. MapStruct requires [mapstruct-processor](https://github.com/mapstruct/mapstruct) to be configured in gradle to generate the corresponding Mapper implementation for defined MapStruct interface. Hence it is highly recommended to **run gradle build before running data-service** to avoid Spring NoSuchBeanDefinitionException for MapStruct autowirings.     
