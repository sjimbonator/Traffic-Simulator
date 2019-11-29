package simulation;

import java.util.ArrayList;

public class Sensor {

    private double x;
    private double y;
    private String topic;
    
    private int payload = 0;

    public Sensor(String topic, double x, double y) {
        this.x = x;
        this.y = y;
        this.topic = topic;
    }

    public void update(ArrayList<DrawAbleObject> worldObjects) {
        int tempPayload = 0;
        for (DrawAbleObject object : worldObjects) {
            double objX = object.getX();
            double objY = object.getY();
            if ((objX >= (x - 40) && objX <= (x + 40)) && (objY >= (y - 40) && objY <= (y + 40))) {
                tempPayload = 1;
            }
        }
        if(tempPayload != payload){
            payload = tempPayload;
            MqttPublisher publisher = new MqttPublisher(topic, ("" + payload));
            Thread t = new Thread(publisher);
            t.start();
            };
        }
    }