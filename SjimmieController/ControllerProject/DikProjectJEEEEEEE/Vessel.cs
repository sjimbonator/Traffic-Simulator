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
    class Vessel : Lane
    {
        private string group;
        public string GetGroup() { return group; }

        private Thread publishThread;

        private string eastSensor;
        private string westSensor;

        private string deckSensor;
        private string bridgeSensor;

        private int eastPriority = 0;
        private int westPriority = 0;
        public int GetPriority() { return (eastPriority + westPriority) / 10; }

        bool ready = true;
        public bool isReady() { return ready; }

        //Array of all lanes that can cross at the same time as this lane.
        private string[] groupedLanes;
        public string[] GetGroupedLanes() { return groupedLanes; }

        private void Publish(string topic, string message)
        {
            ushort msgId = Program.client.Publish(topic, // topic
                       Encoding.UTF8.GetBytes(message), // message body
                       MqttMsgBase.QOS_LEVEL_AT_LEAST_ONCE, // QoS level
                       false); // retained}
        }

        public Vessel(string group, int[] motorisedNumbers, int[] cycleNumbers, int[] footNumbers)
        {

            eastSensor = Program.group_id + "/" + group + "/sensor/" + 0;
            westSensor = Program.group_id + "/" + group + "/sensor/" + 2;

            deckSensor = Program.group_id + "/" + group + "/sensor/" + 3;
            bridgeSensor = Program.group_id + "/" + group + "/sensor/" + 1;

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
        }

        public void CheckPriority()
        {
            int increment = 1;
            string value;
            if (Program.messages.TryGetValue(eastSensor, out value))
            {
                if (value == "1") { eastPriority += increment; }
                else { eastPriority = 0; }
            }
            if (Program.messages.TryGetValue(westSensor, out value))
            {
                if (value == "1") { westPriority += increment; }
                else { westPriority = 0; }
            }
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
