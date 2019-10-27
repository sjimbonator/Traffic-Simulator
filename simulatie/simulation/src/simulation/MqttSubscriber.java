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
    
    private static final String brokerUrl = "tcp://arankieskamp.com:1883";
    private static final String clientId = "Groep7Subscribe";
    private static String mqttmessage;
    private static String topic1 = "7/motorised/1/traffic_light/#";

    public void subscribe(String topic) {
        MemoryPersistence persistence = new MemoryPersistence();
            try {

                MqttClient sampleClient = new MqttClient(brokerUrl, clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);

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

    public static void main(String[] args) {
        System.out.println("Subscriber running");
        new MqttSubscriber().subscribe(topic1);
    }
}
