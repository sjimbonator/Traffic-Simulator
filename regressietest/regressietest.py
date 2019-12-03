import paho.mqtt.client as mqtt #import the client
import time

group_ID = "7"

topic_trafficlights = list()
topic_warninglights = list()
topic_sensors = list()
topic_barriers = list()
boat_light = list()
train_light = list()

#topics for cars (traffic lights + sensors)
topic_trafficlights.append(group_ID+"/motorised/0/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/1/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/2/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/3/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/4/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/5/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/6/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/7/traffic_light/0")
topic_trafficlights.append(group_ID+"/motorised/8/traffic_light/0")
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


#topics for cycles (traffic lights + sensors)
topic_trafficlights.append(group_ID+"/cycle/0/traffic_light/0")
topic_trafficlights.append(group_ID+"/cycle/1/traffic_light/0")
topic_trafficlights.append(group_ID+"/cycle/2/traffic_light/0")
topic_trafficlights.append(group_ID+"/cycle/3/traffic_light/0")
topic_trafficlights.append(group_ID+"/cycle/4/traffic_light/0")
topic_sensors.append(group_ID+"/cycle/0/sensor/0")
topic_sensors.append(group_ID+"/cycle/1/sensor/0")
topic_sensors.append(group_ID+"/cycle/2/sensor/0")
topic_sensors.append(group_ID+"/cycle/3/sensor/0")
topic_sensors.append(group_ID+"/cycle/3/sensor/1")
topic_sensors.append(group_ID+"/cycle/4/sensor/0")
topic_sensors.append(group_ID+"/cycle/4/sensor/1")


#topics for foot (traffic lights + sensors)
topic_trafficlights.append(group_ID+"/foot/0/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/1/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/2/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/3/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/4/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/5/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/6/traffic_light/0")
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


#topics for vessel (boat_lights, sensors, warning_light, barrier, deck)
boat_light.append(group_ID+"/vessel/0/boat_light/0")
boat_light.append(group_ID+"/vessel/0/boat_light/1")
topic_sensors.append(group_ID+"/vessel/0/sensor/0")
topic_sensors.append(group_ID+"/vessel/0/sensor/1")
topic_sensors.append(group_ID+"/vessel/0/sensor/2")
topic_sensors.append(group_ID+"/vessel/0/sensor/3")
topic_warninglights.append(group_ID+"/vessel/0/warning_light/0")
topic_barriers.append(group_ID+"/vessel/0/barrier/0")
deck = group_ID+"/vessel/0/deck/0"


#topics for track (sensor, warning_light, barrier, train_light)
topic_sensors.append(group_ID+"/track/0/sensor/0")
topic_sensors.append(group_ID+"/track/0/sensor/1")
topic_sensors.append(group_ID+"/track/0/sensor/2")
topic_warninglights.append(group_ID+"/track/0/warning_light/0")
topic_barriers.append(group_ID+"/track/0/barrier/0")
train_light.append(group_ID+"/vessel/0/train_light/0")
train_light.append(group_ID+"/vessel/0/train_light/1")


############
def on_message(client, userdata, message):
    payload = str(message.payload.decode("utf-8"))
    #print("message received from topic  " , message.topic)
    #print("payload =",payload)

    for topic in topic_sensors:
        if topic == message.topic and (payload != "0" and payload != "1"):

            print("message received " , message.topic)
            print("payload =",payload)
            print ("sensor payload value does not follow the protocol! (values of 0 and 1)" )
            print("---------------------------------------------------------")

    for topic in topic_trafficlights:
        if topic == message.topic and (payload != "0" and payload != "1" and payload != "2"):
            print("message received " , message.topic)
            print("payload =",payload)
            print ("traffic light payload value does not follow the protocol! (values of 0, 1, 2)" )
            print("---------------------------------------------------------")

    for topic in topic_barriers:
        if topic == message.topic and (payload != "0" and payload != "1"):
            print("message received " , message.topic)
            print("payload =",payload)
            print ("barrier payload value does not follow the protocol! (values of 0 and 1)")
            print("---------------------------------------------------------")

    for topic in topic_warninglights:
        if topic == message.topic and (payload != "0" and payload != "1"):
            print("message received " , message.topic)
            print("payload =",payload)
            print ("warning light payload value does not follow the protocol! (values of 0 and 1)")
            print("---------------------------------------------------------")

    for topic in boat_light:
        if topic == message.topic and (payload != "0" and payload != "1"):
            print("message received " , message.topic)
            print("payload =",payload)
            print ("boat_light payload value does not follow the protocol! (values of 0 and 1)")
            print("---------------------------------------------------------")

    for topic in train_light:
        if topic == message.topic and (payload != "0" and payload != "1"):
            print("message received " , message.topic)
            print("payload =",payload)
            print ("train_light payload value does not follow the protocol! (values of 0 and 1)")
            print("---------------------------------------------------------")

    if deck == message.topic and (payload != "0" and payload != "1"):
            print("message received " , message.topic)
            print("payload =",payload)
            print ("deck payload value does not follow the protocol! (values of 0 and 1)")
            print("---------------------------------------------------------")

    if message.topic not in topic_sensors and  message.topic not in topic_trafficlights and message.topic not in topic_barriers and message.topic not in topic_warninglights and message.topic not in boat_light and message.topic not in train_light and message.topic != deck:
        print("message received " , message.topic)
        print ("published topic does not follow the protocol!")
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
print("checking for mistakes in the topic and payload")
client.subscribe(group_ID+"/#")

print("---------------------------------------------------------")

time.sleep(15000) # wait
client.loop_stop() #stop the loop
