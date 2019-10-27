/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublisher {

    private static String topic;
    private static String content;
    private static String sensorPayload = "0";   //placeholder payload of sensor to publish via mqtt
    private int qos = 1;
    private String broker = "tcp://arankieskamp.com:1883";
    private String publishClientId = "Groep7Publish";
    MemoryPersistence persistence = new MemoryPersistence();

    public void publish(String topic, String content) {
        this.topic = topic;
        this.content = content;
        
        try {
            MqttClient sampleClient = new MqttClient(broker, publishClientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected to broker");
            System.out.println("Publishing message:" + this.content);
            MqttMessage message = new MqttMessage(this.content.getBytes());
            message.setQos(qos);
            sampleClient.publish(this.topic, message);
            System.out.println("Message published");
            sampleClient.disconnect();
            sampleClient.close();
            //System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}
