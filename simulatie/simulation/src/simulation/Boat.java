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
        checkRange = 80;
        width = 21;
        height = 80;
        buildHitBox();

    }

}
