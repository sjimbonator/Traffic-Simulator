
package simulation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

interface WorldObject  {
    public boolean update(ArrayList<WorldObject> worldObjects);
    public String getType();
    public double getX();
    public double getY();
    public double getRotation();
    public Image getImage();
}
