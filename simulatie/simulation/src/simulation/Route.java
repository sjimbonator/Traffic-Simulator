package simulation;

import java.util.ArrayList;

public class Route {
    ArrayList<ArrayList<Integer>> points = new ArrayList();
    
    public Route(ArrayList<ArrayList<Integer>> points){ 
        this.points.addAll(points);
    }
    
    
}
