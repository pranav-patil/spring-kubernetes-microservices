# NGINX Ingress Controller

[Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress/) is a component that routes the  HTTP/HTTPS traffic from outside the cluster to the services and Pods inside the cluster.
Ingress works as a reverse proxy or a load balancer were all external traffic is routed to the Ingress and then is routed to the other components.
Traffic routing is controlled by rules defined on the Ingress resource. Ingress is configured to enable externally-reachable URLs for services, load balance traffic, terminate SSL/TLS, and offer name based virtual hosting.
An Ingress controller is responsible for fulfilling the Ingress, usually with a load balancer.
Ingress frequently uses annotations such as [rewrite-target annotation](https://github.com/kubernetes/ingress-nginx/blob/master/docs/examples/rewrite/README.md) to configure options depending on the Ingress controller.
The Ingress spec contains a list of rules matched against all the incoming HTTP requests. When a host is provided the rules apply to that host else the rule applies to all inbound HTTP traffic through the IP address specified.
The rules contain a list of paths each of which has an associated backend defined with a serviceName and servicePort.
A single default backend is often configured in an Ingress controller to service any requests that do not match a path in the spec.
An Ingress controller is bootstrapped with some load balancing policy settings that it applies to all Ingress, such as the load balancing algorithm, backend weight scheme, while more
advanced concepts such as persistent sessions, dynamic weights is provided through the load balancer used for the Service.
[Ingress controllers](https://kubernetes.io/docs/concepts/services-networking/ingress-controllers/) are not started automatically with a cluster.

There are [many ingress solutions](https://learnk8s.io/kubernetes-ingress-api-gateway/) available such as [Contour](https://github.com/heptio/contour) and [Treafik Ingress](https://docs.traefik.io/user-guide/kubernetes/) which handle HTTP traffic, [Citrix Ingress](https://github.com/citrix/citrix-k8s-ingress-controller) supports TCP/UDP traffic and [HAProxy Ingress](https://github.com/jcmoraisjr/haproxy-ingress) support Websockets. 

The [NGINX Ingress](https://github.com/kubernetes/ingress-nginx) is the most popular Ingress solution and would be setting up as below.
Ingress consists of two components: Ingress resource and Ingress controller. Ingress resource is a collection of rules for the inbound traffic to reach Services.
[Ingress controller](https://devopscube.com/kubernetes-ingress-tutorial/) acts upon the rules set by the Ingress Resource and routes external traffic typically via an HTTP or load balancer.
Ingress controller needs a specific namespace, service account, cluster role bindings, configmaps etc which can be created using the yaml file from [official ingress repo](https://github.com/kubernetes/ingress-nginx/tree/master/deploy).
The routing rules are maintained as Kubernetes ingress object which are baked into pods which enables dynamic configuration of routing rules without redeploying the proxy pods.
An Ingress Resource object is a collection of rules for routing inbound traffic to Kubernetes Services. It can also determines which controller to utilize to serve traffic.
The kind: Ingress dictates it is an Ingress Resource object. Ingress rules are created in same namespace where the services are deployed. The NGINX controller must be exposed for external access. 
This is done using Service type: LoadBalancer or NodePort on the NGINX controller service.
The default backend is a Service which handles all URL paths and hosts the NGINX controller.

There are various [advanced ingress configuration](https://docs.giantswarm.io/guides/advanced-ingress-configuration/) which allows to aggregate several Ingress rules into a single Ingress definition, route to different services based on path (Path Based Fanout) and enable TLS/SSL pass through.
The Ingress Controller also includes support for adding [basic or digest http authentication types](https://tools.ietf.org/html/rfc2617) to an Ingress rule.
A `auth` file is created using `htpasswd` generator tool containing usernames and passwords per line, which is used to create a secret e.g. myauthsecret to be specified in below configuration.

    apiVersion: extensions/v1beta1
    kind: Ingress
    metadata:
      name: <ingress-name>
      annotations:
        # type of authentication [basic|digest]
        nginx.ingress.kubernetes.io/auth-type: basic
        # name of the secret that contains the user/password definitions
        nginx.ingress.kubernetes.io/auth-secret: myauthsecret
        # message to display with an appropiate context why the authentication is required
        nginx.ingress.kubernetes.io/auth-realm: "Authentication Required - foo"
    

An external service can be used to provide authentication with Ingress rule annotated with `nginx.ingress.kubernetes.io/auth-url` to indicate the URL where the HTTP request should be sent and `nginx.ingress.kubernetes.io/auth-method` to specify the HTTP method to use (GET or POST).
To enable Cross-Origin Resource Sharing (CORS) in an Ingress rule add the annotation `ingress.kubernetes.io/enable-cors: "true"`.
When the exposed URLs in the backend service differs from the specified path in the Ingress rule, then without URL rewrite any request will return 404. To circumvent this the `ingress.kubernetes.io/rewrite-target` annotation is used to alter the path expected by the service.
The annotation `ingress.kubernetes.io/limit-connections` limit the number of concurrent connections allowed from a single IP address while the annotation `ingress.kubernetes.io/limit-rps` limit the number of connections that may be accepted from a given IP each second. 
This can be used to mitigate [DDoS Attacks](https://www.nginx.com/blog/mitigating-ddos-attacks-with-nginx-and-nginx-plus).
By default NGINX uses http to reach the services. Adding the annotation `nginx.ingress.kubernetes.io/secure-backends: "true"` in the Ingress rule changes the protocol to https.
The annotation `nginx.ingress.kubernetes.io/affinity` enables and sets the affinity type in all upstreams of an Ingress, allowing the request to always be directed to the same upstream server.
The allowed client IP source ranges can be configured using the `nginx.ingress.kubernetes.io/whitelist-source-range` annotation which takes the value as a comma separated list of CIDRs, e.g. 10.0.0.0/24,172.10.0.1.
In order to support TLS, a certificate can be provided using the flag `--default-ssl-certificate`.
The NGINX Ingress Controller creates an NGINX configuration file. Chunks of configuration, so-called configuration snippets can be directly passed into any ingress manifest, which would be added to the NGINX configuration.
The annotation scheme `nginx.ingress.kubernetes.io/configuration-snippet` in the metadata section of the manifest allows to do the same.
 
   ![NGINX Ingress](images/nginx-ingress.png)


### Setup of NGINX Ingress Controller

Create `ingress` namespace for all resources for [Nginx Ingress controller](https://akomljen.com/kubernetes-nginx-ingress-controller/).

    $ kubectl create namespace ingress
    
Create a default backend endpoint redirects all requests undefined by Ingress rules and default backend service.
    
    $ kubectl create -f default-backend-service.yaml -n=ingress
    
Create a Nginx config to show a VTS page on our load balancer.
    
    $ kubectl create -f nginx-ingress-controller-config-map.yaml -n=ingress
    
Create RBAC rules for Ingress controller.
    
    $ kubectl create -f nginx-ingress-controller-roles.yaml -n=ingress
    
Create Ingress controller.
    
    $ kubectl create -f nginx-ingress-controller-deployment.yaml -n=ingress
    
Define ingress object or Ingress rules for load balancer status page and web applications. The `nginx.ingress.kubernetes.io/rewrite-target: /` annotation currently redirects requests to the `/`.

    $ kubectl create -f nginx-ingress.yaml -n=ingress
    
Expose `nginx-ingress-lb` deployment for external access which is exposed using NodePort.
    
    $ kubectl create -f nginx-ingress-controller-service.yaml -n=ingress

Describe created ingress object to check the configurations.

    $ kubectl describe ingress  -n ingress

### Notes

* The [nginx-ingress.yaml](../ingress-controller/nginx-ingress.yaml) requires the DNS address of any kubernetes node which is accessible outside the cluster. 
