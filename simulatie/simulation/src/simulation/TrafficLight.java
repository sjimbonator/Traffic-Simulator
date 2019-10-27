package simulation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class TrafficLight implements WorldObject {
    
    MqttSubscriber mqttsub = new MqttSubscriber();
    private int x;
    private int y;
    private Image red;
    private Image orange;
    private Image green;
    private Image white;
    private int rotation;
    private String color;
    private static String mqttMessage;
    
    public TrafficLight(int x, int y, int rotation, Image red, Image orange, Image green, Image white) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;

        this.red = red;
        this.orange = orange;
        this.green = green;
        this.white = white;
    }

    //function used to find color of the trafficlight for the cars
    public String getColor() {
        return color;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public Image getImage() {
        if (mqttMessage != null && mqttMessage.contains("0")) {
            color = "red";
            return red;
        }else if(mqttMessage != null && mqttMessage.contains("1")){
            color = "orange";
            return orange;
        }else if(mqttMessage != null && mqttMessage.contains("2")) {
            color = "green";
            return green;
        }else if(mqttMessage != null && mqttMessage.contains("3")) {
            return white;
        } else{
            return white;
        }
    }

    @Override
    public boolean update(ArrayList<WorldObject> worldObjects) {
        mqttMessage = mqttsub.getMessage();
        return false;
    }

    @Override
    public String getType() {
        return "trafficLight";
    }

    @Override
    public double getRotation() {
        return rotation;
    }
}
