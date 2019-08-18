# Docker Registry Installation

Kubernetes uses Deployment (YAML) to deploy kubernetes components such as pods and services. 
When kubernetes service is launched it creates pods within which docker containers are created. These docker containers are ephemeral 
and fetch the docker image of the service to spawn service instance. Kubernetes automatically setups the container instances, 
and uses the docker registry urls in deployment to download the docker images. Since kubernetes deployment mainly relies on docker images 
instead of dockerfile, docker registry becomes necessary to execute kubernetes services. Most of the publicly available docker registries have
size limitations or require payment, hence installing docker registry on local server is recommended. 
To setup a docker registry on a local machine we follow below commands. 

Generate http password using the username `admin` and password `docker123` to setup authentication for docker registry.

    $ mkdir auth
    $ cd auth/
    $ sudo docker run --entrypoint htpasswd registry:2 -Bbn admin docker123 > htpasswd

Open the system OpenSSL configuration to update in VI editor

    $ sudo vi /etc/ssl/openssl.cnf

Update the OpenSSL configuration with below update:    

    [ v3_req ]
    subjectAltName = @alternate_names
    
    [ alternate_names ]
    DNS.1   = docker.registry.com

We need OpenSSL to generate TLS certificates and private key. If not installed use below commands to install OpenSSL and SSL libraries.

    $ sudo apt-get install libssl-dev
    $ sudo apt-get install openssl

Generate Self Signed TLS Certificates with private key

    $ mkdir -p certs
    $ cd certs
    $ openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout registry-selfsigned.key -out registry-selfsigned.crt
    
Launch Docker registry instance with below configuration

    $ sudo docker run -d --restart=always --name registry 
                      -v `pwd`/certs:/certs 
                      -v `pwd`/auth:/auth 
                      -e "REGISTRY_AUTH=htpasswd" 
                      -e "REGISTRY_AUTH_HTPASSWD_REALM=Registry Realm" 
                      -e REGISTRY_AUTH_HTPASSWD_PATH=/auth/htpasswd 
                      -e REGISTRY_HTTP_ADDR=0.0.0.0:5000 
                      -e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/registry-selfsigned.crt 
                      -e REGISTRY_HTTP_TLS_KEY=/certs/registry-selfsigned.key 
                      -p 5000:5000 registry:2

Copy self signed CA certificate to `/usr/local/share/ca-certificates` of each node/client accessing the docker registry server. 
If `/usr/local/share/ca-certificates/registry-selfsigned.crt` certificate already exists for the client then use the first command to empty the file contents, otherwise ignore the first command.

    $ sudo cp /dev/null /usr/local/share/ca-certificates/registry-selfsigned.crt
    
Open file `registry-selfsigned.crt` using `vi certs/registry-selfsigned.crt` and copy the docker registry certificate.
Now paste the certificate in opened `/usr/local/share/ca-certificates/registry-selfsigned.crt` file from below command and save.
Execute update certificates command and restart docker service.
    
    $ sudo vi /usr/local/share/ca-certificates/registry-selfsigned.crt
    $ sudo update-ca-certificates
    $ sudo service docker restart

Update the hosts file of the machine with host name `docker.registry.com` as below.

    $ sudo vi /etc/hosts
    
    127.0.0.1 localhost docker.registry.com    

Configure an Insecure Registry for every node/client by adding below line to `/etc/docker/daemon.json` file.
If the file does not exists then use the touch command `touch /etc/docker/daemon.json` to create the daemon.json file.
NOTE: If the server does not have DNS name then IP address should be used instead of `docker.registry.com`.

    $ sudo vi /etc/docker/daemon.json

    { "insecure-registries":["docker.registry.com:5000"] }
    
    $ sudo systemctl daemon-reload
    $ sudo systemctl restart docker

[Create a Secret](https://kubernetes.io/docs/tasks/configure-pod-container/pull-image-private-registry/#registry-secret-existing-credentials) based on existing docker-registry credentials to authenticate with a container registry to pull a private image.

    $ sudo cat ~/.docker/config.json
    
    $ sudo kubectl create secret generic regcred --from-file=.dockerconfigjson=/home/ubuntu/.docker/config.json --type=kubernetes.io/dockerconfigjson
    
The secret can be stored in a YAML which can then be used for deployment.

    $ kubectl get secret regcred --output=yaml    

Create a docker image for each service, tag the image with service name, and push the image to docker registry from the client.
Use the username `admin` and password `docker123` used for generating http password earlier.

    $ sudo docker build --tag=data-service data-service
    $ sudo docker tag data-service docker.registry.com:5000/data-service:latest
    $ sudo docker login docker.registry.com:5000
    $ sudo docker push docker.registry.com:5000/data-service
