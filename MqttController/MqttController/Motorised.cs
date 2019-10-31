using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MqttController
{
    class Motorised
    {
        private string mqttMessage;
        private string topic;
        private int direction;
        //2d array with sensor directions
        private string [,] trafficLightGroups =  
        {
            //North
            {"/motorised/0/sensor/0", "/motorised/0/sensor/1", "/motorised/1/0/sensor/0", "/motorised/1/0/sensor/1", "/motorised/1/1/sensor/0", "/motorised/1/1/sensor/1", "/motorised/2/sensor/0", "/motorised/2/sensor/1"},
            //East
            {"/motorised/3/sensor/0", "/motorised/3/sensor/1", "/motorised/4/sensor/0", "/motorised/4/sensor/1" , "", "", "", ""},
            //South
            {"/motorised/5/0/sensor/0", "/motorised/5/0/sensor/1", "/motorised/5/1/sensor/0", "/motorised/5/1/sensor/1", "/motorised/6/sensor/0", "/motorised/6/sensor/1", "", ""},
            //West
            {"/motorised/7/sensor/0", "/motorised/7/sensor/1", "/motorised/8/sensor/0", "/motorised/8/sensor/1" , "", "", "", ""}
        };

        public Motorised(string _topic, string _mqttMessage)
        {
            this.mqttMessage = _mqttMessage;
            this.topic = _topic;

            //looks for bounds of 2d array
            int bound0 = trafficLightGroups.GetUpperBound(0);
            int bound1 = trafficLightGroups.GetUpperBound(1);

            //checks 2d array if it matches the topic, returns value of the row, which is also the direction.
            for (int i = 0; i <= bound0; i++)
            {
                for (int x = 0; x <= bound1; x++)
                {
                    if (topic.Contains(trafficLightGroups[i,x]) && trafficLightGroups[i, x] != "")
                    {
                        this.direction = i;
                    }
                }
            }
        }

    }
}
