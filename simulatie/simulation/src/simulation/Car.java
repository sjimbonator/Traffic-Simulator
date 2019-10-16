package simulation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public class Car implements WorldObject {

    private int x;
    private int y;
    private int rotation;
    private int speed;

    private Image model;

    private void move() {
        y += speed * Math.cos(rotation);
        x += speed * Math.sin(rotation);
    }

    public Car(int x, int y, int rotation, Image model) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.speed = 0;
        
        this.model = model;

    }

    public void update(ArrayList<WorldObject> worldObjects) {
            
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform xform = new AffineTransform();
        xform.setToTranslation(x -(model.getWidth( (ImageObserver) this) / 2) , y-(model.getHeight( (ImageObserver) this) / 2));
        xform.rotate(Math.toRadians(rotation), x - (model.getWidth( (ImageObserver) this) / 2), y - (model.getHeight( (ImageObserver) this) / 2));
        g.drawImage(model, xform, (ImageObserver) this);
    }

    @Override
    public ArrayList<Integer> getPos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
