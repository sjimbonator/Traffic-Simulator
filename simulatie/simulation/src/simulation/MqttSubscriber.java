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
    private static ArrayList<String> topic_trafficlights = new ArrayList<String>();
    private static String mqttmessage;

    public void subscribe(ArrayList<String> topic) {
        MemoryPersistence persistence = new MemoryPersistence();
        for (int i = 0; i < topic.size(); i++) {
            try {

                MqttClient sampleClient = new MqttClient(brokerUrl, clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(false);

                System.out.println("Mqtt connected to broker: " + brokerUrl);
                sampleClient.connect(connOpts);
                sampleClient.setCallback(this);
                sampleClient.subscribe(topic.get(i));

                System.out.println("Subscribed to " + topic.get(i));
                System.out.println("Listening");

            } catch (MqttException e) {
                System.out.println(e);
            }
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
        topic_trafficlights.add("7/motorised/1/traffic_light/1");
        topic_trafficlights.add("7/motorised/1/traffic_light/2");
        topic_trafficlights.add("7/motorised/1/traffic_light/3");
        topic_trafficlights.add("7/motorised/1/traffic_light/4");
        topic_trafficlights.add("7/motorised/1/traffic_light/5");
        topic_trafficlights.add("7/motorised/1/traffic_light/6");
        topic_trafficlights.add("7/motorised/1/traffic_light/7");
        topic_trafficlights.add("7/motorised/1/traffic_light/8");
        topic_trafficlights.add("7/motorised/1/traffic_light/9");
        topic_trafficlights.add("7/motorised/1/traffic_light/10");
        topic_trafficlights.add("7/motorised/1/traffic_light/11");
        
        System.out.println("Subscriber running");
        new MqttSubscriber().subscribe(topic_trafficlights);
    }
}
