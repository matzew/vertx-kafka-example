# Simple Hello World Kafka Consumer

Personal project for playing with Vertx3, and Apache Kafka.

# Starting Apache Zookeeper and Apache Kafka

I use Docker for simplicity, to get going, we need to run Zookeeper:
```
docker run -d --name zookeeper jplock/zookeeper:3.4.6
```

Next, we need to start Kafka and link the Zookeeper Linux container to it:
``` 
docker run -d --name kafka --link zookeeper:zookeeper ches/kafka
```

Now, that the broker is running, we need to figure out the IP address of it:
``` 
docker inspect --format '{{ .NetworkSettings.IPAddress }}' kafka  
```

We use this IP address when connecting a _Producer_ and a _Consumer_ to Apache Kafka

# The Producer

For Producer we use a CLI called [`kafkacat`](https://github.com/edenhill/kafkacat), like:

``` 
kafkacat -P -b IP_ADDRESS_OF_KAFKA:9092 -t messages
```

and simply type some lines into the shell, after connecting.

# The VERTX consumer

The `SimpleKafkaConsumer` class is leveraging the new Vertx-kafka-client lib. It's very simple... and just prints the values of the incomming messages...

# Build and Run!

Trigger the build:
```
 mvn clean install
 ```
 
 and run the client:
 ``` 
 ./target/consumer-client
 ```
 
 That's it!
 