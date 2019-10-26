package simulation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class Car implements WorldObject {

    private double x;
    private double y;
    private double rotation;
    private double speed;
    private ArrayList<Point2D> route;
    Point2D destination;
    private int routeIndex = 0;
    private boolean pointReached = true;

    private Image model;

    private void move() {
        y += speed * Math.cos((Math.toRadians(rotation)));
        x += speed * -Math.sin((Math.toRadians(rotation)));
    }

    private void accelerate() {
        if (!(speed > 1.0)) {
            speed += 0.01;
        }
    }

    private void turn() {
        destination = route.get(routeIndex);
        rotation = Math.toDegrees(Math.atan2((x - destination.getX()), -(y - destination.getY())));
        pointReached = false;
    }

    private void deccelerate() {
        speed -= 0.05;
        if (speed < 0) {
            speed = 0;
        }
    }

    public Car(ArrayList<Point2D> route, Image model) {
        Point2D spawn = route.get(0);
        this.x = spawn.getX();
        this.y = spawn.getY();
        this.rotation = 0;
        this.speed = 0.5;
        this.route = route;
        this.model = model;

        destination = route.get(routeIndex);

    }

    @Override
    public boolean update(ArrayList<WorldObject> worldObjects) {
        move();

        for (WorldObject object : worldObjects) {
            if (!(object == this)) {
                if (object instanceof TrafficLight) {
                    if (((TrafficLight) object).getColor() == "green") {
                        break;
                    }
                }
                double objX = object.getX();
                double objY = object.getY();
                if ((objX >= (x - 50) && objX <= (x + 50)) && (objY >= (y - 50) && objY <= (y + 50))) {
                    double desy = y + speed * Math.cos((Math.toRadians(rotation)));
                    double desx = x + speed * -Math.sin((Math.toRadians(rotation)));

                    double desDifference = Math.pow((objY - desy), 2) + Math.pow((objX - desx), 2);
                    double actualDifference = Math.pow((objY - y), 2) + Math.pow((objX - x), 2);

                    if (actualDifference > desDifference) {
                        deccelerate();
                        return false;
                    }

                }
            }

        }

        if ((destination.getX() >= x - speed && destination.getX() <= x) && (destination.getY() >= y - speed && destination.getY() <= y)) {
            pointReached = true;
        }

        if (pointReached) {
            routeIndex += 1;
            if (routeIndex >= route.size()) {
                return true;
            } else {
                turn();
            }
        }
        accelerate();
        return false;
    }

    @Override
    public String getType() {
        return "Car";
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
        return model;
    }

}
