package simulation;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Simulation extends JPanel {

    private ArrayList<WorldObject> worldObjects = new ArrayList();
    private Image car;
    private Image background;

    public Simulation() {
        try {
            car = ImageIO.read(new File("C:\\Users\\sjimm\\Pictures\\car.jpg"));
            background = ImageIO.read(new File("C:\\Users\\sjimm\\Pictures\\REEE.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform xform = new AffineTransform();
        xform.setToTranslation(960 -(background.getWidth(this) / 2) , 540-(background.getHeight(this) / 2));
        g2.drawImage(background, xform, this);
            
        for (WorldObject object : worldObjects) {
            object.draw(g2);
        }

    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("Traffic Jam Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(1920, 1080);
        frame.setVisible(true);

        Simulation sim = new Simulation();
        frame.setContentPane(sim);

    }
}
