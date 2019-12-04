using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using uPLibrary.Networking.M2Mqtt;
using uPLibrary.Networking.M2Mqtt.Messages;

namespace Controller
{
    class SmallLane : Lane
    {
        private string[][] sensors;
        private string group;
        public string GetGroup() { return group; }

        private string trafficLightTopic;
        private string trafficLightMessage = "0";
        private Thread publishThread;
        private int[] priority;
        private bool greenLight = false;

        public int GetPriority()
        {
            int totalPriority = 0;
            foreach (int priority in this.priority) { totalPriority += priority; }
            return totalPriority;
        }

        public bool isReady() { return true; }

        //Array of all lanes that can cross at the same time as this lane.
        private string[] groupedLanes;
        public string[] GetGroupedLanes() { return groupedLanes; }

        private void Publish()
        {
            ushort msgId = Program.client.Publish(trafficLightTopic, // topic
                       Encoding.UTF8.GetBytes(trafficLightMessage), // message body
                       MqttMsgBase.QOS_LEVEL_AT_LEAST_ONCE, // QoS level
                       false); // retained}
        }

        public SmallLane(string group, int laneSize, int sensorAmount, int[] motorisedNumbers, int[] cycleNumbers, int[] footNumbers)
        {
            int id = 0;
            sensors = new string[laneSize][];
            priority = new int[laneSize * sensorAmount];
            for (int i = 0; i < priority.Length; i++) { priority[i] = 0; }
            for (int i = 0; i < laneSize; i++)
            {
                sensors[i] = new string[sensorAmount];
                for (int j = 0; j < sensorAmount; j++)
                {
                    sensors[i][j] = Program.group_id + "/" + group + "/sensor/" + Convert.ToString(id);
                    id++;
                }
            }

            groupedLanes = new string[motorisedNumbers.Length + cycleNumbers.Length + footNumbers.Length];

            for (int i = 0; i < motorisedNumbers.Length; i++)
            {
                groupedLanes[i] = "motorised/" + Convert.ToString(motorisedNumbers[i]);
            }

            for (int i = 0; i < cycleNumbers.Length; i++)
            {
                groupedLanes[i + motorisedNumbers.Length] = "cycle/" + Convert.ToString(cycleNumbers[i]);
            }

            for (int i = 0; i < footNumbers.Length; i++)
            {
                groupedLanes[i + motorisedNumbers.Length + cycleNumbers.Length] = "foot/" + Convert.ToString(footNumbers[i]);
            }

            this.group = group;
            trafficLightTopic = Program.group_id + "/" + group + "/traffic_light/0";
        }

        public void CheckPriority()
        {
            Console.WriteLine("Priority van: " + GetGroup() + " = ");
            for (int i = 0; i < sensors.Length; i++)
            {
                for (int j = 0; j < sensors[i].Length; j++)
                {
                    Console.WriteLine(i + j + "  " + sensors[i][j]);
                    int increment = 2 / sensors.Length;
                    string value = "";
                    if (Program.messages.TryGetValue(sensors[i][j], out value))
                    {
                        Console.WriteLine("true");
                        if (value == "1" && !greenLight) { priority[i + j] += increment; }
                        else { priority[i + j] = 0; }
                    }




                }
            }
            Console.WriteLine(GetPriority());




        }

        public void RedLight()
        {
            trafficLightMessage = "0";
            publishThread = new Thread(Publish);
            publishThread.Start();
            greenLight = false;
        }
        public void OrangeLight()
        {
            trafficLightMessage = "1";
            publishThread = new Thread(Publish);
            publishThread.Start();
            greenLight = false;
        }
        public void GreenLight()
        {
            trafficLightMessage = "2";
            publishThread = new Thread(Publish);
            publishThread.Start();
            greenLight = true;
        }
    }
}
