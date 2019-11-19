package simulation;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Train extends MoveAbleObject {
    
    public Train(ArrayList<Point2D> route, Image model) {
        super(route, model);
        type = "train";
        acceleration = 0.13;
        maxSpeed = 1.3;
        checkRange = 0;
        width = 40;
        height = 400;
        buildHitBox();
        
    }
    

}
