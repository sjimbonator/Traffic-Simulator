package simulation;

import java.util.ArrayList;

public class Sensor {

    private double x;
    private double y;
    private String topic;
    private String type;
    
    private int payload = 0;

    public Sensor(String topic, double x, double y, String type) {
        this.x = x;
        this.y = y;
        this.topic = topic;
        this.type = type;
    }

    public void update(ArrayList<DrawAbleObject> worldObjects) {
        int tempPayload = 0;
        for (DrawAbleObject object : worldObjects) {
            double objX = object.getX();
            double objY = object.getY();
            if (((object.getType() == this.type) && objX >= (x - 30) && objX <= (x + 30)) && (objY >= (y - 30) && objY <= (y + 30))) {
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