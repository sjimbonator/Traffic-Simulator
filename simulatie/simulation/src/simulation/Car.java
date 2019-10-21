package simulation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class Car implements WorldObject {

    private ArrayList<WorldObject> worldObjects = new ArrayList();
    private double x;
    private double y;
    private double rotation;
    private double speed;

    private Image model;

    private void move() {
        y += speed * Math.cos(rotation);
        x += speed * Math.sin(rotation);
        //System.out.print(y);
    }
    

    private void accelerate() {
        if (!(speed > 10.0)){
            speed += 0.01;
        }
        
    }

    private void deccelerate() {
        
        speed = 0;
    }

    public Car(int x, int y, int rotation, Image model) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.speed = 5;

        this.model = model;

    }

    @Override
    public void update(ArrayList<WorldObject> worldObjects) {
        accelerate();
        move();
       // for (WorldObject object : worldObjects) {
         //   if(object.getType() == "trafficLight"){
         //      if((int)y == object.getY()){
         //           deccelerate();
        //        }
        //    }
        //}
    }


    @Override
    public String getType() {
        return "Car";
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
    public int getRotation() {
        return (int) rotation;
    }

    @Override
    public Image getImage() {
        return model;
    }

}
