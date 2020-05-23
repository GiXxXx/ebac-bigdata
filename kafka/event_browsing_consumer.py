from json import loads
from kafka import KafkaConsumer
from time import sleep
from datetime import datetime

# configure consumer
consumer = KafkaConsumer(
    'event_browsing',
     bootstrap_servers=['localhost:9092'],
     auto_offset_reset='earliest',
     enable_auto_commit=True,
     group_id=None,
     value_deserializer=lambda x: loads(x.decode('utf-8'))
)

# configura cassandra connection
from cassandra.cluster import Cluster
cluster = Cluster(['34.68.130.165'], port=31942)
session = cluster.connect('kk')

# prepare CQL insert query
event_browsing_sql_str = session.prepare('''
    insert into kk.event_browsing (
        session_id,
        full_visitor_id,
        user_id,
        platform,
        visit_start_time,
        duration_seconds,
        is_bounce,
        device_language,
        is_exit_page,
        activity_id,
        activity,
        country_id,
        activity_page_visit_duration
    ) values (?,?,?,?,?,?,?,?,?,?,?,?,?)
    ''')

# get message from consumer and load into Cassandra
for message in consumer:
    line = message.value
    data = loads(line)
    data = loads(data['json'])
    print(data)
    # convert null userid to -1
    if not data['user_id']:
        data['user_id'] = -1
    print('BEGIN INSERTING.....')
    session.execute(event_browsing_sql_str,(
        data['session_id'],
        data['full_visitor_id'],
        data['user_id'],
        data['platform'],
        datetime.strptime(data['visit_start_time'], '%Y-%m-%dT%H:%M:%SZ'),
        data['duration_seconds'],
        bool(data['is_bounce']),
        data['device_language'],
        bool(data['is_exit_page']),
        data['activity_id'],
        data['activity'],
        data['country_id'],
        data['activity_page_visit_duration']
        ))
    print('END INSERTING.....')
    sleep(5)
