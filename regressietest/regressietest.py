import paho.mqtt.client as mqtt #import the client1
import time

group_ID = "7"

topic_trafficlights = list()
topic_trafficlights.append(group_ID+"/motorised/0/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/1/0/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/1/1/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/2/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/3/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/4/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/5/0/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/5/1/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/6/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/7/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/8/traffic_light/0")

topic_sensors = list()
topic_sensors.append(group_ID+"/motorised/0/sensor/1")
topic_sensors.append(group_ID+"/motorised/0/sensor/0")
topic_sensors.append(group_ID+"/motorised/1/0/sensor/1")
topic_sensors.append(group_ID+"/motorised/1/0/sensor/0")
topic_sensors.append(group_ID+"/motorised/1/1/sensor/1")
topic_sensors.append(group_ID+"/motorised/1/1/sensor/0")
topic_sensors.append(group_ID+"/motorised/2/sensor/1")
topic_sensors.append(group_ID+"/motorised/2/sensor/0")
topic_sensors.append(group_ID+"/motorised/3/sensor/1")
topic_sensors.append(group_ID+"/motorised/3/sensor/0")
topic_sensors.append(group_ID+"/motorised/4/sensor/1")
topic_sensors.append(group_ID+"/motorised/4/sensor/0")
topic_sensors.append(group_ID+"/motorised/5/0/sensor/0")
topic_sensors.append(group_ID+"/motorised/5/0/sensor/1")
topic_sensors.append(group_ID+"/motorised/5/1/sensor/0")
topic_sensors.append(group_ID+"/motorised/5/1/sensor/1")
topic_sensors.append(group_ID+"/motorised/6/sensor/1")
topic_sensors.append(group_ID+"/motorised/6/sensor/0")
topic_sensors.append(group_ID+"/motorised/7/sensor/1")
topic_sensors.append(group_ID+"/motorised/7/sensor/0")
topic_sensors.append(group_ID+"/motorised/8/sensor/1")
topic_sensors.append(group_ID+"/motorised/8/sensor/0")


############
def on_message(client, userdata, message):
    payload = str(message.payload.decode("utf-8"))
    print("message received " , payload)
    print("from topic =",message.topic)
    #print("message qos =",message.qos)

    for topic in topic_sensors:
        if topic == message.topic and (payload != "0" and payload != "1"):
            print ("sensor payload value does not follow the protocol! (values of 0 and 1)" )

    for topic in topic_trafficlights:
        if topic == message.topic and (payload != "0" and payload != "1" and payload != "2" and payload != "3"):
            print ("traffic light payload value does not follow the protocol! (values of 0, 1, 2 ,3)" )

    print("---------------------------------------------------------")
#############



broker_address="arankieskamp.com"
print("creating new instance")
client = mqtt.Client("Groep7Regressietest") #create new instance
client.on_message=on_message #attach function to callback
print("connecting to broker")
client.connect(broker_address) #connect to broker
client.loop_start() #start the loop

for topic in topic_sensors:
    print("Subscribed to",topic)
    client.subscribe(topic)
    
for topic in topic_trafficlights:
    print("Subscribed to",topic)
    client.subscribe(topic)

print("---------------------------------------------------------")

time.sleep(5000) # wait
client.loop_stop() #stop the loop
