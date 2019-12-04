package simulation;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublisher implements Runnable {
    private int qos = 1;
    private String topic;
    private String content;
    private MqttClient publishclient;
    
    public MqttPublisher(String topic, String content, MqttClient publishclient)
    {
        this.topic = topic;
        this.content = content;
        this.publishclient = publishclient;
    }

    @Override
    public void run() {
        try {
            
            System.out.println("Publishing message:" + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            publishclient.publish(topic, message);
            System.out.println("Message published");
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
