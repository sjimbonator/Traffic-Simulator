package simulation;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Pedestrian extends MoveAbleObject {

    public Pedestrian(ArrayList<Point2D> route, Image model) {
        super(route, model);
        type = "pedestrian";
        acceleration = 1;
        maxSpeed = 0.4;

    }
}
