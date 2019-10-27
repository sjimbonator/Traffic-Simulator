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
import java.awt.geom.Point2D;
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
    private ArrayList<ArrayList<Point2D>> carRoutes = new ArrayList();
    
    private Image carImage;
    private Image background;
    
    private Image red;
    private Image orange;
    private Image green;
    private Image white;
    
    //mqtt subscribe variables
    private static final String brokerUrl = "tcp://arankieskamp.com:1883";
    private static final String clientId = "Groep7Subscribe";
    private static ArrayList<String> topic_trafficlights = new ArrayList<String>();
    private static String mqttmessage;
    
    //mqtt publish variables
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
            carImage = ImageIO.read(new File("./car.png"));
            background = ImageIO.read(new File("./BACKGROUNDarrows.png"));
            
            red = ImageIO.read(new File("./red.png"));
            orange = ImageIO.read(new File("./orange.png"));
            green = ImageIO.read(new File("./green.png"));
            white = ImageIO.read(new File("./white.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //Filling the carRoutes ArrayList
        ArrayList<Point2D> route1 = new ArrayList();
        route1.add(new Point2D.Double(470,5));
        route1.add(new Point2D.Double(470,190));
        route1.add(new Point2D.Double(470,330));
        route1.add(new Point2D.Double(0,330));
        carRoutes.add(route1);
        
        //Creating traffic lights
        
        //North
        TrafficLight light0 = new TrafficLight(702, 470, 180, red, orange, green, white );
        worldObjects.add(light0);
        TrafficLight light1 = new TrafficLight(702, 421, 270, red, orange, green, white );
        worldObjects.add(light1);
        TrafficLight light2 = new TrafficLight(702, 342, 270, red, orange, green, white );
        worldObjects.add(light2);
        TrafficLight light3 = new TrafficLight(702, 293, 0, red, orange, green, white );
        worldObjects.add(light3);
        
        //East
        TrafficLight light4 = new TrafficLight(588, 698, 90, red, orange, green, white );
        worldObjects.add(light4);
        TrafficLight light5 = new TrafficLight(509, 698, 270, red, orange, green, white );
        worldObjects.add(light5);
        
        //South
        TrafficLight light6 = new TrafficLight(202, 505, 90, red, orange, green, white );
        worldObjects.add(light6);
        TrafficLight light7 = new TrafficLight(202, 584, 90, red, orange, green, white );
        worldObjects.add(light7);
        TrafficLight light8 = new TrafficLight(202, 438, 0, red, orange, green, white );
        worldObjects.add(light8);
        
        //West
        TrafficLight light9 = new TrafficLight(538, 202, 90, red, orange, green, white );
        worldObjects.add(light9);
        TrafficLight light10 = new TrafficLight(460, 202, 270, red, orange, green, white );
        worldObjects.add(light10);
        
        
    }

    public void update() {
        int random = (int) (Math.random() * 100 + 1);
        if (random == 10) {
            worldObjects.add(new Car(carRoutes.get(0), carImage));
        }
        ArrayList<WorldObject> deleteList = new ArrayList();
        for (WorldObject object : worldObjects) {
            if(object.update(worldObjects)) {deleteList.add(object);}
        }
        worldObjects.removeAll(deleteList);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform xform = new AffineTransform();
        g2.drawImage(background, xform, this);
        for (WorldObject object : worldObjects) {
            xform.setToTranslation((int) object.getX() - (object.getImage().getWidth(this) / 2), (int) object.getY() - (object.getImage().getHeight(this) / 2));
            double offset = (object.getImage().getWidth(this) - object.getImage().getHeight(this));
            xform.rotate(Math.toRadians((int) object.getRotation()), (object.getImage().getWidth(this) / 2), (object.getImage().getHeight(this) / 2));
            xform.translate(-offset, -offset);
            g2.drawImage(object.getImage(), xform, this);

        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1311, 900);
    }

    public static void main(String args[]) {
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
