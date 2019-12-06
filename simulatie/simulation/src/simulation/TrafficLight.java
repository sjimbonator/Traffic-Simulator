package simulation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class TrafficLight implements DrawAbleObject {
    
    MqttSubscriber mqttsub;
    
    private int x;
    private int y;
    private Image red;
    private Image orange;
    private Image green;
    private int rotation;
    private String color;
    private String payLoad;
    private String topic;
    private String type;
    
    public TrafficLight(String topic, int x, int y, int rotation, Image red, Image orange, Image green , String type) {
        this.topic = topic;
        
        this.x = x;
        this.y = y;
        this.rotation = rotation;

        this.red = red;
        this.orange = orange;
        this.green = green;
        
        this.type = type;
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
        if (payLoad != null && payLoad.contains("0")) {
            color = "red";
            return red;
        }else if(payLoad != null && payLoad.contains("1")){
            color = "orange";
            if(type == "boat" || type == "train"){color = "green";}
            return orange;
        }else if(payLoad != null && payLoad.contains("2")) {
            color = "green";
            return green;
        }else{
            return red;
        }
    }

    @Override
    public boolean update(ArrayList<DrawAbleObject> worldObjects) {
        payLoad = MqttSubscriber.messages.get(topic);
        return false;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public double getRotation() {
        return rotation;
    }
}
