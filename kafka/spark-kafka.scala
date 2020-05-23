import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils

val ssc = new StreamingContext(sc, Seconds(5))
val kafkaParams = Map("metadata.broker.list" -> "34.125.178.150:9092")
val topics = Set("event_browsing1")
val stream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)

stream.foreachRDD { rdd => println(rdd.first)}

ssc.start()
ssc.awaitTermination()
