using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace MqttController
{
    class Motorised
    {
        private string mqttMessage;
        private string topic;
        private string team_id;
        private int direction;
        //2d array with sensor directions
        private string[,] trafficLightGroups =
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

        public Motorised(string topic, string mqttMessage, string team_id)
        {
            this.mqttMessage = mqttMessage;
            this.topic = topic;
            this.team_id = team_id;

            //looks for bounds of 2d array
            int bound0 = trafficLightGroups.GetUpperBound(0);
            int bound1 = trafficLightGroups.GetUpperBound(1);

            //checks 2d array if it matches the topic, returns value of the row, which is also the direction.
            for (int i = 0; i <= bound0; i++)
            {
                for (int x = 0; x <= bound1; x++)
                {
                    if (topic.Contains(trafficLightGroups[i, x]) && trafficLightGroups[i, x] != "")
                    {
                        direction = i;

                        PublishMotor();
                    }
                }
            }
        }

        private void PublishMotor()
        {
            MqttPublish publishTrafficLight = new MqttPublish();

            switch (direction)
            {
                case 0: //north
                    publishTrafficLight.publish("2", team_id + "/motorised/0/traffic_light/0");
                    publishTrafficLight.publish("2", team_id + "/motorised/1/0/traffic_light/0");
                    publishTrafficLight.publish("2", team_id + "/motorised/1/1/traffic_light/0");
                    publishTrafficLight.publish("2", team_id + "/motorised/2/traffic_light/0");

                    System.Threading.Thread.Sleep(7500);

                    publishTrafficLight.publish("1", team_id + "/motorised/0/traffic_light/0");
                    publishTrafficLight.publish("1", team_id + "/motorised/1/0/traffic_light/0");
                    publishTrafficLight.publish("1", team_id + "/motorised/1/1/traffic_light/0");
                    publishTrafficLight.publish("1", team_id + "/motorised/2/traffic_light/0");

                    System.Threading.Thread.Sleep(2000);

                    publishTrafficLight.publish("0", team_id + "/motorised/0/traffic_light/0");
                    publishTrafficLight.publish("0", team_id + "/motorised/1/0/traffic_light/0");
                    publishTrafficLight.publish("0", team_id + "/motorised/1/1/traffic_light/0");
                    publishTrafficLight.publish("0", team_id + "/motorised/2/traffic_light/0");

                    break;

                case 1: //east
                    publishTrafficLight.publish("2", team_id + "/motorised/3/traffic_light/0");
                    publishTrafficLight.publish("2", team_id + "/motorised/4/traffic_light/0");

                    System.Threading.Thread.Sleep(7500);

                    publishTrafficLight.publish("1", team_id + "/motorised/3/traffic_light/0");
                    publishTrafficLight.publish("1", team_id + "/motorised/4/traffic_light/0");

                    System.Threading.Thread.Sleep(2000);

                    publishTrafficLight.publish("0", team_id + "/motorised/3/traffic_light/0");
                    publishTrafficLight.publish("0", team_id + "/motorised/4/traffic_light/0");

                    break;
                case 2: //south
                    publishTrafficLight.publish("2", team_id + "/motorised/5/0/traffic_light/0");
                    publishTrafficLight.publish("2", team_id + "/motorised/5/1/traffic_light/0");
                    publishTrafficLight.publish("2", team_id + "/motorised/6/traffic_light/0");

                    System.Threading.Thread.Sleep(7500);

                    publishTrafficLight.publish("1", team_id + "/motorised/5/0/traffic_light/0");
                    publishTrafficLight.publish("1", team_id + "/motorised/5/1/traffic_light/0");
                    publishTrafficLight.publish("1", team_id + "/motorised/6/traffic_light/0");

                    System.Threading.Thread.Sleep(1500);

                    publishTrafficLight.publish("0", team_id + "/motorised/5/0/traffic_light/0");
                    publishTrafficLight.publish("0", team_id + "/motorised/5/1/traffic_light/0");
                    publishTrafficLight.publish("0", team_id + "/motorised/6/traffic_light/0");

                    break;
                case 3: //west
                    publishTrafficLight.publish("2", team_id + "/motorised/7/traffic_light/0");
                    publishTrafficLight.publish("2", team_id + "/motorised/8/traffic_light/0");

                    System.Threading.Thread.Sleep(7500);

                    publishTrafficLight.publish("1", team_id + "/motorised/7/traffic_light/0");
                    publishTrafficLight.publish("1", team_id + "/motorised/8/traffic_light/0");

                    System.Threading.Thread.Sleep(2000);

                    publishTrafficLight.publish("0", team_id + "/motorised/7/traffic_light/0");
                    publishTrafficLight.publish("0", team_id + "/motorised/8/traffic_light/0");

                    break;
            }
        }
    }
}
