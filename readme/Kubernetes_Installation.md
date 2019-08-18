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

### Master Node Setup

Now we initialize Master Node and with Kude admin. The --pod-network-cidr add-on option allows to specify the Container Network Interface (CNI) also called as Pod Network. 
There are various third party pod network interfaces available which can be selected using --pod-network-cidr option. 
For example to start a Calico CNI we specify 192.168.0.0/16 and to start a Flannel CNI we use 10.244.0.0/16. 
It is recommended that the master host have at least 2 core CPUs and 4GB of RAM. If set, the control plane will automatically allocate 
CIDRs (Classless Inter-Domain Routing or Subnet) for every node. A pod network add-on must be installed so that the pods can communicate with each other.

Kubeadm uses the network interface associated with the default gateway to advertise master node's IP address which it would be listening on. 
The --apiserver-advertise-address option allows to select a different network interface on master node machine. Specify '0.0.0.0' to use the address 
of the default network interface.

    $ sudo kubeadm init --pod-network-cidr=192.168.1.0/16 --apiserver-advertise-address=<master-node-ip-address>

Issue following commands as regular user before joining a node.

    $ mkdir -p $HOME/.kube
    $ sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
    $ sudo chown $(id -u):$(id -g) $HOME/.kube/config

Download the Calico networking manifest for the Kubernetes API datastore and apply the manifest using the following command.
It creates Pods based on Calico (Calico Pod Network) using specified release 3.6 calico.yaml file.

    $ wget "https://docs.projectcalico.org/v3.6/getting-started/kubernetes/installation/hosted/kubernetes-datastore/calico-networking/1.7/calico.yaml" --no-check-certificate
    $ kubectl apply -f calico.yaml

### Worker Node Setup

The kubeadm join command is executed on the worker nodes to allow them to join the cluster using the <worker-token> returned by 
the kubeadm init command on the master node.

    $ sudo kubeadm join <master-node-ip-address>:6443 --token <master-token> --discovery-token-ca-cert-hash <master-token-hash>
