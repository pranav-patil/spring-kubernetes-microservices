# Metal Load Balancer for Bare Metal Kubernetes

Load balancers are available on-demand on cloud environments and a single Kubernetes manifest suffices to provide a single point of contact to the [NGINX Ingress controller](https://kubernetes.github.io/ingress-nginx/deploy/baremetal/) to external clients and, indirectly, to any application running inside the cluster. 
Bare-metal environments lack this commodity, requiring a slightly different setup to offer the same kind of access to external consumers. MetalLB provides a network load-balancer implementation for Kubernetes clusters that do not run on a supported cloud provider, effectively allowing the usage of LoadBalancer Services within any cluster.
[MetalLB](https://metallb.universe.tf/) hooks into the Kubernetes cluster, and provides a network load-balancer implementation thus allowing to create Kubernetes services of type `LoadBalancer` in clusters that don't run on a cloud provider.
It has two features that work together to provide this service: address allocation, and external announcement. MetalLB is responsible for address allocation in bare metal cluster. 
Address pools are provided to MetalLB which is used for assigning and unassigning individual addresses as services come and go. Once MetalLB has assigned an external IP address to a service, it needs to make the network beyond the cluster aware that the IP `lives` in the cluster. MetalLB uses standard routing protocols to achieve this: ARP, NDP, or BGP. 
To install MetalLB, simply apply the manifest as below:

    $ kubectl apply -f https://raw.githubusercontent.com/google/metallb/v0.8.1/manifests/metallb.yaml

MetalLB remains idle until configured. This is accomplished by creating and deploying a configmap into the same namespace (metallb-system) as the deployment.
There is an example configmap in [manifests/example-config.yaml](https://raw.githubusercontent.com/google/metallb/v0.8.1/manifests/example-config.yaml), annotated with explanatory comments.
Some of the MetalLB configurations e.g. Layer 2 and BGP can be found [here](https://metallb.universe.tf/configuration/).
  
Some of the [example usages](https://metallb.universe.tf/usage/) is as below:

    apiVersion: v1
    kind: Service
    metadata:
      name: nginx
      annotations:
        metallb.universe.tf/address-pool: production-public-ips
    spec:
      ports:
      - port: 80
        targetPort: 80
      selector:
        app: nginx
      type: LoadBalancer

