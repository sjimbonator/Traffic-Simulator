package simulation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class Car implements WorldObject {

    private double x;
    private double y;
    private double rotation;
    private double speed;

    private Image model;

    private void move() {
        System.out.println(rotation);
        y += speed * Math.cos((Math.toRadians(rotation)));
        x += speed * -Math.sin((Math.toRadians(rotation)));
        System.out.println("Y+=");
        System.out.println(Math.cos(Math.toRadians(rotation)));
        System.out.println("X+=");
        System.out.println(Math.sin(Math.toRadians(rotation)));
    }

    private void accelerate() {
        if (!(speed > 3.0)){
            speed += 0.01;
        }
        
    }

    private void deccelerate() {
        speed -= 1;
    }

    public Car(double x, double y, double rotation, Image model) {
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
