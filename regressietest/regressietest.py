import paho.mqtt.client as mqtt #import the client1
import time

group_ID = "7"

#barriers
topic_barriers = list()
#west > oost trains
topic_barriers.append(group_ID+"/track/0/barrier/0")
topic_barriers.append(group_ID+"/track/0/barrier/1")
topic_barriers.append(group_ID+"/track/0/barrier/2")
topic_barriers.append(group_ID+"/track/0/barrier/3")
topic_barriers.append(group_ID+"/track/0/barrier/4")
topic_barriers.append(group_ID+"/track/0/barrier/5")
topic_barriers.append(group_ID+"/track/0/barrier/6")
topic_barriers.append(group_ID+"/track/0/barrier/7")
topic_barriers.append(group_ID+"/track/0/barrier/8")
#oost > west trains
topic_barriers.append(group_ID+"/track/1/barrier/0")
topic_barriers.append(group_ID+"/track/1/barrier/1")
topic_barriers.append(group_ID+"/track/1/barrier/2")
                      
topic_barriers.append(group_ID+"/track/1/barrier/3")
topic_barriers.append(group_ID+"/track/1/barrier/4")
topic_barriers.append(group_ID+"/track/1/barrier/5")
topic_barriers.append(group_ID+"/track/1/barrier/6")
topic_barriers.append(group_ID+"/track/1/barrier/7")
topic_barriers.append(group_ID+"/track/1/barrier/8")

#west > oost boats
topic_barriers.append(group_ID+"/vessel/0/barrier/0")
topic_barriers.append(group_ID+"/vessel/0/barrier/1")
topic_barriers.append(group_ID+"/vessel/0/barrier/2")
topic_barriers.append(group_ID+"/vessel/0/barrier/3")
topic_barriers.append(group_ID+"/vessel/0/barrier/4")
topic_barriers.append(group_ID+"/vessel/0/barrier/5")
topic_barriers.append(group_ID+"/vessel/0/barrier/6")
topic_barriers.append(group_ID+"/vessel/0/barrier/7")


#oost > west boats
topic_barriers.append(group_ID+"/vessel/1/barrier/0")
topic_barriers.append(group_ID+"/vessel/1/barrier/1")
topic_barriers.append(group_ID+"/vessel/1/barrier/2")
topic_barriers.append(group_ID+"/vessel/1/barrier/3")
topic_barriers.append(group_ID+"/vessel/1/barrier/4")
topic_barriers.append(group_ID+"/vessel/1/barrier/5")
topic_barriers.append(group_ID+"/vessel/1/barrier/6")
topic_barriers.append(group_ID+"/vessel/1/barrier/7")

#traffic lights
topic_trafficlights = list()
#traffic light for cars
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
topic_trafficlights.append(group_ID+"/cycle/3/0/traffic_light/0")
topic_trafficlights.append(group_ID+"/cycle/3/1/traffic_light/0")
topic_trafficlights.append(group_ID+"/cycle/4/traffic_light/0")
topic_trafficlights.append(group_ID+"/cycle/5/traffic_light/0")
#traffic light for foot
topic_trafficlights.append(group_ID+"/foot/0/0/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/0/1/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/1/0/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/1/1/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/2/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/3/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/4/0/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/4/1/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/4/2/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/5/0/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/5/1/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/5/2/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/6/traffic_light/0")
topic_trafficlights.append(group_ID+"/foot/7/traffic_light/0")


#warning lights
topic_warninglights = list()
topic_warninglights.append(group_ID+"/track/0/warning_light/0")
topic_warninglights.append(group_ID+"/track/1/warning_light/0")
topic_warninglights.append(group_ID+"/vessel/0/warning_light/0")
topic_warninglights.append(group_ID+"/vessel/1/warning_light/0")

#sensors
topic_sensors = list()
#sensor for cars
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
#sensor for train
topic_sensors.append(group_ID+"/track/0/sensor/0")
topic_sensors.append(group_ID+"/track/1/sensor/0")
#sensor for boat
topic_sensors.append(group_ID+"/vessel/0/sensor/0")
topic_sensors.append(group_ID+"/vessel/1/sensor/0")
#sensor for bike
topic_sensors.append(group_ID+"/cycle/0/sensor/0")
topic_sensors.append(group_ID+"/cycle/1/sensor/0")
topic_sensors.append(group_ID+"/cycle/2/sensor/0")
topic_sensors.append(group_ID+"/cycle/3/0/sensor/0")
topic_sensors.append(group_ID+"/cycle/3/1/sensor/0")
topic_sensors.append(group_ID+"/cycle/4/sensor/0")
topic_sensors.append(group_ID+"/cycle/5/sensor/0")
#sensor for foot
topic_sensors.append(group_ID+"/foot/0/0/sensor/0")
topic_sensors.append(group_ID+"/foot/0/1/sensor/0")
topic_sensors.append(group_ID+"/foot/1/0/sensor/0")
topic_sensors.append(group_ID+"/foot/1/1/sensor/0")
topic_sensors.append(group_ID+"/foot/2/sensor/0")
topic_sensors.append(group_ID+"/foot/3/sensor/0")
topic_sensors.append(group_ID+"/foot/4/0/sensor/0")
topic_sensors.append(group_ID+"/foot/4/1/sensor/0")
topic_sensors.append(group_ID+"/foot/4/2/sensor/0")
topic_sensors.append(group_ID+"/foot/5/0/sensor/0")
topic_sensors.append(group_ID+"/foot/5/1/sensor/0")
topic_sensors.append(group_ID+"/foot/5/2/sensor/0")
topic_sensors.append(group_ID+"/foot/6/sensor/0")
topic_sensors.append(group_ID+"/foot/7/sensor/0")










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
