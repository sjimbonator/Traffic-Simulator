package simulation;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Boat extends MoveAbleObject {

    public Boat(ArrayList<Point2D> route, Image model) {
        super(route, model);
        type = "boat";
        acceleration = 0.08;
        maxSpeed = 0.8;

    }

    @Override
    public boolean update(ArrayList<DrawAbleObject> worldObjects) {
        move();
        for (DrawAbleObject object : worldObjects) {
            if (!(object == this)) {
                double objX = object.getX();
                double objY = object.getY();

                double xCheckRange = 50 * -Math.sin((Math.toRadians(rotation)));
                double yCheckRange = 50 * Math.cos((Math.toRadians(rotation)));

                double minX = Math.min(x, x + xCheckRange);
                double maxX = Math.max(x, x + xCheckRange);
                if ((maxX - minX) < 40) {
                    minX = x - 20;
                    maxX = x + 20;
                }

                double minY = Math.min(y, y + yCheckRange);
                double maxY = Math.max(y, y + yCheckRange);
                if ((maxY - minY) < 40) {
                    minY = y - 20;
                    maxY = y + 20;
                }

                if ((objX >= (minX) && objX <= (maxX)) && (objY >= (minY) && objY <= (maxY))) {
                    deccelerate();
                    return false;
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
        
        if(!Simulation.openBridge){
            if(y >= 250 && y <= 750 ){deccelerate();}
        }
        return false;
    }

}
