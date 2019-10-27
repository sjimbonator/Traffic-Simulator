package simulation;
import java.util.ArrayList;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttSubscriber implements MqttCallback {
    
    private final String brokerUrl = "tcp://arankieskamp.com:1883";
    private static String clientId = "Groep7Subscribe";
    private String mqttmessage;
    private String topic;
    
    private void subscribe() {
        MemoryPersistence persistence = new MemoryPersistence();
            try {
                System.out.println(clientId);
                MqttClient sampleClient = new MqttClient(brokerUrl, clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(false);

                System.out.println("Mqtt connected to broker: " + brokerUrl);
                sampleClient.connect(connOpts);
                sampleClient.setCallback(this);
                sampleClient.subscribe(topic);

                System.out.println("Subscribed to " + topic);
                System.out.println("Listening");

            } catch (MqttException e) {
                System.out.println(e);
            }
    }
    
    public MqttSubscriber(String topic)
    {
        this.topic = topic;
        clientId += "I";
        subscribe();
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
    
    public String getMessage() {
        return mqttmessage;
    }


}
