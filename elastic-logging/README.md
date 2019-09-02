# Elastic Stack (Elasticsearch, Logstash and Kibana)

Elastic Stack is a group of open source applications from Elastic designed to take data from any source and in any format and then search, analyze, and visualize that data in real time.
It is a collection of three projects [Elasticsearch](https://www.elastic.co/products/elasticsearch), [Logstash](https://www.elastic.co/products/logstash), and [Kibana](https://www.elastic.co/products/kibana) which enable to log/filter messages and search/analyze logs. The Elastic Stack is the next evolution of the ELK Stack.

* [Elasticsearch](https://www.elastic.co/products/elasticsearch) is a real-time, distributed storage, JSON-based search, and analytics engine designed for horizontal scalability, maximum reliability, and easy management. It can be used for many purposes, but one context where it excels is indexing streams of semi-structured data, such as logs or decoded network packets.
* [Kibana](https://www.elastic.co/products/kibana) is an open source analytics and visualization platform designed to work with Elasticsearch. Kibana can be used to search, view, and interact with data stored in Elasticsearch indices, allowing advanced data analysis and visualizing data in a variety of charts, tables, and maps.
* [Logstash](https://www.elastic.co/products/logstash) is a powerful tool that integrates with a wide variety of deployments. It offers a large selection of plugins to parse, enrich, transform, and buffer data from a variety of sources. If the data requires additional processing that is not available in Beats, then Logstash can be added to the deployment.
* [Beats](https://www.elastic.co/products/beats) are open source data shippers that can be installed as agents on servers to send operational data directly to Elasticsearch or via Logstash, where it can be further processed and enhanced. There’s a number of Beats for different purposes:
        
    * Filebeat: Log files
    * Metricbeat: Metrics (node analytics)
    * Packetbeat: Network data
    * Heartbeat: Uptime monitoring 

* [Curator](https://www.elastic.co/guide/en/elasticsearch/client/curator/current/ilm.html) from ElasticSearch allows to apply batch actions to indexes (close, create, delete, etc.).  One specific use case is applying a retention policy to indexes, deleting any indexes that are older than a certain threshold.

   ![Elastic Stack](images/elastic-stack.png)

Elasticsearch is installed as a StatefulSet so that there is some awareness between them. Filebeat and Metricbeat instances are installed as DaemonSets, meaning there is one for every node we have. This is because they read the logs right off the node. Without this we would miss logs  from some nodes.

## Installation

To install Elastic Stack on kubernetes cluster apply all the files as below:

    $ kubectl apply -f elastic-logging

It's safest to do the elasticsearch service first and wait for the pods to start up as the logstash pod will fail if it cannot connect to the elasticsearch instance and will need to be restarted.

    $ kubectl apply -f elasticsearch-ss.yaml
    $ kubectl apply -f logstash-deployment.yaml
    $ kubectl apply -f curator-cronjob.yaml
    $ kubectl apply -f filebeat-ds.yaml
    $ kubectl apply -f metricbeat-ds.yaml
    $ kubectl apply -f kibana-deployment.yaml
