from json import loads
from kafka import KafkaConsumer
from time import sleep

consumer = KafkaConsumer(
    'review_test',
     bootstrap_servers=['localhost:9092'],
     auto_offset_reset='earliest',
     enable_auto_commit=True,
     group_id=None,
     value_deserializer=lambda x: loads(x.decode('utf-8'))
)

from cassandra.cluster import Cluster
cluster = Cluster(['104.154.161.131'], port=31942)
session = cluster.connect('kk')

review_sql_str = session.prepare("insert into kk.review (review_id,review_time,user_id,review_score,review_eng,booking_id,is_robot,activity_id,activity,merchant_id) values (?,?,?,?,?,?,?,?,?,?)")

from datetime import datetime
import csv

for message in consumer:
    line = message.value
    data = [ '{}'.format(x) for x in list(csv.reader([line], delimiter=',', quotechar='"'))[0] ]
    print(data)
    session.execute(review_sql_str,(
        int(data[0]),
        datetime.strptime(data[1],'%Y-%m-%d %H:%M:%S %Z'),
        int(data[2]),
        float(data[3]),
        data[4],
        int(data[5]),
        bool(data[6]),
        int(data[7]),
        data[8],
        int(data[9])
        ))
    sleep(5)
