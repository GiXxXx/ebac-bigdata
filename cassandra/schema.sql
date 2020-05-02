
CREATE KEYSPACE kk WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
CREATE TABLE kk.fact_booking (
  booking_id int,
  user_id int,
  booking_time timestamp,
  participation_time timestamp,
  destination_country_id int,
  activity_id int,
  activity varchar,
  merchant_id int,
  bu_level_1 varchar,
  bu_level_2 varchar,
  pay_amount double,
  is_domestic boolean,
  is_new_order boolean,
  is_fraud boolean,
  participants int,
  review_id int,
  review_time timestamp,
  review_score double,
  review_eng varchar,
  is_robot boolean,
  PRIMARY KEY (booking_id, user_id, booking_time)
);

CREATE TABLE kk.event_browsing (
  session_id varchar,
  full_visitor_id varchar,
  user_id int,
  platform varchar,
  visit_start_time timestamp,
  duration_seconds int,
  is_bounce boolean,
  device_language varchar,
  is_exit_page boolean,
  activity_id varchar,
  activity varchar,
  country_id int,
  activity_page_visit_duration double,
  PRIMARY KEY (session_id, user_id, activity_id, visit_start_time, activity_page_visit_duration)
);



-- drop keyspace kk;
-- CREATE KEYSPACE kk WITH REPLICATION = { 'class' : 'SimpleStrategy', -- 'replication_factor' : 1 };
--
-- drop table kk.booking;
-- CREATE TABLE kk.booking (
--   booking_id int,
--   user_id int,
--   booking_time timestamp,
--   participation_time timestamp,
--   destination_country_id int,
--   activity_id int,
--   activity varchar,
--   merchant_id int,
--   bu_level_1 varchar,
--   bu_level_2 varchar,
--   pay_amount double,
--   is_domestic boolean,
--   is_new_order boolean,
--   is_fraud boolean,
--   participants int,
--   PRIMARY KEY (booking_id, user_id, booking_time)
-- );
--
-- copy kk.booking (booking_id,user_id,booking_time,participation_time,-- destination_country_id,activity_id,activity,merchant_id,bu_level_1,bu_level_2,-- pay_amount,is_domestic,is_new_order,is_fraud,participants) from '/tmp/-- food_booking.csv' with HEADER = TRUE;
--
--
-- drop table kk.review;
-- CREATE TABLE kk.review (
--   review_id int,
--   review_time timestamp,
--   user_id int,
--   review_score double,
--   review_eng varchar,
--   booking_id int,
--   is_robot boolean,
--   activity_id int,
--   activity varchar,
--   merchant_id int,
--   PRIMARY KEY (review_id, review_time, user_id, booking_id)
-- );
--
-- copy kk.review (review_id,review_time,user_id,review_score,review_eng,-- booking_id,is_robot,activity_id,activity,merchant_id) from '/tmp/food_reviews.-- csv' with HEADER = TRUE;
