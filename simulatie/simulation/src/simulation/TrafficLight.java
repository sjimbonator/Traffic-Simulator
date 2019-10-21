package simulation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class TrafficLight implements WorldObject {
    private double x;
    private double y;
    private String color;
    private Image model;
    private double rotation;
    
    private void color(String color)
    {
        this.color = color;
    }
    
    public TrafficLight(int x, int y, int rotation, Image model){
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.model = model;
        color = "red";
    }
           
    @Override
    public int getX() {
        return (int) x;
    }

    @Override
    public int getY() {
        return (int) y;
    }
    
    @Override
    public Image getImage() {
        return model;
    }

    @Override
    public void update(ArrayList<WorldObject> worldObjects) {
        color(color);
    }

    @Override
    public String getType() {
        return "trafficLight";
    }

    @Override
    public int getRotation() {
        return (int) rotation;
    }
}