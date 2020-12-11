import java.util
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{StreamingContext, _}
import org.apache.spark.{SparkConf, SparkContext}
import twitter4j.Status

object twitters {
  def main(args: Array[String]): Unit = {

    if (args.length < 2) {
      println("Error: Insufficient arguments!! Enter 2 arguments: Streaming topic and hashtag name")
    }

    // Config
    val sparkConfig = new SparkConf().setAppName("spark-streaming-with-twitter-and-kafka").setMaster(sys.env.getOrElse("spark.master", "local[*]"))
    val sc = new SparkContext(sparkConfig)
    sc.setLogLevel("ERROR")
    val IntervalSlide = Duration(1 * 1000)
    val timeoutJobLength = 3600 * 1000	//Wait time for stream closure
    val kafkaBrokers = "localhost:9092"

    val properties = new util.HashMap[String, Object]()
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers)
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")
    val stream = new StreamingContext(sc, IntervalSlide)

    // Stream creation
    val tweetStream: DStream[Status] = TwitterUtils.createStream(stream, None, args.slice(1, args.length))

    // Analyze sentiment
    val tweets = tweetStream.map(x => (x.getText, Analyse.get(x.getText).toString))
    tweets.print()

    //Send message to producer
    tweets.foreachRDD(rdd => {
      rdd.foreachPartition(partition => {
        val producer = new KafkaProducer[String,String](properties)
        partition.foreach(record => {
          val producerMessage = new ProducerRecord[String, String](args(0), record._1, record._2)
          println(producerMessage)
          producer.send(producerMessage)
        })
        producer.flush()
        producer.close()
      })
    })

    stream.start()
    stream.awaitTerminationOrTimeout(timeoutJobLength) //Wait for stream closure
  }
}