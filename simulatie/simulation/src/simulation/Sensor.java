package simulation;

import java.util.ArrayList;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Sensor {

    private double x;
    private double y;
    private String topic;
    private MqttClient publishclient;

    private int payload = 0;

    public Sensor(String topic, double x, double y, String type) {
        this.x = x;
        this.y = y;
        this.topic = topic;
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            String broker = "tcp://arankieskamp.com:1883";
            publishclient = new MqttClient(broker, "7Publisher" + topic, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            publishclient.connect(connOpts);
            System.out.println("Connected to broker");

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }

    }

    public void update(ArrayList<DrawAbleObject> worldObjects) {
        int tempPayload = 0;
        for (DrawAbleObject object : worldObjects) {
            double objX = object.getX();
            double objY = object.getY();
            if (((object.getType() == this.type) && objX >= (x - 30) && objX <= (x + 30)) && (objY >= (y - 30) && objY <= (y + 30))) {
                tempPayload = 1;
            }
        }
        if (tempPayload != payload) {
            payload = tempPayload;
            MqttPublisher publisher = new MqttPublisher(topic, ("" + payload), publishclient);
            Thread t = new Thread(publisher);
            t.start();
        };
    }
}
