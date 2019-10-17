
package simulation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

interface WorldObject  {
    public void update(ArrayList<WorldObject> worldObjects);
    public String getType();
    public int getX();
    public int getY();
    public int getRotation();
    public Image getImage();
}
