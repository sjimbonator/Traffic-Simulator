package simulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Simulation extends JPanel implements MqttCallback {

    private ArrayList<WorldObject> worldObjects = new ArrayList();
    private Image carImage;
    private Image background;
    private Image trafficLight;
    private static String mqttmessage;

    // variabelen voor mqtt subscribe
    private static final String brokerUrl = "tcp://arankieskamp.com:1883";
    private static final String clientId = "Groep7Subscribe";
    private static ArrayList<String> topic_trafficlights = new ArrayList<String>();

    // variabelen voor mqtt publish
    private static String topic = "7/motorised/1/sensor/1";
    private static String sensorPayload = "0";   //placeholder payload of sensor to publish via mqtt
    private int qos = 1;
    private String broker = "tcp://arankieskamp.com:1883";
    private String publishClientId = "Groep7Publish";
    MemoryPersistence persistence = new MemoryPersistence();

    public void publish(String topic, String content) {
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

    public void subscribe(ArrayList<String> topic_trafficlights) {
        //logger file name and pattern to log
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

    public Simulation() {
        try {
            carImage = ImageIO.read(new File("C:\\Users\\Startklaar\\Pictures\\car.png"));
            background = ImageIO.read(new File("C:\\Users\\Startklaar\\Pictures\\crossing.png"));
            trafficLight = ImageIO.read(new File("C:\\Users\\Startklaar\\Pictures\\trafficlight.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addObjects() {
        worldObjects.add(new TrafficLight(720, 520, 0, trafficLight));
    }

    public void update() {
        int random = (int) (Math.random() * 200 + 1);
        if (random == 10) {
            worldObjects.add(new Car(1800, 540, 300, carImage));
            //worldObjects.add(new TrafficLight(720, 520, 0, trafficLight));
        }

        for (WorldObject object : worldObjects) {
            object.update(worldObjects);
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        AffineTransform xform = new AffineTransform();
        g2.drawImage(background, xform, this);
        for (WorldObject object : worldObjects) {
            if (object.getType() == "Car") {
                xform.setToTranslation(object.getX() - (object.getImage().getWidth(this) / 2), object.getY() - (object.getImage().getHeight(this) / 2));
                //xform.setToTranslation(object.getX(), object.getY());
                //xform.rotate(Math.toRadians(object.getRotation()));
                xform.rotate(Math.toRadians(270), object.getImage().getWidth(this) / 2, object.getImage().getHeight(this) / 2);
                //xform.rotate(Math.toRadians(270));
                g2.drawImage(carImage, xform, this);
            }
            if (object.getType() == "trafficLight") {
                xform.setToTranslation(object.getX() - (object.getImage().getWidth(this) / 2), object.getY() - (object.getImage().getHeight(this) / 2));
                //xform.setToTranslation(object.getX(), object.getY());
                xform.rotate(Math.toRadians(object.getRotation()));
                xform.scale(1.5, 1.5);
                g2.drawImage(trafficLight, xform, this);
            }

        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1920, 1080);
    }

    public static void main(String args[]) {
        System.out.println("Subscriber running");

        JFrame frame = new JFrame("Traffic Jam Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        Simulation sim = new Simulation();
        sim.subscribe(topic_trafficlights);
        sim.publish(topic, sensorPayload);
        frame.setContentPane(sim);
        frame.pack();
        frame.setVisible(true);
        sim.addObjects();
        while (true) {
            try {
                sim.update();
                Thread.sleep(17);
            } catch (InterruptedException ex) {
                Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
