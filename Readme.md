This project makes use of kafka to get the tweets on twitter over a particular time range. We tried using this during the finals of the Indian Premier League 2020 finals game and performed the sentiment analysis of the tweets that were recieved real-time. 

This project needs a twitter developer account in order to get the live tweets for the dataset. 


Steps for executing the kafka program : 

1.	Start the apache zookeeper on a terminal window 
2.	Start the kafka broker service on another terminal window 
3.	Run the spark application using the spark-submit command which needs to be run in the directory in which spark has been installed. 

bin/spark-submit --class twitters kafka_2.11-0.1.jar {stream_name} {hashtag_name}

Here the stream_name/topic that we chose was ipl and the hashtag name being #ipl2020.

4.	Start a new consumer topic which would be able to get the results of the sentiment analysis. 
5.	Start Elastic Search, Kibana and Logstash. We can see the graphs using the following link:

http://localhost:5601/app/kibana#/visualize
