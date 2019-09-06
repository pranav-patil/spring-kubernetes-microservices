# Persistent Volumes and Minio Service

### NFS Server Installation on Ubuntu

[Update](https://vitux.com/install-nfs-server-and-client-on-ubuntu/) system's repository index with that of the Internet through the apt command as below:

    $ sudo apt-get update

[Install](https://linuxconfig.org/how-to-configure-a-nfs-file-server-on-ubuntu-18-04-bionic-beaver) the NFS Kernel Server using apt in the system.

    $ sudo apt install nfs-kernel-server

The directory to be shared with the client system is called an export directory. 
Creating an export directory by the name of `kubernetes` in the system's mnt(mount) directory.

    $ sudo mkdir -p /mnt/kubernetes

Remove all restrictive permissions to provide access to all the clients.

    $ sudo chown nobody:nogroup /mnt/kubernetes
    $ sudo chmod 777 /mnt/kubernetes

Provide the clients permission to access the host server machine using the exports file located in /etc folder. Open the `/etc/exports` file in an editor.

    $ sudo vi /etc/exports

Enable client access for each shared kubernetes folder on NFS server add the below line in `/etc/exports` file.
It is important that hardware IP address of the client be used instead of virtual or floating IP address.

    /mnt/kubernetes clientIP(rw,sync,no_subtree_check)

For entire subnet access specify subnet IP:

    /mnt/kubernetes subnetIP/24(rw,sync,no_subtree_check)
    
The `rw,sync,no_subtree_check` permissions defined means that the client(s) can perform:
* rw: read and write operations
* sync: write any change to the disc before applying it
* no_subtree_check: prevent subtree checking
    
Export the shared kubernetes directory

    $ sudo exportfs -a 
 
Restart the NFS service which will automatically reload and share the specified files/folders.

    $ sudo systemctl restart nfs-kernel-server

Open firewall for the clients for them to access the shared content.

    $ sudo ufw allow from [clientIP or clientSubnetIP] to any port nfs
 
Check status of the Ubuntu firewall to view the Action status as “Allow” for the client’s IP.

    $ sudo ufw status

Portmap service maps RPC requests to the correct services. RPC processes notify portmap when they start, revealing the port number they are monitoring and the RPC program numbers they expect to serve. The client system then contacts portmap service on the server with a particular RPC program number. portmap then redirects the client to the proper port number to communicate with its intended service.
Check portmap service is running, and if not running start the portmap service:

    $ sudo service portmap status
    $ sudo service portmap start
    $ rpcinfo -p

Check the list of shared directories available on NFS server

    $ sudo showmount -e <nfs-ip-address>
    

### NFS Client Installation on Ubuntu

The NFC Client is used to configure persistent volume using nfs client provisioner which requires that all the kubernetes nodes (including master) be setup NFS client. 
Update the system repository and install the latest NFS common client in the system. 

    $ sudo apt-get update
    $ sudo apt-get install nfs-common
    
To test whether the NFS client is able to mount the NFS shared directory, create a mount point for the NFS host's kubernetes folder
    
    $ sudo mkdir -p /mnt/kubernetes_client
    
Mount the kubernetes folder from the host to a mount folder on the client:

    $ sudo mount <nfs-ip-address>:/mnt/kubernetes /mnt/kubernetes_client


### Persistent Volume Setup
 
In order to enable PersistentVolumes (PVs) and PersistentVolumeClaims (PVCs) for bare metal installations we need to [set up a private storage class](https://joshrendek.com/2018/04/kubernetes-on-bare-metal/#nfs-sc) using the incubator of nfs-client. 
The [nfs-client](https://github.com/kubernetes-incubator/external-storage/tree/master/nfs-client) is an automatic provisioner which uses the existing configured NFS server to support dynamic provisioning of Kubernetes Persistent Volumes via Persistent Volume Claims. Persistent volumes are provisioned as ${namespace}-${pvcName}-${pvName}.

Clone the [nfs-client](https://github.com/kubernetes-incubator/external-storage/tree/v5.5.0) repository and go to the `nfs-client/docker` directory. Copy the YAML files from `deploy` and `deploy/objects` folders.
Update the `provisioner` in `class.yaml` and `PROVISIONER_NAME` in `deployment.yaml` to custom provisioner name. Also update the `deployment.yaml` file to corresponding NFS server IP address and NFS shared directory path.  

    $ cd external-storage/nfs-client/
    # vi deploy/deployment.yaml
    $ vi deploy/class.yaml
    
Create all the RBAC permissions, add them to the deployment, and then set a default storage class provider in kubernetes cluster. 
    
    $ kubectl apply -f deploy/deployment.yaml
    $ kubectl apply -f deploy/class.yaml
    $ kubectl create -f deploy/objects/serviceaccount.yaml
    $ kubectl create -f deploy/objects/clusterrole.yaml
    $ kubectl create -f deploy/objects/clusterrolebinding.yaml
    $ kubectl patch deployment nfs-client-provisioner -p '{"spec":{"template":{"spec":{"serviceAccount":"nfs-client-provisioner"}}}}'
    $ kubectl patch storageclass managed-nfs-storage -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"true"}}}'

For convenience, have copied all the YAML deployment files in minio-service, so that we can directly execute the below commands. Although before update the NFS_SERVER value with IP Address of NFS Server in `deployment.yaml`.

    $ kubectl apply -f nfs-provisioner-deployment.yaml
    $ kubectl apply -f nfs-provisioner-class.yaml
    $ kubectl create -f nfs-provisioner-clusterrole.yaml
    $ kubectl create -f nfs-provisioner-clusterrolebinding.yaml
    $ kubectl create -f nfs-provisioner-rbac.yaml
    $ kubectl patch deployment nfs-client-provisioner -p '{"spec":{"template":{"spec":{"serviceAccount":"nfs-client-provisioner"}}}}'
    $ kubectl patch storageclass managed-nfs-storage -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"true"}}}'

Check the default storage class provider setup for the cluster using below command: 

    $ kubectl get sc

To test whether the PersistentVolumeClaim (PVC) works using the nfs-client and able to create a PersistentVolume, execute below test PVC script. 

    $ kubectl apply -f test-pvc.yaml
    
Check the test PersistentVolume and PersistentVolumeClaim created using the get and describe commands:
    
    $ kubectl get pv,pvc
    $ kubectl describe pv,pvc

To debug various NFS issues and check the mount command outputs on the worker nodes, use the kubernetes events commands which gives the list of all events sorted by timestamp. 

    $ kubectl get events -w --sort-by=.metadata.creationTimestamp
    
 
### Minio Service Kubernetes Installation

Using the below commands a [Standalone MiniO server](https://github.com/minio/minio/tree/master/docs/orchestration/kubernetes#minio-standalone-server-deployment) is deployed on Kubernetes.
The `minio-pvc.yaml` creates a PersistentVolumeClaim which is used to request persistent storage for the MinIO instance tpo store objects.
Kubernetes looks out for PVs matching the PVC request in the cluster and binds it to the PVC automatically. 
Once the PersistentVolumeClaim is created, execute the `minio-deployment.yaml` to deploy mino-server onto a pod.
Finally a minio-service is created which can be access using the NodePort 32250. 

    $ kubectl create -f minio-pvc.yaml
    $ kubectl create -f minio-deployment.yaml
    $ kubectl create -f minio-service.yaml

Minio Service can be access using the below URL, were user id is `minio` and password is `minio123`.

    http://<node-ip-address>:32250/minio/

