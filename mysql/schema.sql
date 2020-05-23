
# create schema
create schema kk;

# create tables
CREATE TABLE review (
  review_id int,
  review_time timestamp,
  user_id int,
  review_score double,
  review_eng text,
  booking_id int,
  is_robot boolean,
  activity_id int,
  activity text,
  merchant_id int,
  PRIMARY KEY (review_id, review_time, user_id, booking_id)
);

CREATE TABLE booking (
  booking_id int,
  user_id int,
  booking_time timestamp,
  participation_time timestamp,
  destination_country_id int,
  activity_id int,
  activity text,
  merchant_id int,
  bu_level_1 text,
  bu_level_2 text,
  pay_amount double,
  is_domestic boolean,
  is_new_order boolean,
  is_fraud boolean,
  participants int,
  PRIMARY KEY (booking_id, user_id, booking_time)
);

# load data
LOAD DATA LOCAL INFILE '/tmp/food_booking.csv'
INTO TABLE booking
COLUMNS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES;

LOAD DATA LOCAL INFILE '/tmp/food_reviews.csv'
INTO TABLE review
COLUMNS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES;
