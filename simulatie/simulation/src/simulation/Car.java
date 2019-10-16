package simulation;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class Car implements WorldObject {
    private int x;
    private int y;
    private int rotation;
    private int speed;
    
    private void move(){
        y += speed * Math.cos(rotation);
        x += speed * Math.sin(rotation);
    }
    
    public Car(int x, int y, int rotation){
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.speed = 0;
    
    }
    
    public void update(ArrayList<WorldObject> worldObjects){
        
    
    }
  

    @Override
    public void draw(Graphics2D g) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Integer> getPos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
