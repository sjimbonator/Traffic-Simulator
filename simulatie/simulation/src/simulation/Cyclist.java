package simulation;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Cyclist extends MoveAbleObject {

    public Cyclist(ArrayList<Point2D> route, Image model) {
        super(route, model);
        type = "cyclist";
        acceleration = 0.15;
        maxSpeed = 0.8;
        checkRange = 10;
        width = 10;
        height = 22;
        buildHitBox();

    }
}
