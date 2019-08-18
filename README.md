# Spring Kubernetes Micro Services Showcase

[Kubernetes](https://kubernetes.io/docs/concepts/overview/what-is-kubernetes/) is leading orchestration framework which enables automated deployments, scaling, and management of containerized applications.
Spring Kubernetes Micro Services is a showcase application which uses the new [Spring Cloud Kubernetes](https://spring.io/projects/spring-cloud-kubernetes) framework to manage service discovery and is deployed on kubernetes network.
The application provides services to fetch the generated report documents using [Spring Batch](https://spring.io/projects/spring-batch) and stored on a [Minio cloud storage](https://min.io/).
The application has below services.

* [Discovery Service](discovery-service/README.md): Eureka discovery service allows micro services to find and communicate with each other.
* [Data Service](data-service/README.md): Data service provides reactive services using Spring WebFlux to store and fetch various report documents data.
* [Storage Service](storage-service/README.md): Analytics services consume data from various sources, mainly finance-service and data-service and provide analytical details regarding the 
* [Document Service](document-service/README.md): Analytics services consume data from various sources, mainly finance-service and data-service and provide analytical details regarding the

Follow the below documentation in order to setup this application using Kubernetes.

* [Kubernetes Architecture](http://emprovisetech.blogspot.com/2018/12/kubernetes-container-orchestration-at.html): Blog post discussing Kubernetes concepts, architecture and various commands in depth.
* [Kubernetes Installation](readme/Kubernetes_Installation.md): Setup Kubernetes Master and Worker nodes for [Ubuntu Bionic](http://releases.ubuntu.com/18.04/). Please refer to various documentations online for respective operating systems.
* [Docker Registry](readme/Docker_Registry.md): Docker registry setup is required to distribute docker images to various Kubernetes pods. 
* [Kubernetes Commands](readme/Kubernetes_Commands.md): Kubernetes commands provides some frequently used commands to deploy and delete/clean up Kubernetes services and debug or troubleshoot kubernetes pods.
