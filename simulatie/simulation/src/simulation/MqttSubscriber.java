/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import java.util.ArrayList;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author Kevin
 */
public class MqttSubscriber implements MqttCallback {
    //mqtt subscribe variables
    private static final String brokerUrl = "tcp://arankieskamp.com:1883";
    private static final String clientId = "Groep7Subscribe";
    private static ArrayList<String> topic_trafficlights = new ArrayList<String>();
    private static String mqttmessage;
    
    public void subscribe(ArrayList<String> topic_trafficlights) {
        MemoryPersistence persistence = new MemoryPersistence();
        topic_trafficlights.add("7/motorised/1/traffic_light/1");
        topic_trafficlights.add("7/motorised/1/traffic_light/2");
        topic_trafficlights.add("7/motorised/1/traffic_light/3");
        topic_trafficlights.add("7/motorised/1/traffic_light/4");
        topic_trafficlights.add("7/motorised/1/traffic_light/5");

        for (int i = 0; i < topic_trafficlights.size(); i++) {
            try {

                MqttClient sampleClient = new MqttClient(brokerUrl, clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(false);

                System.out.println("checking");
                System.out.println("Mqtt Connecting to broker: " + brokerUrl);

                sampleClient.connect(connOpts);
                System.out.println("Mqtt Connected");

                sampleClient.setCallback(this);
                sampleClient.subscribe(topic_trafficlights.get(i));

                System.out.println("Subscribed to " + topic_trafficlights.get(i));
                System.out.println("Listening");

            } catch (MqttException e) {
                System.out.println(e);
            }
        }
    }
    
    public String getMessage() {
        return mqttmessage;
    }

    //Called when the client lost the connection to the broker
    public void connectionLost(Throwable arg0) {

    }

    //Called when a outgoing publish is complete
    public void deliveryComplete(IMqttDeliveryToken arg0) {

    }

    public void messageArrived(String topic, MqttMessage mqttmessage) throws Exception {

        System.out.println("| Topic:" + topic);
        System.out.println("| Message: " + mqttmessage.toString());
        System.out.println("-------------------------------------------------");
        this.mqttmessage = mqttmessage.toString();

    }
    
    public static void main(String[] args) {

		System.out.println("Subscriber running");
		new MqttSubscriber().subscribe(topic_trafficlights);

	}
}
