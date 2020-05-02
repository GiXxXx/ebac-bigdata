from time import sleep
from kafka import KafkaProducer
from json import dumps
import os

producer = KafkaProducer(bootstrap_servers=['localhost:9092'],
                         value_serializer=lambda x:
                         dumps(x).encode('utf-8'))

folder = '/home/gao_jc92/event_browsing'
for filename in os.listdir(folder):
   with open(os.path.join(folder, filename), 'r') as fp:
      for line in fp:
        print(line)
        producer.send('event_browsing', value=line)
        sleep(5)
