package simulation;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Simulation extends JPanel {

    private String groupID = "7";

    private int tickCount = 0;

    private ArrayList<DrawAbleObject> worldObjects = new ArrayList();
    private ArrayList<Sensor> sensors = new ArrayList();

    private ArrayList<ArrayList<Point2D>> carRoutes = new ArrayList();
    private ArrayList<ArrayList<Point2D>> trainRoutes = new ArrayList();
    private ArrayList<ArrayList<Point2D>> boatRoutes = new ArrayList();
    private ArrayList<ArrayList<Point2D>> cyclistRoutes = new ArrayList();
    private ArrayList<ArrayList<Point2D>> pedestrianRoutes = new ArrayList();

    private ArrayList<Barrier> trainBarriers = new ArrayList();
    private ArrayList<Barrier> boatBarriers = new ArrayList();

    private Image carImage;
    private Image trainImage;
    private Image boatImage;
    private Image pedestrianImage;
    private Image bikeImage;

    private Image background;
    private Image backgroundOpen;

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
    
    private Image redBike;
    private Image whiteBike;
    private Image greenBike;
    
    private Image redPedestrian;
    private Image whitePedestrian;
    private Image greenPedestrian;

    private MqttSubscriber subscriber = MqttSubscriber.getInstance(groupID + "/#");

    public static boolean openBridge = false;

    public Simulation() {
        try {
            carImage = ImageIO.read(new File("./car.png"));
            trainImage = ImageIO.read(new File("./train.png"));
            boatImage = ImageIO.read(new File("./boat.png"));
            pedestrianImage = ImageIO.read(new File("./monkey.png"));
            bikeImage = ImageIO.read(new File("./bike.png"));

            background = ImageIO.read(new File("./BACKGROUNDarrows.png"));
            backgroundOpen = ImageIO.read(new File("./BRIDGEOPEN.png"));

            red = ImageIO.read(new File("./red.png"));
            orange = ImageIO.read(new File("./orange.png"));
            green = ImageIO.read(new File("./green.png"));
            white = ImageIO.read(new File("./white.png"));
            redBike =ImageIO.read(new File("./redBike.png"));
            greenBike =ImageIO.read(new File("./greenBike.png"));
            whiteBike =ImageIO.read(new File("./whiteBike.png"));
            redPedestrian =ImageIO.read(new File("./redPedestrian.png"));
            greenPedestrian =ImageIO.read(new File("./greenPedestrian.png"));
            whitePedestrian =ImageIO.read(new File("./whitePedestrian.png"));

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
        //Track
        sensors.add(new Sensor(groupID + "/Track/0/sensor/0", 343, 10));
        sensors.add(new Sensor(groupID + "/Track/1/sensor/0", 343, 890));

        //Vessel
        sensors.add(new Sensor(groupID + "/Vessel/0/sensor/0", 1129, 10));
        sensors.add(new Sensor(groupID + "/Vessel/1/sensor/0", 1129, 890));

        //Motorised
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

        //Filling the trainRoutes ArrayList
        ArrayList<Point2D> routeT0 = new ArrayList();
        routeT0.add(new Point2D.Double(343, -800));
        routeT0.add(new Point2D.Double(343, 1800));
        trainRoutes.add(routeT0);

        ArrayList<Point2D> routeT1 = new ArrayList();
        routeT1.add(new Point2D.Double(343, 1800));
        routeT1.add(new Point2D.Double(343, -800));
        trainRoutes.add(routeT1);

        //Filling the boatRoutes ArrayList
        ArrayList<Point2D> routeB0 = new ArrayList();
        routeB0.add(new Point2D.Double(1129, -800));
        routeB0.add(new Point2D.Double(1129, 1800));
        boatRoutes.add(routeB0);

        ArrayList<Point2D> routeB1 = new ArrayList();
        routeB1.add(new Point2D.Double(1129, 1800));
        routeB1.add(new Point2D.Double(1129, -800));
        boatRoutes.add(routeB1);

        //Filling the carRoutes ArrayList
        ArrayList<Point2D> route0 = new ArrayList();
        route0.add(new Point2D.Double(1300, 460));
        route0.add(new Point2D.Double(710, 460));
        route0.add(new Point2D.Double(470, 460));
        route0.add(new Point2D.Double(470, 900));
        carRoutes.add(route0);

        ArrayList<Point2D> route1 = new ArrayList();
        route1.add(new Point2D.Double(1300, 460));
        route1.add(new Point2D.Double(1010, 460));
        route1.add(new Point2D.Double(880, 408));
        route1.add(new Point2D.Double(710, 408));
        route1.add(new Point2D.Double(200, 380));
        route1.add(new Point2D.Double(0, 380));
        carRoutes.add(route1);

        ArrayList<Point2D> route2 = new ArrayList();
        route2.add(new Point2D.Double(1300, 460));
        route2.add(new Point2D.Double(1010, 460));
        route2.add(new Point2D.Double(880, 359));
        route2.add(new Point2D.Double(710, 359));
        route2.add(new Point2D.Double(200, 380));
        route2.add(new Point2D.Double(0, 380));
        carRoutes.add(route2);

        ArrayList<Point2D> route3 = new ArrayList();
        route3.add(new Point2D.Double(1300, 460));
        route3.add(new Point2D.Double(1010, 460));
        route3.add(new Point2D.Double(880, 304));
        route3.add(new Point2D.Double(710, 304));
        route3.add(new Point2D.Double(575, 311));
        route3.add(new Point2D.Double(575, 0));
        carRoutes.add(route3);

        ArrayList<Point2D> route4 = new ArrayList();
        route4.add(new Point2D.Double(575, 900));
        route4.add(new Point2D.Double(575, 570));
        route4.add(new Point2D.Double(1300, 570));
        carRoutes.add(route4);

        ArrayList<Point2D> route5 = new ArrayList();
        route5.add(new Point2D.Double(520, 900));
        route5.add(new Point2D.Double(520, 380));
        route5.add(new Point2D.Double(0, 380));
        carRoutes.add(route5);

        ArrayList<Point2D> route6 = new ArrayList();
        route6.add(new Point2D.Double(5, 520));
        route6.add(new Point2D.Double(1300, 520));
        carRoutes.add(route6);

        ArrayList<Point2D> route7 = new ArrayList();
        route7.add(new Point2D.Double(5, 575));
        route7.add(new Point2D.Double(475, 575));
        route7.add(new Point2D.Double(475, 900));
        carRoutes.add(route7);

        ArrayList<Point2D> route8 = new ArrayList();
        route8.add(new Point2D.Double(5, 450));
        route8.add(new Point2D.Double(570, 450));
        route8.add(new Point2D.Double(570, 0));
        carRoutes.add(route8);

        ArrayList<Point2D> route9 = new ArrayList();
        route9.add(new Point2D.Double(522, 5));
        route9.add(new Point2D.Double(522, 570));
        route9.add(new Point2D.Double(1300, 570));
        carRoutes.add(route9);

        ArrayList<Point2D> route10 = new ArrayList();
        route10.add(new Point2D.Double(470, 5));
        route10.add(new Point2D.Double(470, 330));
        route10.add(new Point2D.Double(0, 330));
        carRoutes.add(route10);
  
        //Filling the pedestrianRoutes ArrayList
        //North left side
        ArrayList<Point2D> routeP0 = new ArrayList();
        routeP0.add(new Point2D.Double(1400, 334));
        routeP0.add(new Point2D.Double(1061, 334));
        routeP0.add(new Point2D.Double(936, 187));
        routeP0.add(new Point2D.Double(662, 187));
        routeP0.add(new Point2D.Double(662, 668));
        routeP0.add(new Point2D.Double(1350, 668));
        pedestrianRoutes.add(routeP0);
        ArrayList<Point2D> routeP1 = new ArrayList();
        routeP1.add(new Point2D.Double(1400, 336));
        routeP1.add(new Point2D.Double(1058, 336));
        routeP1.add(new Point2D.Double(937, 190));
        routeP1.add(new Point2D.Double(677, 190));
        routeP1.add(new Point2D.Double(600, 222));
        routeP1.add(new Point2D.Double(450, 222));
        routeP1.add(new Point2D.Double(397, 231));
        routeP1.add(new Point2D.Double(-50, 231));
        pedestrianRoutes.add(routeP1);
        
        //West side
        ArrayList<Point2D> routeP2 = new ArrayList();
        routeP2.add(new Point2D.Double(657, -100));
        routeP2.add(new Point2D.Double(657, 210));
        routeP2.add(new Point2D.Double(662, 290));
        routeP2.add(new Point2D.Double(662, 668));
        routeP2.add(new Point2D.Double(1350, 668));
        pedestrianRoutes.add(routeP2);
        
        //South left side
        ArrayList<Point2D> routeP3 = new ArrayList();
        routeP3.add(new Point2D.Double(-100, 248));
        routeP3.add(new Point2D.Double(268, 248));
        routeP3.add(new Point2D.Double(268, 668));
        routeP3.add(new Point2D.Double(374, 668));
        routeP3.add(new Point2D.Double(374, 950));
        pedestrianRoutes.add(routeP3);
        ArrayList<Point2D> routeP4 = new ArrayList();
        routeP4.add(new Point2D.Double(-100, 248));
        routeP4.add(new Point2D.Double(400, 248));
        routeP4.add(new Point2D.Double(450, 238));
        routeP4.add(new Point2D.Double(630, 238));
        routeP4.add(new Point2D.Double(650, 205));
        routeP4.add(new Point2D.Double(930, 205));
        routeP4.add(new Point2D.Double(1053, 355));
        routeP4.add(new Point2D.Double(1350, 355));
        pedestrianRoutes.add(routeP4);
        ArrayList<Point2D> routeP5 = new ArrayList();
        routeP5.add(new Point2D.Double(-100, 248));
        routeP5.add(new Point2D.Double(268, 248));
        routeP5.add(new Point2D.Double(268, 668));
        routeP5.add(new Point2D.Double(374, 668));
        routeP5.add(new Point2D.Double(434, 675));
        routeP5.add(new Point2D.Double(600, 675));
        routeP5.add(new Point2D.Double(650, 668));
        routeP5.add(new Point2D.Double(1350, 668));
        pedestrianRoutes.add(routeP5);
        
        //North right side
        ArrayList<Point2D> routeP6 = new ArrayList();
        routeP6.add(new Point2D.Double(1400, 651));
        routeP6.add(new Point2D.Double(650, 651));
        routeP6.add(new Point2D.Double(600, 659));
        routeP6.add(new Point2D.Double(435, 659));
        routeP6.add(new Point2D.Double(397, 651));
        routeP6.add(new Point2D.Double(-100, 651));
        pedestrianRoutes.add(routeP6);
        ArrayList<Point2D> routeP7 = new ArrayList();
        routeP7.add(new Point2D.Double(1400, 651));
        routeP7.add(new Point2D.Double(678, 651));
        routeP7.add(new Point2D.Double(678, -100));
        pedestrianRoutes.add(routeP7);
        
        //East side
        ArrayList<Point2D> routeP8 = new ArrayList();
        routeP8.add(new Point2D.Double(392, 1000));
        routeP8.add(new Point2D.Double(392, 675));
        routeP8.add(new Point2D.Double(600, 675));
        routeP8.add(new Point2D.Double(650, 668));
        routeP8.add(new Point2D.Double(1400, 668));
        pedestrianRoutes.add(routeP8);
        ArrayList<Point2D> routeP9 = new ArrayList();
        routeP9.add(new Point2D.Double(392, 1000));
        routeP9.add(new Point2D.Double(392, 651));
        routeP9.add(new Point2D.Double(284, 651));
        routeP9.add(new Point2D.Double(284, 230));
        routeP9.add(new Point2D.Double(-100, 230));
        pedestrianRoutes.add(routeP9);
        ArrayList<Point2D> routeP10 = new ArrayList();
        routeP10.add(new Point2D.Double(392, 1000));
        routeP10.add(new Point2D.Double(392, 651));
        routeP10.add(new Point2D.Double(284, 651));
        routeP10.add(new Point2D.Double(284, 230));
        routeP10.add(new Point2D.Double(397, 230));
        routeP10.add(new Point2D.Double(450, 238));
        routeP10.add(new Point2D.Double(650, 238));
        routeP10.add(new Point2D.Double(675, 207));
        routeP10.add(new Point2D.Double(675, -100));
        pedestrianRoutes.add(routeP10);
        
        //South right side
        ArrayList<Point2D> routeP11 = new ArrayList();
        routeP11.add(new Point2D.Double(-100, 668));
        routeP11.add(new Point2D.Double(392, 668));
        routeP11.add(new Point2D.Double(434, 675));
        routeP11.add(new Point2D.Double(600, 675));
        routeP11.add(new Point2D.Double(650, 668));
        routeP11.add(new Point2D.Double(1400, 668));
        pedestrianRoutes.add(routeP11);
        ArrayList<Point2D> routeP12 = new ArrayList();
        routeP12.add(new Point2D.Double(-100, 668));
        routeP12.add(new Point2D.Double(392, 668));
        routeP12.add(new Point2D.Double(434, 675));
        routeP12.add(new Point2D.Double(600, 675));
        routeP12.add(new Point2D.Double(650, 668));
        routeP12.add(new Point2D.Double(678, 668));
        routeP12.add(new Point2D.Double(678, -100));
        pedestrianRoutes.add(routeP12);
        
        
        //Cyclist routes
        //South right side
        ArrayList<Point2D> routeC0 = new ArrayList();
        routeC0.add(new Point2D.Double(-100, 632));
        routeC0.add(new Point2D.Double(1350, 632));
        cyclistRoutes.add(routeC0);

        ArrayList<Point2D> routeC2 = new ArrayList();
        routeC2.add(new Point2D.Double(-100, 632));
        routeC2.add(new Point2D.Double(248, 632));
        routeC2.add(new Point2D.Double(248, 282));
        routeC2.add(new Point2D.Double(642, 282));
        routeC2.add(new Point2D.Double(642, -100));
        cyclistRoutes.add(routeC2);
        
        //West right side
        ArrayList<Point2D> routeC3 = new ArrayList();
        routeC3.add(new Point2D.Double(404, -100));
        routeC3.add(new Point2D.Double(404, 950));
        cyclistRoutes.add(routeC3);
        ArrayList<Point2D> routeC7 = new ArrayList();
        routeC7.add(new Point2D.Double(404, -100));
        routeC7.add(new Point2D.Double(404, 632));
        routeC7.add(new Point2D.Double(1350, 632));
        cyclistRoutes.add(routeC7);
        
        //North left side
        ArrayList<Point2D> routeC5 = new ArrayList();
        routeC5.add(new Point2D.Double(1400, 375));
        routeC5.add(new Point2D.Double(1040, 375));
        routeC5.add(new Point2D.Double(916, 227));
        routeC5.add(new Point2D.Double(650, 227));
        routeC5.add(new Point2D.Double(602, 261));
        routeC5.add(new Point2D.Double(-100, 261));
        cyclistRoutes.add(routeC5);
        ArrayList<Point2D> routeC6 = new ArrayList();
        routeC6.add(new Point2D.Double(1400, 375));
        routeC6.add(new Point2D.Double(1040, 375));
        routeC6.add(new Point2D.Double(916, 227));
        routeC6.add(new Point2D.Double(620, 227));
        routeC6.add(new Point2D.Double(620, 950));
        cyclistRoutes.add(routeC6);

        //Creating traffic lights
        //North
        TrafficLight light0 = new TrafficLight(groupID + "/motorised/0/traffic_light/0", 702, 470, 180, red, orange, green, white, "car");
        worldObjects.add(light0);
        TrafficLight light1 = new TrafficLight(groupID + "/motorised/1/0/traffic_light/0", 702, 421, 270, red, orange, green, white, "car");
        worldObjects.add(light1);
        TrafficLight light2 = new TrafficLight(groupID + "/motorised/1/1/traffic_light/0", 702, 342, 270, red, orange, green, white, "car");
        worldObjects.add(light2);
        TrafficLight light3 = new TrafficLight(groupID + "/motorised/2/traffic_light/0", 702, 293, 0, red, orange, green, white, "car");
        worldObjects.add(light3);

        //East
        TrafficLight light4 = new TrafficLight(groupID + "/motorised/3/traffic_light/0", 588, 698, 90, red, orange, green, white, "car");
        worldObjects.add(light4);
        TrafficLight light5 = new TrafficLight(groupID + "/motorised/4/traffic_light/0", 509, 698, 270, red, orange, green, white, "car");
        worldObjects.add(light5);

        //South
        TrafficLight light6 = new TrafficLight(groupID + "/motorised/5/0/traffic_light/0", 202, 505, 90, red, orange, green, white, "car");
        worldObjects.add(light6);
        TrafficLight light7 = new TrafficLight(groupID + "/motorised/5/1/traffic_light/0", 202, 584, 90, red, orange, green, white, "car");
        worldObjects.add(light7);
        TrafficLight light8 = new TrafficLight(groupID + "/motorised/6/traffic_light/0", 202, 438, 0, red, orange, green, white, "car");
        worldObjects.add(light8);

        //West
        TrafficLight light9 = new TrafficLight(groupID + "/motorised/7/traffic_light/0", 538, 202, 90, red, orange, green, white, "car");
        worldObjects.add(light9);
        TrafficLight light10 = new TrafficLight(groupID + "/motorised/8/traffic_light/0", 460, 202, 270, red, orange, green, white, "car");
        worldObjects.add(light10);
        
        
        //Cyclist lights
        //South side
        TrafficLight lightC0 = new TrafficLight(groupID + "/cycle/8/traffic_light/0", 245, 598, 0,redBike, whiteBike, greenBike, whiteBike, "cyclist");
        worldObjects.add(lightC0);
        TrafficLight lightP0 = new TrafficLight(groupID + "/foot/8/traffic_light/0", 275, 598, 0, redPedestrian, whitePedestrian, greenPedestrian, whitePedestrian, "pedestrian");
        worldObjects.add(lightP0);
        TrafficLight lightC1 = new TrafficLight(groupID + "/cycle/8/traffic_light/0", 228, 302, 0, redBike, whiteBike, greenBike, whiteBike, "cyclist");
        worldObjects.add(lightC1);
        TrafficLight lightP1 = new TrafficLight(groupID + "/foot/8/traffic_light/0", 275, 302, 0, redPedestrian, whitePedestrian, greenPedestrian, whitePedestrian, "pedestrian");
        worldObjects.add(lightP1);
        TrafficLight lightC2 = new TrafficLight(groupID + "/cycle/8/traffic_light/0", 400, 305, 0, redBike, whiteBike, greenBike, whiteBike, "cyclist");
        worldObjects.add(lightC2);
        
        //North side
        TrafficLight lightC3 = new TrafficLight(groupID + "/cycle/8/traffic_light/0", 638, 585, 0, redBike, whiteBike, greenBike, whiteBike, "cyclist");
        worldObjects.add(lightC3);
        
        //East side
        TrafficLight lightC4 = new TrafficLight(groupID + "/cycle/8/traffic_light/0", 450, 635, 0, redBike, whiteBike, greenBike, whiteBike, "cyclist");
        worldObjects.add(lightC4);
        
        //West side
        TrafficLight lightC5 = new TrafficLight(groupID + "/cycle/8/traffic_light/0", 440, 280, 0, redBike, whiteBike, greenBike, whiteBike, "cyclist");
        worldObjects.add(lightC5);
        TrafficLight lightC6 = new TrafficLight(groupID + "/cycle/8/traffic_light/0", 600, 260, 0, redBike, whiteBike, greenBike, whiteBike, "cyclist");
        worldObjects.add(lightC6);
        
        //Pedestrian lights
        //East side
        TrafficLight lightP2 = new TrafficLight(groupID + "/foot/8/traffic_light/0", 440, 675, 0, redPedestrian, whitePedestrian, greenPedestrian, whitePedestrian, "pedestrian");
        worldObjects.add(lightP2);
        TrafficLight lightP3 = new TrafficLight(groupID + "/foot/8/traffic_light/0", 600, 657, 0, redPedestrian, whitePedestrian, greenPedestrian, whitePedestrian, "pedestrian");
        worldObjects.add(lightP3);
        
        //West side
        TrafficLight lightP4 = new TrafficLight(groupID + "/foot/8/traffic_light/0", 440, 235, 0, redPedestrian, whitePedestrian, greenPedestrian, whitePedestrian, "pedestrian");
        worldObjects.add(lightP4);
        TrafficLight lightP5 = new TrafficLight(groupID + "/foot/8/traffic_light/0", 605, 220, 0, redPedestrian, whitePedestrian, greenPedestrian, whitePedestrian, "pedestrian");
        worldObjects.add(lightP5);
        
        //South side
        TrafficLight lightP6 = new TrafficLight(groupID + "/foot/8/traffic_light/0", 274, 416, 0, redPedestrian, whitePedestrian, greenPedestrian, whitePedestrian, "pedestrian");
        worldObjects.add(lightP6);
        TrafficLight lightP7 = new TrafficLight(groupID + "/foot/8/traffic_light/0", 274, 487, 0, redPedestrian, whitePedestrian, greenPedestrian, whitePedestrian, "pedestrian");
        worldObjects.add(lightP7);
        
        TrafficLight lightP8 = new TrafficLight(groupID + "/foot/8/traffic_light/0", 665, 285, 0, redPedestrian, whitePedestrian, greenPedestrian, whitePedestrian, "pedestrian");
        worldObjects.add(lightP8);
        TrafficLight lightP9 = new TrafficLight(groupID + "/foot/8/traffic_light/0", 675, 595, 0, redPedestrian, whitePedestrian, greenPedestrian, whitePedestrian, "pedestrian");
        worldObjects.add(lightP9);
        TrafficLight lightP10 = new TrafficLight(groupID + "/foot/8/traffic_light/0", 670, 487, 0, redPedestrian, whitePedestrian, greenPedestrian, whitePedestrian, "pedestrian");
        worldObjects.add(lightP10);

        //Creating train barriers
        //West > East
        Barrier barrier0 = new Barrier(groupID + "/track/0/barrier/0", 375, 270, shortbarrierOff, shortbarrier);
        worldObjects.add(barrier0);
        trainBarriers.add(barrier0);
        Barrier barrier1 = new Barrier(groupID + "/track/0/barrier/1", 375, 365, longbarrierOff, longbarrier);
        worldObjects.add(barrier1);
        trainBarriers.add(barrier1);
        Barrier barrier2 = new Barrier(groupID + "/track/0/barrier/2", 375, 625, shortbarrierOff, shortbarrier);
        worldObjects.add(barrier2);
        trainBarriers.add(barrier2);
        Barrier barrier3 = new Barrier(groupID + "/track/0/barrier/3", 375, 665, shortbarrierOff, shortbarrier);
        worldObjects.add(barrier3);
        trainBarriers.add(barrier3);
        Barrier barrier4 = new Barrier(groupID + "/track/0/barrier/4", 310, 240, shortbarrierOff, shortbarrier);
        worldObjects.add(barrier4);
        trainBarriers.add(barrier4);
        Barrier barrier5 = new Barrier(groupID + "/track/0/barrier/5", 310, 270, shortbarrierOff, shortbarrier);
        worldObjects.add(barrier5);
        trainBarriers.add(barrier5);
        Barrier barrier6 = new Barrier(groupID + "/track/0/barrier/6", 310, 450, midbarrierOff, midbarrier);
        worldObjects.add(barrier6);
        trainBarriers.add(barrier6);
        Barrier barrier7 = new Barrier(groupID + "/track/0/barrier/7", 310, 547, longbarrierOff, longbarrier);
        worldObjects.add(barrier7);
        trainBarriers.add(barrier7);
        Barrier barrier8 = new Barrier(groupID + "/track/0/barrier/8", 310, 645, midbarrierOff, midbarrier);
        worldObjects.add(barrier8);
        trainBarriers.add(barrier8);

        //Creating boat barriers
        //West > East
        Barrier vesbarrier0 = new Barrier(groupID + "/vessel/0/barrier/0", 1225, 372, longbarrierOff, longbarrier);
        worldObjects.add(vesbarrier0);
        boatBarriers.add(vesbarrier0);
        Barrier vesbarrier1 = new Barrier(groupID + "/vessel/0/barrier/1", 1225, 465, midbarrierOff, midbarrier);
        worldObjects.add(vesbarrier1);
        boatBarriers.add(vesbarrier1);
        Barrier vesbarrier2 = new Barrier(groupID + "/vessel/0/barrier/2", 1225, 552, longbarrierOff, longbarrier);
        worldObjects.add(vesbarrier2);
        boatBarriers.add(vesbarrier2);
        Barrier vesbarrier3 = new Barrier(groupID + "/vessel/0/barrier/3", 1225, 643, midbarrierOff, midbarrier);
        worldObjects.add(vesbarrier3);
        boatBarriers.add(vesbarrier3);
        Barrier vesbarrier4 = new Barrier(groupID + "/vessel/0/barrier/4", 1062, 372, longbarrierOff, longbarrier);
        worldObjects.add(vesbarrier4);
        boatBarriers.add(vesbarrier4);
        Barrier vesbarrier5 = new Barrier(groupID + "/vessel/0/barrier/5", 1062, 465, midbarrierOff, midbarrier);
        worldObjects.add(vesbarrier5);
        boatBarriers.add(vesbarrier5);
        Barrier vesbarrier6 = new Barrier(groupID + "/vessel/0/barrier/6", 1062, 552, longbarrierOff, longbarrier);
        worldObjects.add(vesbarrier6);
        boatBarriers.add(vesbarrier6);
        Barrier vesbarrier7 = new Barrier(groupID + "/vessel/0/barrier/7", 1062, 643, midbarrierOff, midbarrier);
        worldObjects.add(vesbarrier7);
        boatBarriers.add(vesbarrier7);

    }

    public void update() {
        tickCount++;
        String warningLightPayload = MqttSubscriber.messages.get(groupID + "/track/0/warning_light/0");
        if (warningLightPayload != null && warningLightPayload.contains("1")) {
            for (Barrier object : trainBarriers) {
                object.setOverride(true);
            }

        } else {

            for (Barrier object : trainBarriers) {
                object.setOverride(false);
            }

        }

        warningLightPayload = MqttSubscriber.messages.get(groupID + "/vessel/0/warning_light/0");
        if (warningLightPayload != null && warningLightPayload.contains("1")) {
            openBridge = true;
            for (Barrier object : boatBarriers) {
                object.setOverride(true);
            }

        } else {
            openBridge = false;
            for (Barrier object : boatBarriers) {
                object.setOverride(false);
            }

        }

        //Car spawn
        if (tickCount % 100 == 0) {
            int random = (int) (Math.random() * carRoutes.size());
            worldObjects.add(new Car(carRoutes.get(random), carImage));
        }
        //Train spawn
        if (tickCount % 3600 == 0) {
            int random = (int) (Math.random() * trainRoutes.size());
            worldObjects.add(new Train(trainRoutes.get(random), trainImage));
        }
        //Boat spawn
        if (tickCount % 4800 == 0) {
            int random = (int) (Math.random() * boatRoutes.size());
            worldObjects.add(new Boat(boatRoutes.get(random), boatImage));
        }
        //Pedestrian spawn
        if (tickCount % 150 == 0) {
            int random = (int) (Math.random() * pedestrianRoutes.size());
            worldObjects.add(new Pedestrian(pedestrianRoutes.get(random), pedestrianImage));
        }
        
        if (tickCount % 150 == 0) {
            int random = (int) (Math.random() * cyclistRoutes.size());
            worldObjects.add(new Cyclist(cyclistRoutes.get(random), bikeImage));
        }
        ArrayList<DrawAbleObject> deleteList = new ArrayList();
        for (DrawAbleObject object : worldObjects) {
            if (object.update(worldObjects)) {
                deleteList.add(object);
            }
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
        if (openBridge) {
            g2.drawImage(backgroundOpen, xform, this);
        } else {
            g2.drawImage(background, xform, this);
        }

        for (DrawAbleObject object : worldObjects) {
            xform.setToTranslation((int) object.getX() - (object.getImage().getWidth(this) / 2), (int) object.getY() - (object.getImage().getHeight(this) / 2));
            xform.rotate(Math.toRadians((int) object.getRotation()), (object.getImage().getWidth(this) / 2), (object.getImage().getHeight(this) / 2));
            g2.drawImage(object.getImage(), xform, this);
            
            if(object instanceof MoveAbleObject){
                
                Rectangle2D rect = ((MoveAbleObject) object).getHitbox();
                g2.drawRect( (int) rect.getX() + 1, (int) rect.getY()+ 1,(int) rect.getWidth()+ 1,(int) rect.getHeight()+ 1 );}

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
