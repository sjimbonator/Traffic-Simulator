package simulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Simulation extends JPanel {

    private ArrayList<WorldObject> worldObjects = new ArrayList();
    private Image carImage;
    private Image background;
    private Image trafficLight;

    public Simulation() {
        try {
            carImage = ImageIO.read(new File("C:\\Users\\Startklaar\\Pictures\\car1.png"));
            background = ImageIO.read(new File("C:\\Users\\Startklaar\\Pictures\\crossing.png"));
            trafficLight = ImageIO.read(new File("C:\\Users\\Startklaar\\Pictures\\trafficlight.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void update() {
        int random = (int) (Math.random() * 200 + 1);
        if (random == 10) {
            worldObjects.add(new Car(1800, 540, 300, carImage));
            worldObjects.add(new TrafficLight(720, 520, 0, trafficLight));
        }
        
        for (WorldObject object : worldObjects) {
            object.update(worldObjects);
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        
        
        AffineTransform xform = new AffineTransform();
        g2.drawImage(background, xform, this);
        for (WorldObject object : worldObjects) {
            if(object.getType() == "Car"){
            xform.setToTranslation(object.getX() - (object.getImage().getWidth(this) / 2), object.getY() - (object.getImage().getHeight(this) / 2));
            //xform.setToTranslation(object.getX(), object.getY());
           //xform.rotate(Math.toRadians(object.getRotation()));
            xform.rotate(Math.toRadians(270),object.getImage().getWidth(this)/2,object.getImage().getHeight(this)/2 );
            //xform.rotate(Math.toRadians(270));
            g2.drawImage(carImage, xform, this);
            }
            if(object.getType() == "trafficLight") {
            xform.setToTranslation(object.getX() - (object.getImage().getWidth(this) / 2), object.getY() - (object.getImage().getHeight(this) / 2));
            //xform.setToTranslation(object.getX(), object.getY());
            xform.rotate(Math.toRadians(object.getRotation()));
            xform.scale(1.5, 1.5);
            g2.drawImage(trafficLight, xform, this);
            }

        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1920, 1080);
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
