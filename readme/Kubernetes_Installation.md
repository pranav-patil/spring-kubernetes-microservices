# Kubernetes Installation

Install apt-transport-https to enable HTTPS protocol for package managers.

    $ sudo apt-get update && sudo apt-get install -y apt-transport-https

Add docker signing key and repository URL

    $ curl -s https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    $ sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"

Install docker on every node

    $ sudo apt update && sudo apt install -qy docker-ce

Start and enable the Docker service

    $ sudo systemctl start docker
    $ sudo systemctl enable docker

Install Kubernetes involves installing kubeadm which bootstraps a Kubernetes cluster,  kubelet which configures containers to run on a host and kubectl which deploys and manages apps on Kubernetes.
Add the Kubernetes in signing key

    $ sudo curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add 

Create the file /etc/apt/sources.list.d/kubernetes.list and add kubernetes repository URL as below.

    $ sudo touch /etc/apt/sources.list.d/kubernetes.list
    $ sudo vi /etc/apt/sources.list.d/kubernetes.list

Add "deb http://apt.kubernetes.io/ kubernetes-xenial main" to vi and save with "!wq".

    $ sudo apt-get update
    $ sudo apt-get install -y kubelet kubeadm kubectl kubernetes-cni

### Notes

* Data service uses [MapStruct](http://mapstruct.org/) for mapping between domain object to DTO object. MapStruct requires [mapstruct-processor](https://github.com/mapstruct/mapstruct) to be configured in gradle to generate the 

