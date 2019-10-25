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
    private double x;
    private double y;
    private String color;
    private Image model;
    private double rotation;
    
    private void color(String color)
    {
        this.color = color;
    }
    
    private void changeRed (){
        try {
            model = ImageIO.read(new File("C:\\Users\\Startklaar\\Pictures\\trafficlight.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void changeYellow (){
        try {
            model = ImageIO.read(new File("C:\\Users\\Startklaar\\Pictures\\trafficlight.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void changeGreen (){
        try {
            model = ImageIO.read(new File("C:\\Users\\Startklaar\\Pictures\\trafficlight.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
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