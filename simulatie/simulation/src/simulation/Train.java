package simulation;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Train extends MoveAbleObject {
    
    public Train(ArrayList<Point2D> route, Image model) {
        super(route, model);
        type = "train";
        acceleration = 0.08;
        maxSpeed = 0.8;
        
    }
    

}
