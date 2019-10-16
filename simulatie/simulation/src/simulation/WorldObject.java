
package simulation;

import java.awt.Graphics2D;
import java.util.ArrayList;

interface WorldObject {
    public void draw(Graphics2D g);
    public ArrayList<Integer> getPos();
}
