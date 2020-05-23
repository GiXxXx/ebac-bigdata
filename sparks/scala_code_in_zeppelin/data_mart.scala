// data mart demo

// init sql syntax
import org.apache.spark.sql.SQLContext
val sqlcontext = new org.apache.spark.sql.SQLContext(sc)

// cassandra dep
import com.datastax.spark.connector._
import org.apache.spark.sql.cassandra._

// read table from cassandra
val df = sqlcontext.read
    .format("org.apache.spark.sql.cassandra")
    .options(Map("table" -> "booking", "keyspace" -> "kk"))
    .load()

// filter only dec data
val dec_booking = df.filter(df("booking_time").gt(lit("2019-12-01")))

// register df as a temp table for SQL operation
dec_booking.registerTempTable( "dec_booking" )

// sql queries
val orders = sqlcontext.sql(
    """
    select to_date(booking_time) as date
    , bu_level_2
    , count(*) as total_orders
    , sum(pay_amount) as GMV
    from dec_booking
    group by to_date(booking_time), bu_level_2
    """
    )

val users = sqlcontext.sql(
    """
    select user_id
        , count(*) as total_orders
        , sum(pay_amount) as GMV
        , mean(pay_amount) as ABS
        , min(booking_time) as first_booking
    from dec_booking
    group by user_id
    """
    )

val order_by_date = sqlcontext.sql(
    """
    select to_date(booking_time) as date
    , count(*) as total_orders
    from dec_booking
    group by to_date(booking_time)
    """
    )

// configure MySQL data mart connection
import java.util.Properties
val connectionProperties = new Properties()
connectionProperties.put("user", "root")
connectionProperties.put("password", "password")

// create table and write data
order_by_date.write
  .option("createTableColumnTypes", "date DATE, count  INT")
  .jdbc("jdbc:mysql://34.68.130.165:30306/kk", "kk.summary", connectionProperties)

orders.write
  .option("createTableColumnTypes", "date DATE, bu_level_2 VARCHAR, total_orders INT, GMV, FLOAT")
  .jdbc("jdbc:mysql://34.68.130.165:30306/kk", "kk.orders_by_date", connectionProperties)

users.write
  .option("createTableColumnTypes", "user_id INT, total_orders INT, GMV  FLOAT, ABS FLOAT, first_booking  TIMESTAMP")
  .jdbc("jdbc:mysql://34.68.130.165:30306/kk", "kk.users", connectionProperties)
