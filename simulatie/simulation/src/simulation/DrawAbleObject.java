
package simulation;

import java.awt.Image;
import java.util.ArrayList;

interface DrawAbleObject  {
    public boolean update(ArrayList<DrawAbleObject> worldObjects);
    public String getType();
    public double getX();
    public double getY();
    public double getRotation();
    public Image getImage();
}
