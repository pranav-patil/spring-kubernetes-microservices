# Docker Registry Installation

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


Create a Certificate Authority private key (important key) as below

    $ mkdir -p certs
    $ cd certs
    $ openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout registry-selfsigned.key -out registry-selfsigned.crt
    
Create a CA self-signed certificate

    $ openssl x509 -trustout -signkey ca.key -days 365 -req -in ca.csr -out ca.pem

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
    $ sudo vi /usr/local/share/ca-certificates/registry-selfsigned.crt
    $ sudo update-ca-certificates
    $ sudo service docker restart

Configure an Insecure Registry for every node/client by adding below line to `/etc/docker/daemon.json` file.   

    { "insecure-registries":["registry.example.lab:5000"] }
    
    $ systemctl restart docker 

Create a docker image for each service, tag the image with service name, and push the image to docker registry from the client.

    $ sudo docker build --tag=data-service data-service
    $ sudo docker tag data-service docker.registry.com:5000/data-service:latest
    $ sudo docker login docker.registry.com:5000
    $ sudo docker push docker.registry.com:5000/data-service

cat /etc/docker/daemon.json
cat > /etc/docker/daemon.json <<EOF
{
  "exec-opts": ["native.cgroupdriver=systemd"],
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "100m"
  },
  "storage-driver": "overlay2"
}

sudo touch /etc/docker/daemon.json
sudo vi /etc/docker/daemon.json

sudo mkdir -p /etc/systemd/system/docker.service.d
sudo systemctl daemon-reload
sudo systemctl restart docker

kubectl proxy --address="10.141.62.8" -p 8080 --accept-hosts='^*$' &

cd /usr/local/share/ca-certificates
openssl req -new -newkey rsa:1024 -nodes -out ca.csr -keyout ca.key
http -a hakase https://registry.hakase-labs.io/v2/_catalog

sudo docker run -d --restart=always --name registry -v `pwd`/certs:/certs -v `pwd`/auth:/auth -e "REGISTRY_AUTH=htpasswd" -e "REGISTRY_AUTH_HTPASSWD_REALM=Registry Realm" -e REGISTRY_AUTH_HTPASSWD_PATH=/auth/htpasswd -e REGISTRY_HTTP_ADDR=0.0.0.0:5000 -e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/registry-selfsigned.crt -e REGISTRY_HTTP_TLS_KEY=/certs/registry-selfsigned.key -p 5000:5000 registry:2
less ~/.bash_history
