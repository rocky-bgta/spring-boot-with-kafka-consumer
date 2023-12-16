 # spring-boot-with-kafka

# required cmd 
docker exec -it kafka /bin/sh
ls
cd opt
ls
cd kafka_2.13-2.8.1
pwd
ls
cd bin
# To create topic =========
kafka-topics.sh --create --zookeeper zookeeper:2181 --topic topicName --partitions 3 --replication-factor 1

# To published topic ==============
kafka-console-producer.sh --topic topicName --bootstrap-server localhost:9092

# To create consumer topic ==============
kafka-console-consumer.sh --topic topicName --from-beginning --bootstrap-server localhost:9092