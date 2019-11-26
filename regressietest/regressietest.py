import paho.mqtt.client as mqtt #import the client1
import time

group_ID = "7"

#barriers
topic_barriers = list()
#trains
topic_barriers.append(group_ID+"/track/0/barrier/0")
#boats
topic_barriers.append(group_ID+"/vessel/0/barrier/0")


#traffic lights
topic_trafficlights = list()
#traffic light for cars
topic_trafficlights.append(group_ID+"/motorised/0/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/1/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/2/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/3/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/4/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/5/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/6/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/7/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/8/traffic_light/0")

#traffic light for train
topic_trafficlights.append(group_ID+"/track/0/traffic_light/0")
topic_trafficlights.append(group_ID+"/track/1/traffic_light/0")
#traffic light for boat
topic_trafficlights.append(group_ID+"/vessel/0/traffic_light/0")
topic_trafficlights.append(group_ID+"/vessel/1/traffic_light/0")

#traffic light for bike
topic_trafficlights.append(group_ID+"/cycle/0/traffic_light/0")
topic_trafficlights.append(group_ID+"/cycle/1/traffic_light/0")
topic_trafficlights.append(group_ID+"/cycle/2/traffic_light/0")
topic_trafficlights.append(group_ID+"/cycle/3/traffic_light/0")
topic_trafficlights.append(group_ID+"/cycle/4/traffic_light/0")

#traffic light for foot
topic_trafficlights.append(group_ID+"/foot/0/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/1/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/2/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/3/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/4/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/5/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/6/traffic_light/0")


#warning lights
topic_warninglights = list()
topic_warninglights.append(group_ID+"/track/0/warning_light/0")
topic_warninglights.append(group_ID+"/vessel/0/warning_light/0")

#sensors
topic_sensors = list()
#sensor for cars
topic_sensors.append(group_ID+"/motorised/0/sensor/0")
topic_sensors.append(group_ID+"/motorised/0/sensor/1")
topic_sensors.append(group_ID+"/motorised/1/sensor/0")
topic_sensors.append(group_ID+"/motorised/1/sensor/1")
topic_sensors.append(group_ID+"/motorised/1/sensor/2")
topic_sensors.append(group_ID+"/motorised/1/sensor/3")
topic_sensors.append(group_ID+"/motorised/2/sensor/0")
topic_sensors.append(group_ID+"/motorised/2/sensor/1")
topic_sensors.append(group_ID+"/motorised/3/sensor/0")
topic_sensors.append(group_ID+"/motorised/3/sensor/1")
topic_sensors.append(group_ID+"/motorised/4/sensor/0")
topic_sensors.append(group_ID+"/motorised/4/sensor/1")
topic_sensors.append(group_ID+"/motorised/5/sensor/0")
topic_sensors.append(group_ID+"/motorised/5/sensor/1")
topic_sensors.append(group_ID+"/motorised/5/sensor/2")
topic_sensors.append(group_ID+"/motorised/5/sensor/3")
topic_sensors.append(group_ID+"/motorised/6/sensor/0")
topic_sensors.append(group_ID+"/motorised/6/sensor/1")
topic_sensors.append(group_ID+"/motorised/7/sensor/0")
topic_sensors.append(group_ID+"/motorised/7/sensor/1")
topic_sensors.append(group_ID+"/motorised/8/sensor/0")
topic_sensors.append(group_ID+"/motorised/8/sensor/1")
#sensor for train
topic_sensors.append(group_ID+"/track/0/sensor/0")
topic_sensors.append(group_ID+"/track/0/sensor/1")
topic_sensors.append(group_ID+"/track/0/sensor/2")
#sensor for boat
topic_sensors.append(group_ID+"/vessel/0/sensor/0")
topic_sensors.append(group_ID+"/vessel/1/sensor/1")
topic_sensors.append(group_ID+"/vessel/1/sensor/2")

#sensor for bike
topic_sensors.append(group_ID+"/cycle/0/sensor/0")
topic_sensors.append(group_ID+"/cycle/1/sensor/0")
topic_sensors.append(group_ID+"/cycle/2/sensor/0")
topic_sensors.append(group_ID+"/cycle/3/sensor/0")
topic_sensors.append(group_ID+"/cycle/3/sensor/1")
topic_sensors.append(group_ID+"/cycle/4/sensor/0")
topic_sensors.append(group_ID+"/cycle/4/sensor/1")
#sensor for foot
topic_sensors.append(group_ID+"/foot/0/sensor/0")
topic_sensors.append(group_ID+"/foot/0/sensor/1")
topic_sensors.append(group_ID+"/foot/1/sensor/0")
topic_sensors.append(group_ID+"/foot/1/sensor/1")
topic_sensors.append(group_ID+"/foot/2/sensor/0")
topic_sensors.append(group_ID+"/foot/2/sensor/1")
topic_sensors.append(group_ID+"/foot/3/sensor/0")
topic_sensors.append(group_ID+"/foot/3/sensor/1")
topic_sensors.append(group_ID+"/foot/4/sensor/0")
topic_sensors.append(group_ID+"/foot/4/sensor/1")
topic_sensors.append(group_ID+"/foot/5/sensor/0")
topic_sensors.append(group_ID+"/foot/5/sensor/1")
topic_sensors.append(group_ID+"/foot/6/sensor/0")
topic_sensors.append(group_ID+"/foot/6/sensor/1")










############
def on_message(client, userdata, message):
    payload = str(message.payload.decode("utf-8"))
    print("message received " , payload)
    print("from topic =",message.topic)
    #print("message qos =",message.qos)
    print (" topic from published: " + message.topic)

    for topic in topic_sensors:
        if topic == message.topic and (payload != "0" and payload != "1"):
            print ("sensor payload value does not follow the protocol! (values of 0 and 1)" )

    for topic in topic_trafficlights:
        if topic == message.topic and (payload != "0" and payload != "1" and payload != "2" and payload != "3"):
            print ("traffic light payload value does not follow the protocol! (values of 0, 1, 2 ,3)" )

    for topic in topic_barriers:
        if topic == message.topic and (payload != "0" and payload != "1"):
            print ("barrier payload value does not follow the protocol! (values of 0 and 1)")

    for topic in topic_warninglights:
        if topic == message.topic and (payload != "0" and payload != "1"):
            print ("warning light payload value does not follow the protocol! (values of 0 and 1)")

    print("---------------------------------------------------------")
#############



broker_address="arankieskamp.com"
print("creating new instance")
client = mqtt.Client("Groep7Regressietest") #create new instance
client.on_message=on_message #attach function to callback
print("connecting to broker")
client.connect(broker_address) #connect to broker
client.loop_start() #start the loop

print("Subscribed to",group_ID+"/#")
client.subscribe(group_ID+"/#")

print("---------------------------------------------------------")

time.sleep(5000) # wait
client.loop_stop() #stop the loop
