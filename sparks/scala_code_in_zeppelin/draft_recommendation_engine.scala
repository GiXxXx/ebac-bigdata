// draft code for recommendatin engine

// cassandra dep
import com.datastax.spark.connector._
import org.apache.spark.sql.cassandra._

// get review df
val review_df = sc.cassandraTable("kk", "review_demo")

// split train and test set
val Array(training, test) = review_df.randomSplit(Array(0.7, 0.3), seed = 100)

// import ALS
import org.apache.spark.ml.recommendation.{ALS, ALSModel}

val als = new ALS()
      .setMaxIter(10)
      .setRank(20)
      .setUserCol("user_id")
      .setItemCol("activity_id")
      .setRatingCol("review_score")
      .setRegParam(0.01)

// fit model
val model = als.fit(data)

// should features
model.userFactors.show(10)
model.itemFactors.show(10)

// predict
val predict = model.transform(test.toDF().sample(withReplacement = false, 0.1))

predict.show(50)
