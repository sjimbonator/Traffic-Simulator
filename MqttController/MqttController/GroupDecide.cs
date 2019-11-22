using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

namespace MqttController
{
    class GroupDecide
    {
        private string mqttMessage;
        private string topic;
        private string team_id;

        private int[] PriorityGroup;

        private string[,] trafficLightGroups =
        {
            //Group1 2x Noord naar Zuid en 1x Zuid naar Noord
            {"/motorised/1/0/sensor/0", "/motorised/1/0/sensor/1", "/motorised/1/1/sensor/0", "/motorised/1/1/sensor/1", "/motorised/5/0/sensor/0", "/motorised/5/0/sensor/0", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",},
            //Group2 Alle voetgangers sensoren + fietsers sensoren
            {"/foot/0/0/sensor/0", "/foot/0/1/sensor/0", "/foot/1/0/sensor/0", "/foot/1/1/sensor/0", "/foot/2/sensor/0", "/foot/3/sensor/0", "/foot/4/0/sensor/0", "/foot/4/1/sensor/0", "/foot/4/2/sensor/0", "/foot/5/0/sensor/0", "/foot/5/1/sensor/0", "/foot/5/2/sensor/0", "/foot/6/sensor/0", "/foot/7/sensor/0", "cycle/0/sensor/0", "cycle/1/sensor/0", "cycle/2/sensor/0", "cycle/3/0/sensor/0", "cycle/3/1/sensor/0", "cycle/4/sensor/0", "cycle/5/sensor/0"},
            //Group3
            {"/motorised/3/sensor/0", "/motorised/3/sensor/1", "/motorised/4/sensor/0", "/motorised/4/sensor/1", "/motorised/5/1/sensor/0", "/motorised/5/1/sensor/1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
            //Group4
            {"/motorised/8/sensor/0", "/motorised/8/sensor/1", "/motorised/7/sensor/0", "/motorised/7/sensor/1", "/motorised/2/sensor/0", "/motorised/2/sensor/1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" },
            //Group5
            {"/motorised/6/sensor/0", "/motorised/6/sensor/1", "/motorised/0/sensor/0", "/motorised/0/sensor/1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" },
            //Group6
            {"/motorised/3/sensor/0", "/motorised/3/sensor/1", "/motorised/4/sensor/0", "/motorised/4/sensor/1", "/motorised/5/1/sensor/0", "/motorised/5/1/sensor/1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""},
            //Group7
            {"/motorised/3/sensor/0", "/motorised/3/sensor/1", "/motorised/4/sensor/0", "/motorised/4/sensor/1", "/motorised/5/1/sensor/0", "/motorised/5/1/sensor/1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" }
        };

        public GroupDecide(string topic, string mqttMessage, string team_id)
        {
            this.mqttMessage = mqttMessage;
            this.topic = topic;
            this.team_id = team_id;

            //looks for bounds of 2d array
            int bound0 = trafficLightGroups.GetUpperBound(0);
            int bound1 = trafficLightGroups.GetUpperBound(1);

            //checks 2d array if it matches the topic, returns value of the row
            for (int i = 0; i <= bound0; i++)
            {
                for (int x = 0; x <= bound1; x++)
                {
                    if (topic.Contains(trafficLightGroups[i, x]) && trafficLightGroups[i, x] != "")
                    {
                        //if(i = 5)
                        //{
                        //    //track.publish
                        //}
                        //elseif(i == 6){

                        //    //vessel code
                        //}

                        PriorityGroup[i]++;
                        Console.WriteLine("Added to prioritygroup");
                    }
                }
            }
        }

        private void checkGroup()
        {
            MqttPublish publishTrafficLight = new MqttPublish();

            int maxValue = PriorityGroup.Max();
            int maxIndex = PriorityGroup.ToList().IndexOf(maxValue);

            Thread t = new Thread(() => PublishMotor(maxIndex));
            t.Start();
        }

        private void PublishMotor(int i)
        {
            MqttPublish publishTrafficLight = new MqttPublish();
            

        }

    }
}
