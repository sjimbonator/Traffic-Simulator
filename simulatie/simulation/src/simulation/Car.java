package simulation;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Car extends MoveAbleObject {
    
    public Car(ArrayList<Point2D> route, Image model) {
        super(route, model);
        type = "car";
        acceleration = 0.2;
        maxSpeed = 1.4;
        checkRange = 50;
        width = 16;
        height = 34;
        buildHitBox();
        
    }
    

}
