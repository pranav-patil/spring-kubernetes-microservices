# Kubernetes Commands

To deploy service (resources) by directly updating the current live resources.

    $ sudo kubectl apply -f data-service.yaml
    
To replace the service (resources) by first deleting then creating from the provided deployment file.

    $ kubectl replace -f data-service.yaml
    
To Get the details of all the pods

    $ sudo kubectl get pods --all-namespaces -o wide
    
To [forcefully delete](https://kubernetes.io/docs/tasks/run-application/force-delete-stateful-set-pod/) a StatefulSet pod without waiting for confirmation from the kubelet that the Pod has been terminated 

    $ kubectl delete pods <pod-name> --grace-period=0 --force    
    
If the pod is still stuck on Unknown state use below command to remove pod from the cluster

    $ kubectl patch pod <pod-name> -p '{"metadata":{"finalizers":null}}'   
    
To Get additional information about the specified pod

    $ kubectl describe pod <pod-name>

To delete the kubernetes service and the service deployment

    $ kubectl delete services data-service
    $ kubectl delete deployment data-service

To check the logs within the pod use below command. The p (previous) option prints the logs for the previous instance of the container in a pod if it exists.

    $ kubectl logs <pod-name> -p

The --follow flag streams logs from the specified resource, allowing to get live tail logs from the terminal.

    $ kubectl logs --follow <pod-name>

Execute bash commands in the specified pod using kubectl. The double dash symbol "--" is used to separate the arguments to be passed to the command from the kubectl arguments.

    $ kubectl exec -it <pod-name> -- /bin/bash
 
Get list of all deployments, replicasets, pods and services.

    $ kubectl get all

Get the list of kubernetes services running.

    $ kubectl get service

Get the list of endpoints, which is the list of addresses (ip and port) of endpoints that implement a Service.
They are automatically created when a Service is created, and configured with the pods matching the selector of the Service. 

    $ kubectl get -n kube-system endpoints

To list all pods and watch the container getting created:

    $ kubectl get pods --watch

Deploy container into kubernetes cluster exposing just the port 9200

    $ kubectl run elasticsearch --image=docker.elastic.co/elasticsearch/elasticsearch:6.2.1 --env="discovery.type=single-node" --port=9200

Display addresses of the master and services

    $ kubectl cluster-info

Dump current cluster state to stdout

    $ kubectl cluster-info dump
