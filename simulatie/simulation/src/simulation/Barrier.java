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

    private String topic;
    private String payLoad;
    private double x;
    private double y;
    private Image barrierOn;
    private Image barrierOff;
    private int rotation;
    private boolean active;
    private boolean override;

    public Barrier(String topic, double x, double y, Image barrierOff, Image barrierOn) {
        this.topic = topic;
        this.x = x;
        this.y = y;
        this.topic = topic;
        this.barrierOff = barrierOff;
        this.barrierOn = barrierOn;
        rotation = 0;
    }

    public boolean isActive() {
        return active;
    }
    
    public Image barrierOn() {
        return barrierOn;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    @Override
    public boolean update(ArrayList<DrawAbleObject> worldObjects) {
        payLoad = MqttSubscriber.messages.get(topic);
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
        if (override) {
            active = true;
            return barrierOn;
        } else {
            if (payLoad != null && payLoad.contains("1")) {
                active = true;
                return barrierOn;
            } else {
                active = false;
                return barrierOff;
            }
        }

    }
}
