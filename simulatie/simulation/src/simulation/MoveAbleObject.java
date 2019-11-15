package simulation;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public abstract class MoveAbleObject implements DrawAbleObject {

    protected double x;
    protected double y;
    protected double rotation;
    protected double speed = 0;
    protected double acceleration;
    protected double maxSpeed;
    protected String type;
    protected ArrayList<Point2D> route;
    protected Point2D destination;
    protected int routeIndex = 0;
    protected boolean pointReached = true;

    protected Image model;

    protected void move() {
        y += speed * Math.cos((Math.toRadians(rotation)));
        x += speed * -Math.sin((Math.toRadians(rotation)));
    }

    protected void accelerate() {
        if (!(speed > maxSpeed)) {
            speed += acceleration;
        }
        else {speed = maxSpeed;}
    }

    protected void turn() {
        destination = route.get(routeIndex);
        rotation = Math.toDegrees(Math.atan2((x - destination.getX()), -(y - destination.getY())));
        pointReached = false;
    }

    protected void deccelerate() {
        speed -= acceleration * 5;
        if (speed < 0) {
            speed = 0;
        }
    }

    public MoveAbleObject(ArrayList<Point2D> route, Image model) {
        Point2D spawn = route.get(0);
        this.x = spawn.getX();
        this.y = spawn.getY();
        this.rotation = 0;
        this.route = route;
        this.model = model;

        destination = route.get(routeIndex);

    }

    @Override
    public boolean update(ArrayList<DrawAbleObject> worldObjects) {
        move();
        if(!(type == "train")){
            for (DrawAbleObject object : worldObjects) {
                if (!(object == this)) {
                    if (object instanceof TrafficLight) {

                        if ( ((TrafficLight) object).getColor() == "green" || ((TrafficLight) object).getType() != this.type) {
                            continue;
                        }
                    }
                    else if (object instanceof Barrier){
                        if ( !(((Barrier) object).isActive())) {
                            continue;
                        }
                    }
                    double objX = object.getX();
                    double objY = object.getY();

                    double xCheckRange = 50 * -Math.sin((Math.toRadians(rotation)));
                    double yCheckRange= 50 * Math.cos((Math.toRadians(rotation)));

                    double minX = Math.min(x, x + xCheckRange);
                    double maxX = Math.max(x, x + xCheckRange);
                    if((maxX - minX) <40){
                        minX = x - 20;
                        maxX = x + 20;
                    }

                    double minY = Math.min(y, y + yCheckRange);
                    double maxY = Math.max(y, y + yCheckRange);
                    if((maxY - minY) <40){
                        minY = y - 20;
                        maxY = y + 20;
                    }

                    if ((objX >= (minX) && objX <= (maxX)) && (objY >= (minY) && objY <= (maxY))) {                
                        deccelerate();
                        return false;
                    }
                }

            }
        }
        if ((destination.getX() >= x - speed && destination.getX() <= x + speed) && (destination.getY() >= y - speed && destination.getY() <= y + speed)) {
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
        return type;
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


