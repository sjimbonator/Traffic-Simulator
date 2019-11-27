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
    class Lane
    {
        private string[][] sensors;
        private string group;
        public string GetGroup() { return group; }
        private string trafficLightTopic;
        private string trafficLightMessage = "0";
        private Thread publishThread;

        //Array of all lanes that can cross at the same time as this lane.
        private string[] groupedLanes;
        public string[] GetGroupedLanes() { return groupedLanes; }

        private void Publish()
        {
            // Create Client instance
            MqttClient client = new MqttClient(Program.brokerAddress);

            byte code = client.Connect(Guid.NewGuid().ToString());
            Console.WriteLine(trafficLightTopic);

            ushort msgId = client.Publish(trafficLightTopic, // topic
                              Encoding.UTF8.GetBytes(trafficLightMessage), // message body
                              MqttMsgBase.QOS_LEVEL_AT_LEAST_ONCE, // QoS level
                              false); // retained
        }

        public Lane(string group, int laneSize, int sensorAmount, int[] motorisedNumbers, int[] cycleNumbers, int[] footNumbers)
        {
            int id = 0;
            sensors = new string[laneSize][];
            for (int i = 0; i < laneSize; i++)
            {
                sensors[i] = new string[sensorAmount];
                for (int j = 0; j < sensorAmount; j++)
                {
                    sensors[i][j] =group + "/sensor/" + Convert.ToString(id);
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

        public int GetPriority()
        {
            //WIP er kan beter een systeem komen dat telt hoelang de sensors al aan staan
            int priority = 0;
            foreach (string[] lane in sensors)
            {
                foreach (string sensor in lane)
                {
                    string value = "";
                    if (Program.messages.TryGetValue(sensor, out value)) { if (value == "1") { priority++; } }
                }
            }
            return priority;
        }

        public void RedLight()
        {
            trafficLightMessage = "0";
            publishThread = new Thread(Publish);
            publishThread.Start();
        }
        public void OrangeLight()
        {
            trafficLightMessage = "1";
            publishThread = new Thread(Publish);
            publishThread.Start();
        }
        public void GreenLight()
        {
            trafficLightMessage = "2";
            publishThread = new Thread(Publish);
            publishThread.Start();
        }
    }
}
