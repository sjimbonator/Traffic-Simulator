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
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Simulation extends JPanel {

    private ArrayList<WorldObject> worldObjects = new ArrayList();
    private ArrayList<ArrayList<Point2D>> carRoutes = new ArrayList();
    private Image carImage;
    private Image background;

    public Simulation() {
        try {
            carImage = ImageIO.read(new File("./car.png"));
            background = ImageIO.read(new File("./BACKGROUNDarrows.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //Filling the carRoutes ArrayList
        ArrayList<Point2D> route1 = new ArrayList();
        route1.add(new Point2D.Double(470,5));
        route1.add(new Point2D.Double(470,190));
        route1.add(new Point2D.Double(470,330));
        route1.add(new Point2D.Double(0,330));
        carRoutes.add(route1);
        
        
    }

    public void update() {
        int random = (int) (Math.random() * 100 + 1);
        if (random == 10) {
            worldObjects.add(new Car(carRoutes.get(0), carImage));
        }
        ArrayList<WorldObject> deleteList = new ArrayList();
        for (WorldObject object : worldObjects) {
            if(object.update(worldObjects)) {deleteList.add(object);}
        }
        worldObjects.removeAll(deleteList);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform xform = new AffineTransform();
        g2.drawImage(background, xform, this);
        for (WorldObject object : worldObjects) {
            xform.setToTranslation((int) object.getX() - (object.getImage().getWidth(this) / 2), (int) object.getY() - (object.getImage().getHeight(this) / 2));
            double offset = (object.getImage().getWidth(this) - object.getImage().getHeight(this));
            xform.rotate(Math.toRadians((int) object.getRotation()), (object.getImage().getWidth(this) / 2), (object.getImage().getHeight(this) / 2));
            xform.translate(-offset, -offset);
            g2.drawImage(carImage, xform, this);

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
