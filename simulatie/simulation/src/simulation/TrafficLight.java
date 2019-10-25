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
    private int x;
    private int y;
    private String mqttmessage;
    private String color;
    private Image model;
    private int rotation;

    public TrafficLight(int x, int y, int rotation, Image model, String mqttmessage){
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.model = model;
        this.mqttmessage = mqttmessage;
        
    }
    
    private void changeColor (){
        if(mqttmessage != null && mqttmessage.contains("0")){
            
            try {
                model = ImageIO.read(new File("C:\\Users\\Startklaar\\Pictures\\car1.png"));
                System.out.println("color is red");
                

            } catch (IOException e) {
                e.printStackTrace();
            }
            color = "red";
            
        }
        
        if (mqttmessage != null && mqttmessage.contains("1")){
            try {
                model = ImageIO.read(new File("C:\\Users\\Startklaar\\Pictures\\trafficlight.png"));
                System.out.println("color is orange");

            } catch (IOException e) {
                e.printStackTrace();
            }
            color = "orange";
        }
        if (mqttmessage != null && mqttmessage.contains("2")){
            try {
                model = ImageIO.read(new File("C:\\Users\\Startklaar\\Pictures\\trafficlight.png"));
                System.out.println("color is green");

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
        changeColor();
        System.out.println(mqttmessage);
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