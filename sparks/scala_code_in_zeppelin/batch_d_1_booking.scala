// D-1 batch job to ingest booking data

// init sql syntax
import org.apache.spark.sql.SQLContext
val sqlcontext = new org.apache.spark.sql.SQLContext(sc)

// cassandra dep
import com.datastax.spark.connector._
import org.apache.spark.sql.cassandra._

// get yesterday date
import java.time.{ZonedDateTime, ZoneId}
import java.time.format.DateTimeFormatter

// no 2020 data, mock using 2019 data
val yesterday = ZonedDateTime.now(ZoneId.of("GMT+8")).minusYears(1).minusDays(1)
val formatter_start = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")
val formatter_end = DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")
val yesterdayDateString_start = formatter_start format yesterday
val yesterdayDateString_end = formatter_end format yesterday

println(yesterdayDateString_start)
println(yesterdayDateString_end)

// generate sql
val sql=f"""
select
    *
from
    kk.booking
where
    booking_time >= '$yesterdayDateString_start%s' and
    booking_time <= '$yesterdayDateString_end%s'
"""

// read into rdd
val booking_df = sqlcontext.read
  .format("jdbc")
  .option("url","jdbc:mysql://34.68.130.165:30306/kk")
  .option("dbtable",  s"( $sql ) t")
  .option("user", "root")
  .option("password", "password")
  .load()

// No. of records in cassandra before
val rdd = sc.cassandraTable("kk", "booking_demo")
rdd.count

// ingest into cassandra
booking_df.write
    .options(Map("table" -> "booking_demo", "keyspace" -> "kk"))
    .mode("append")
    .format("org.apache.spark.sql.cassandra").save()

// No. of records in cassandra before
val rdd = sc.cassandraTable("kk", "booking_demo")
rdd.count


