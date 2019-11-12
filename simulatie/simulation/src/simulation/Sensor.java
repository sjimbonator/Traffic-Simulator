package simulation;

import java.util.ArrayList;
import javax.swing.SwingWorker;

public class Sensor {

    private double x;
    private double y;
    private String topic;
    private MqttPublisher publisher = new MqttPublisher(topic);
    
    private int payload = 0;

    public Sensor(String topic, double x, double y) {
        this.x = x;
        this.y = y;
        this.topic = topic;
    }
    
    public void update(ArrayList<WorldObject> worldObjects) {
        int tempPayload = 0;
        for (WorldObject object : worldObjects) {
            double objX = object.getX();
            double objY = object.getY();
            if ((objX >= (x - 30) && objX <= (x + 30)) && (objY >= (y - 30) && objY <= (y + 30))) {
                tempPayload = 1;
            }
        }
        if(tempPayload != payload){
            payload = tempPayload;
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                publisher.publish("" + payload);
                 return null;
                }
            };
            worker.execute();
        }
    }
}
