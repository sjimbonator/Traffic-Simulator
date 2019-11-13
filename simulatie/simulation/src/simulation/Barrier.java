package simulation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Barrier implements DrawAbleObject {

    MqttSubscriber mqttsub;
    private double x;
    private double y;
    private Image barrierOn;
    private Image barrierOff;
    private int rotation;
    private String topic;
    private String mqttMessage;
    private boolean active;
    
    public Barrier(String topic, double x, double y,Image barrierOff, Image barrierOn) {
        this.mqttsub = new MqttSubscriber(topic);
        this.x = x;
        this.y = y;
        this.topic = topic;
        this.barrierOff = barrierOff;
        this.barrierOn = barrierOn;
        rotation = 0;
    }
    
    public boolean isActive (){
        return active;
    }

    @Override
    public boolean update(ArrayList<DrawAbleObject> worldObjects) {
        mqttMessage = mqttsub.getMessage();
        return false;
    }

    @Override
    public String getType() {
        return "barrier";
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
    public double getRotation() {
        return rotation;
    }

    @Override
    public Image getImage() {
        if (mqttMessage != null && mqttMessage.contains("1")) {
            active = true;
            return barrierOn;
        }
        else {
            active = false;
            return barrierOff;
        }
}
}