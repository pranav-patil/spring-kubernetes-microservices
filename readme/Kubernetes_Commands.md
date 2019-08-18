# Kubernetes Commands

To deploy service

    $ sudo kubectl apply -f data-service.yaml
    
To Get the details of all the pods

    $ sudo kubectl get pods --all-namespaces -o wide
    
To Get additional information about the specified pod

    $ kubectl describe pod <pod-name>

To delete the kubernetes service and the service deployment

    $ kubectl delete services data-service
    $ kubectl delete deployment data-service

To check the logs within the pod use below command

    $ kubectl logs <pod-name> -p

Execute bash commands in the specified pod using kubectl. The double dash symbol "--" is used to separate the arguments to be passed to the command from the kubectl arguments.

    $ kubectl exec -it <pod-name> -- /bin/bash
 