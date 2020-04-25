from time import sleep
from kafka import KafkaProducer
from json import dumps

producer = KafkaProducer(bootstrap_servers=['localhost:9092'],
                         value_serializer=lambda x:
                         dumps(x).encode('utf-8'))

isLineOne = True
with open("/home/gao_jc92/food_booking.csv") as fp:
    for line in fp:
        if isLineOne:
            isLineOne = False
            continue
        print(line)
        producer.send('booking_test', value=line)
        sleep(5)
