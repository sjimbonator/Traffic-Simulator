package simulation;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Car extends MoveAbleObject {
    
    public Car(ArrayList<Point2D> route, Image model) {
        super(route, model);
        type = "car";
        acceleration = 0.01;
        maxSpeed = 1;
        checkRange = 50;
        width = 18;
        height = 36;
        buildHitBox();
        
    }
    

}
