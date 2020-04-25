from json import loads
from kafka import KafkaConsumer
from time import sleep

consumer = KafkaConsumer(
    'booking_test',
     bootstrap_servers=['localhost:9092'],
     auto_offset_reset='earliest',
     enable_auto_commit=True,
     group_id=None,
     value_deserializer=lambda x: loads(x.decode('utf-8'))
)

from cassandra.cluster import Cluster
cluster = Cluster(['104.154.161.131'], port=31942)
session = cluster.connect('kk')

booking_sql_str = session.prepare("insert into kk.booking (booking_id,user_id,booking_time,participation_time,destination_country_id,activity_id,activity,merchant_id,bu_level_1,bu_level_2,pay_amount,is_domestic,is_new_order,is_fraud,participants) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")

from datetime import datetime
import csv

for message in consumer:
    line = message.value
    data = [ '{}'.format(x) for x in list(csv.reader([line], delimiter=',', quotechar='"'))[0] ]
    print(data)
    session.execute(booking_sql_str,(
        int(data[0]),
        int(data[1]),
        datetime.strptime(data[2],'%Y-%m-%d %H:%M:%S %Z'),
        datetime.strptime(data[3],'%Y-%m-%d %H:%M:%S %Z'),
        int(data[4]),
        int(data[5]),
        str(data[6]),
        int(data[7]),
        data[8],
        data[9],
        float(data[10]),
        bool(data[11]),
        bool(data[12]),
        bool(data[13]),
        int(data[14])
        ))
    sleep(5)
