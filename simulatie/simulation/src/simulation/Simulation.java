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
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        Image img;
        try{
            for (WorldObject object : worldObjects){
                object.draw(g2);
            }
            
            img = ImageIO.read(new File("C:\\Users\\sjimm\\Pictures\\thumbnail.png"));
            AffineTransform xform = new AffineTransform();
            xform.setToTranslation(960 -(img.getWidth(this) / 2) , 540-(img.getHeight(this) / 2)); // in het midden
            //xform.rotate(Math.PI / 6);
            g2.drawImage(img, xform, this);
        } catch(Exception e){e.printStackTrace();}
        
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
