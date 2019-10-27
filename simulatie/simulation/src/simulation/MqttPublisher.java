package simulation;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublisher {

    //private static String topic;
    //private static String content;
    //private static String sensorPayload = "0";   //placeholder payload of sensor to publish via mqtt
    private int qos = 1;
    private static String broker = "tcp://arankieskamp.com:1883";
    private static String publishClientId = "Groep7Publish";
    private String topic;
    MemoryPersistence persistence = new MemoryPersistence();
    
    public MqttPublisher(String topic)
    {
        this.topic = topic;
        publishClientId += "I";
    }
    
    public void publish(String content) {
        try {
            MqttClient sampleClient = new MqttClient(broker, publishClientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected to broker");
            System.out.println("Publishing message:" + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");
            sampleClient.disconnect();
            sampleClient.close();
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
