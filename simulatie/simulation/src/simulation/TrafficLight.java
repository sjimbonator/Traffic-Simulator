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
    
    Simulation sim = new Simulation();
    private int x;
    private int y;
    private static String mqttmessage;
    private String color;
    private Image model;
    private int rotation;

    public TrafficLight(int x, int y, int rotation, Image model){
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.model = model;
    }
    
    private void changeColor (){
        if(mqttmessage != null && mqttmessage.contains("0")){
            try {
                model = ImageIO.read(new File("C:\\Users\\Startklaar\\Pictures\\car1.png"));
                System.out.println("traffic light color is red");
                

            } catch (IOException e) {
                e.printStackTrace();
            }
            color = "red";
        }
        if (mqttmessage != null && mqttmessage.contains("1")){
            try {
                model = ImageIO.read(new File("C:\\Users\\Startklaar\\Pictures\\trafficlight.png"));
                System.out.println("traffic light color is orange");

            } catch (IOException e) {
                e.printStackTrace();
            }
            color = "orange";
        }
        if (mqttmessage != null && mqttmessage.contains("2")){
            try {
                model = ImageIO.read(new File("C:\\Users\\Startklaar\\Pictures\\trafficlight.png"));
                System.out.println("traffic light color is green");

            } catch (IOException e) {
                e.printStackTrace();
            }
            color = "green";
        }
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
        return model;
    }

    @Override
    public boolean update(ArrayList<WorldObject> worldObjects) {
        mqttmessage = sim.getMessage();
        changeColor();
        //System.out.println(mqttmessage);
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