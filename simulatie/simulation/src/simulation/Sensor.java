package simulation;

import java.util.ArrayList;

public class Sensor {

    private double x;
    private double y;
    private MqttPublisher publisher;
    
    private int payload = 0;

    public Sensor(String topic, double x, double y) {
        this.publisher = new MqttPublisher(topic);
        this.x = x;
        this.y = y;
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
            publisher.publish("" + payload);
        }
    }
}
