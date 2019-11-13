package simulation;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Simulation extends JPanel {
    
    private String groupID = "7";
    
    private ArrayList<DrawAbleObject> worldObjects = new ArrayList();
    private ArrayList<Sensor> sensors = new ArrayList();
    private ArrayList<ArrayList<Point2D>> carRoutes = new ArrayList();
    
    private Image carImage;
    private Image background;
    
    private Image shortbarrierOff;
    private Image shortbarrier;  // image for barrier with id 0, 2, 3, 4, 5
    private Image midbarrierOff;
    private Image midbarrier;  // image for barrier with id 6, 8
    private Image longbarrierOff;
    private Image longbarrier;  // image for barrier with id 1, 7
    
    private Image red;
    private Image orange;
    private Image green;
    private Image white;

    public Simulation() {
        try {
            carImage = ImageIO.read(new File("./car.png"));
            background = ImageIO.read(new File("./BACKGROUNDarrows.png"));
            
            
            red = ImageIO.read(new File("./red.png"));
            orange = ImageIO.read(new File("./orange.png"));
            green = ImageIO.read(new File("./green.png"));
            white = ImageIO.read(new File("./white.png"));
            
            shortbarrier = ImageIO.read(new File("./shortbarrier.png"));
            shortbarrierOff = ImageIO.read(new File("./shortbarrierOff.png"));
            midbarrier = ImageIO.read(new File("./midbarrier.png"));
            midbarrierOff = ImageIO.read(new File("./midbarrierOff.png"));
            longbarrier = ImageIO.read(new File("./longbarrier.png"));
            longbarrierOff = ImageIO.read(new File("./longbarrierOff.png"));
            

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //Creating sensors
        
            //North
            
                //North > East
                sensors.add(new Sensor(groupID + "/motorised/0/sensor/0", 872, 470));
                sensors.add(new Sensor(groupID + "/motorised/0/sensor/1", 714, 470));

                //North >South
                    //East Lane
                    sensors.add(new Sensor(groupID + "/motorised/1/0/sensor/0", 872, 407));
                    sensors.add(new Sensor(groupID + "/motorised/1/0/sensor/1", 714, 407));

                    //West Lane
                    sensors.add(new Sensor(groupID + "/motorised/1/1/sensor/0", 872, 360));
                    sensors.add(new Sensor(groupID + "/motorised/1/1/sensor/1", 714, 360));

                //North > West
                sensors.add(new Sensor(groupID + "/motorised/2/sensor/0", 872, 300));
                sensors.add(new Sensor(groupID + "/motorised/2/sensor/1", 714, 300));
        
            //East
            
                //East > North
                sensors.add(new Sensor(groupID + "/motorised/3/sensor/0", 880, 574));
                sensors.add(new Sensor(groupID + "/motorised/3/sensor/1", 714, 574));
                
                //East > South
                sensors.add(new Sensor(groupID + "/motorised/4/sensor/0", 880, 522));
                sensors.add(new Sensor(groupID + "/motorised/4/sensor/1", 714, 522));
        
            //South

                //South > North & East
                    //South > North
                    sensors.add(new Sensor(groupID + "/motorised/5/0/sensor/0", 35, 520));
                    sensors.add(new Sensor(groupID + "/motorised/5/0/sensor/1", 166, 520));

                    //South > East
                    sensors.add(new Sensor(groupID + "/motorised/5/1/sensor/0", 35, 573));
                    sensors.add(new Sensor(groupID + "/motorised/5/1/sensor/1", 166, 573));

                //South > West
                sensors.add(new Sensor(groupID + "/motorised/6/sensor/0", 35, 452));
                sensors.add(new Sensor(groupID + "/motorised/6/sensor/1", 166, 452));

            //West

                //West > North
                sensors.add(new Sensor(groupID + "/motorised/7/sensor/0", 516, 330));
                sensors.add(new Sensor(groupID + "/motorised/7/sensor/1", 516, 35));

                //West > South
                sensors.add(new Sensor(groupID + "/motorised/8/sensor/0", 470, 330));
                sensors.add(new Sensor(groupID + "/motorised/8/sensor/1", 470, 35));
        
        //Filling the carRoutes ArrayList
        ArrayList<Point2D> route0 = new ArrayList();
        route0.add(new Point2D.Double(1300,460));
        route0.add(new Point2D.Double(710,460));
        route0.add(new Point2D.Double(470,460));
        route0.add(new Point2D.Double(470,900));
        carRoutes.add(route0);
        
        ArrayList<Point2D> route1 = new ArrayList();
        route1.add(new Point2D.Double(1300,460));
        route1.add(new Point2D.Double(1010,460));
        route1.add(new Point2D.Double(880,408));
        route1.add(new Point2D.Double(710,408));
        route1.add(new Point2D.Double(200,380));
        route1.add(new Point2D.Double(0,380));
        carRoutes.add(route1);
        
        ArrayList<Point2D> route2 = new ArrayList();
        route2.add(new Point2D.Double(1300,460));
        route2.add(new Point2D.Double(1010,460));
        route2.add(new Point2D.Double(880,359));
        route2.add(new Point2D.Double(710,359));
        route2.add(new Point2D.Double(200,380));
        route2.add(new Point2D.Double(0,380));
        carRoutes.add(route2);
        
        ArrayList<Point2D> route3 = new ArrayList();
        route3.add(new Point2D.Double(1300,460));
        route3.add(new Point2D.Double(1010,460));
        route3.add(new Point2D.Double(880,304));
        route3.add(new Point2D.Double(710,304));
        route3.add(new Point2D.Double(575,311));
        route3.add(new Point2D.Double(575,0));
        carRoutes.add(route3);
        
        ArrayList<Point2D> route4 = new ArrayList();
        route4.add(new Point2D.Double(575,900));
        route4.add(new Point2D.Double(575,570));
        route4.add(new Point2D.Double(1300,570));
        carRoutes.add(route4);
        
        ArrayList<Point2D> route5 = new ArrayList();
        route5.add(new Point2D.Double(520,900));
        route5.add(new Point2D.Double(520,380));
        route5.add(new Point2D.Double(0,380));
        carRoutes.add(route5);
        
        ArrayList<Point2D> route6 = new ArrayList();
        route6.add(new Point2D.Double(5,520));
        route6.add(new Point2D.Double(1300,520));
        carRoutes.add(route6);
        
        ArrayList<Point2D> route7 = new ArrayList();
        route7.add(new Point2D.Double(5,575));
        route7.add(new Point2D.Double(475,575));
        route7.add(new Point2D.Double(475,900));
        carRoutes.add(route7);
        
        ArrayList<Point2D> route8 = new ArrayList();
        route8.add(new Point2D.Double(5,450));
        route8.add(new Point2D.Double(570,450));
        route8.add(new Point2D.Double(570,0));
        carRoutes.add(route8);
        
        ArrayList<Point2D> route9 = new ArrayList();
        route9.add(new Point2D.Double(522,5));
        route9.add(new Point2D.Double(522,570));
        route9.add(new Point2D.Double(1300,570));
        carRoutes.add(route9);
        
        ArrayList<Point2D> route10 = new ArrayList();
        route10.add(new Point2D.Double(470,5));
        route10.add(new Point2D.Double(470,330));
        route10.add(new Point2D.Double(0,330));
        carRoutes.add(route10);
        
        //Creating traffic lights
        
        //North
        TrafficLight light0 = new TrafficLight(groupID + "/motorised/0/traffic_light/0", 702, 470, 180, red, orange, green, white );
        worldObjects.add(light0);
        TrafficLight light1 = new TrafficLight(groupID + "/motorised/1/0/traffic_light/0", 702, 421, 270, red, orange, green, white );
        worldObjects.add(light1);
        TrafficLight light2 = new TrafficLight(groupID + "/motorised/1/1/traffic_light/0", 702, 342, 270, red, orange, green, white );
        worldObjects.add(light2);
        TrafficLight light3 = new TrafficLight(groupID + "/motorised/2/traffic_light/0", 702, 293, 0, red, orange, green, white );
        worldObjects.add(light3);
        
        //East
        TrafficLight light4 = new TrafficLight(groupID + "/motorised/3/traffic_light/0", 588, 698, 90, red, orange, green, white );
        worldObjects.add(light4);
        TrafficLight light5 = new TrafficLight(groupID + "/motorised/4/traffic_light/0", 509, 698, 270, red, orange, green, white );
        worldObjects.add(light5);
        
        //South
        TrafficLight light6 = new TrafficLight(groupID + "/motorised/5/0/traffic_light/0", 202, 505, 90, red, orange, green, white );
        worldObjects.add(light6);
        TrafficLight light7 = new TrafficLight(groupID + "/motorised/5/1/traffic_light/0", 202, 584, 90, red, orange, green, white );
        worldObjects.add(light7);
        TrafficLight light8 = new TrafficLight(groupID + "/motorised/6/traffic_light/0", 202, 438, 0, red, orange, green, white );
        worldObjects.add(light8);
        
        //West
        TrafficLight light9 = new TrafficLight(groupID + "/motorised/7/traffic_light/0", 538, 202, 90, red, orange, green, white );
        worldObjects.add(light9);
        TrafficLight light10 = new TrafficLight(groupID + "/motorised/8/traffic_light/0", 460, 202, 270, red, orange, green, white );
        worldObjects.add(light10);
        
        //Creating train barroers
        
        //West > East
        Barrier barrier0 = new Barrier(groupID + "/track/0/barrier/0", 338, 240, shortbarrierOff, shortbarrier);
        worldObjects.add(barrier0);
        Barrier barrier1 = new Barrier(groupID + "/track/0/barrier/1", 290, 272, longbarrierOff, longbarrier);
        worldObjects.add(barrier1);
        Barrier barrier2 = new Barrier(groupID + "/track/0/barrier/2", 338, 595, shortbarrierOff, shortbarrier);
        worldObjects.add(barrier2);
        Barrier barrier3 = new Barrier(groupID + "/track/0/barrier/3", 338, 635, shortbarrierOff, shortbarrier);
        worldObjects.add(barrier3);
        Barrier barrier4 = new Barrier(groupID + "/track/0/barrier/4", 275, 205, shortbarrierOff, shortbarrier);
        worldObjects.add(barrier4);
        Barrier barrier5 = new Barrier(groupID + "/track/0/barrier/5", 275, 240, shortbarrierOff, shortbarrier);
        worldObjects.add(barrier5);
        Barrier barrier6 = new Barrier(groupID + "/track/0/barrier/6", 260, 405, midbarrierOff, midbarrier);
        worldObjects.add(barrier6);
        Barrier barrier7 = new Barrier(groupID + "/track/0/barrier/7", 227, 462, longbarrierOff, longbarrier);
        worldObjects.add(barrier7);
        Barrier barrier8 = new Barrier(groupID + "/track/0/barrier/8", 260, 588, midbarrierOff, midbarrier);
        worldObjects.add(barrier8);
        
        
        
    }

    public void update() {
        int random = (int) (Math.random() * 100 + 1);
        if (random == 10) {
            random = (int) (Math.random() * 11);
            worldObjects.add(new Car(carRoutes.get(random), carImage));
        }
        ArrayList<DrawAbleObject> deleteList = new ArrayList();
        for (DrawAbleObject object : worldObjects) {
            if(object.update(worldObjects)) {deleteList.add(object);}
        }
        worldObjects.removeAll(deleteList);
        for (Sensor sensor : sensors) {
            sensor.update(worldObjects);
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform xform = new AffineTransform();
        g2.drawImage(background, xform, this);
        for (DrawAbleObject object : worldObjects) {
            xform.setToTranslation((int) object.getX() - (object.getImage().getWidth(this) / 2), (int) object.getY() - (object.getImage().getHeight(this) / 2));
            double offset = (object.getImage().getWidth(this) - object.getImage().getHeight(this));
            xform.rotate(Math.toRadians((int) object.getRotation()), (object.getImage().getWidth(this) / 2), (object.getImage().getHeight(this) / 2));
            xform.translate(-offset, -offset);
            g2.drawImage(object.getImage(), xform, this);

        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1311, 900);
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("Traffic Jam Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        Simulation sim = new Simulation();
        frame.setContentPane(sim);
        frame.pack();
        frame.setVisible(true);
        while (true) {
            try {
                sim.update();
                Thread.sleep(17);
            } catch (InterruptedException ex) {
                Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
