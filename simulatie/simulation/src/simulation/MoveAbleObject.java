package simulation;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public abstract class MoveAbleObject implements DrawAbleObject {

    protected double x;
    protected double y;
    protected Rectangle2D hitbox;
    protected double rotation;
    protected double speed = 0;
    protected double acceleration;
    protected double maxSpeed;
    protected String type;
    protected int checkRange;
    protected int width;
    protected int height;
    protected ArrayList<Point2D> route;
    protected Point2D destination;
    protected int routeIndex = 0;
    protected boolean pointReached = true;

    //public Rectangle2D predictbox;
    protected Image model;

    protected Rectangle2D buildRect(double x, double y) {
        Point2D[] points = new Point2D[4];
        points[0] = rotatePoint(new Point2D.Double((x - (width / 2)), (y + (height / 2))));
        points[1] = rotatePoint(new Point2D.Double((x - (width / 2)), (y - (height / 2))));
        points[2] = rotatePoint(new Point2D.Double((x + (width / 2)), (y + (height / 2))));
        points[3] = rotatePoint(new Point2D.Double((x + (width / 2)), (y - (height / 2))));
        double[] minMaxX = getMinMax(points, 'x');
        double[] minMaxY = getMinMax(points, 'y');
        Rectangle2D rect = new Rectangle((int) minMaxX[0], (int) minMaxY[0], (int) ((minMaxX[1] - minMaxX[0])), (int) ((minMaxY[1] - minMaxY[0])));
        return rect;

    }

    protected void buildHitBox() {
        hitbox = buildRect(x, y);
    }

    protected Rectangle2D predictHitbox() {
        double xTemp = x;
        double yTemp = y;
        double speedTemp = speed;
        speed = maxSpeed;
        for (int i = 0; i < 20; i++) {
            move();
        }
        Rectangle2D temprect = buildRect(x, y);
        x = xTemp;
        y = yTemp;
        speed = speedTemp;
        //predictbox = temprect;
        return temprect;
    }

    protected Point2D rotatePoint(Point2D p) {
        double radian = Math.toRadians(rotation);
        double s = Math.sin(radian);
        double c = Math.cos(radian);

        double xTemp = p.getX() - x;
        double yTemp = p.getY() - y;

        double newX = xTemp * c - yTemp * s;
        double newY = xTemp * s + yTemp * c;

        newX += x;
        newY += y;

        return new Point2D.Double(newX, newY);
    }

    protected void move() {
        y += speed * Math.cos((Math.toRadians(rotation)));
        x += speed * -Math.sin((Math.toRadians(rotation)));
    }

    protected void accelerate() {
        if (speed < maxSpeed) {
            speed += acceleration;
        }
        if (speed > maxSpeed) {
            speed = maxSpeed;
        }
    }

    protected void turn() {
        destination = route.get(routeIndex);
        rotation = Math.toDegrees(Math.atan2((x - destination.getX()), -(y - destination.getY())));
        System.out.print(rotation + "ROTATE--------------");
        pointReached = false;

    }

    protected void deccelerate() {
        speed -= acceleration * 5;
        if (speed < 0) {
            speed = 0;
        }
    }

    protected double[] getMinMax(Point2D[] hitbox, char index) {
        double[] minmax = null;
        for (Point2D point : hitbox) {

            double p;
            if (index == 'x') {
                p = point.getX();
            } else {
                p = point.getY();
            }

            if (minmax == null) {
                minmax = new double[2];
                minmax[0] = p;
                minmax[1] = p;
                continue;
            }
            if (p < minmax[0]) {
                minmax[0] = p;
            } else if (p > minmax[1]) {
                minmax[1] = p;
            }
        }
        return minmax;
    }
    
    protected String CheckRotation()
    {
        String returnString = "";
        double temprotation = rotation;
        if(temprotation < 0){temprotation += 360;}
        if((temprotation >= 315 && temprotation <= 360) || (temprotation >= 0 && temprotation <= 45)){returnString = "down";}
        if(temprotation >= 45 && temprotation <= 135){returnString = "left";}
        if(temprotation >= 135 && temprotation <= 225){returnString = "up";}
        if(temprotation >= 225 && temprotation <= 315){returnString = "right";}
        
        return returnString;
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
        buildHitBox();
        move();
        //Rectangle2D predictBox = predictHitbox();
        
        for (DrawAbleObject object : worldObjects) {
            if (!(object == this)) {
                if (object instanceof TrafficLight) {
                    
                    if (((TrafficLight) object).getColor() == "green" || ((TrafficLight) object).getType() != this.type || (!(CheckRotation() ==(((TrafficLight) object).getDirection())))) {
                        continue;
                    }
                } else if (object instanceof Barrier) {
                    if (!(this.type == "train")) {
                        if (!(((Barrier) object).isActive())) {
                            continue;
                        }
                    }
                }
                else if (object instanceof MoveAbleObject) {
                    Rectangle2D objHitbox = ((MoveAbleObject) object).getHitbox();

                    if (predictHitbox().intersects(objHitbox)) {
                        deccelerate();
                        return false;
                    }
                    continue;

                }
                double objX = object.getX();
                double objY = object.getY();

                double xCheckRange = checkRange * -Math.sin((Math.toRadians(rotation)));
                double yCheckRange = checkRange * Math.cos((Math.toRadians(rotation)));

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
        if(!Simulation.openBridge && this.type == "boat"){
            if(y >= 250 && y <= 750 ){deccelerate();}
        }
        double yTemp = y + (speed * Math.cos((Math.toRadians(rotation) * 13)));
        double xTemp = x + (speed * -Math.sin((Math.toRadians(rotation) * 13)));
        if(Simulation.warningBoat && this.type != "boat"){
            if( !((x >= 1025 && x <= 1260 ) && (y >= 328 && y <= 674 )) && ((xTemp >= 1025 && xTemp <= 1260 ) && (yTemp >= 328 && yTemp <= 674 )) ){deccelerate();}
            //else{accelerate();}
        }
        if(Simulation.warningTrain && this.type != "train"){
            if( !((x >= 295 && x <= 390 ) && (y >= 215 && y <= 685 )) && ((xTemp >= 295 && xTemp <= 390 ) && (yTemp >= 215 && yTemp <= 685 )) ){deccelerate();}
            //else{accelerate();}
        }
        return false;
    }

    public Rectangle2D getHitbox() {
        return hitbox;
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
