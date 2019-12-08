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
    class Track
    {
        private string group;
        public string GetGroup() { return group; }

        private Thread publishThread;

        private string eastSensor;
        private string passSensor;
        private string westSensor;

        private string warning_light;
        private string barrier;

        private string eastLight;
        private string westLight;

        private int eastPriority = 0;
        private int westPriority = 0;
        public int GetPriority() { return eastPriority + westPriority; }

        bool ready = true;
        public bool isReady() { return ready; }

        //Array of all lanes that can cross at the same time as this lane.
        private string[] groupedLanes;
        public string[] GetGroupedLanes() { return groupedLanes; }

        private MqttClient client;

        private void Publish(string topic, string message)
        {
            ushort msgId = client.Publish(topic, // topic
                       Encoding.UTF8.GetBytes(message), // message body
                       MqttMsgBase.QOS_LEVEL_AT_LEAST_ONCE, // QoS level
                       false); // retained}
        }

        private void openTrack()
        {
            ready = false;
            Publish(barrier, "0");
            Thread.Sleep(4000);
            Publish(warning_light, "0");
            ready = true;
        }

        private void closeTrack()
        {
            ready = false;
            string value;
            Publish(warning_light, "1");
            Thread.Sleep(1000);
            Publish(barrier, "1");
            Thread.Sleep(4000);
            if (eastPriority > 0) { Publish(eastLight, "1"); }
            else if (westPriority > 0) { Publish(westLight, "1"); }

            bool passed = false;
            while (!passed)
            {
                if (Program.messages.TryGetValue(passSensor, out value))
                {
                    if (value == "1") { passed = true; }
                }
            }
            ready = true;

        }

        public Track(string group, int[] motorisedNumbers, int[] cycleNumbers, int[] footNumbers, int[] vesselNumbers, int[] trackNumbers)
        {

            eastSensor = Program.group_id + "/" + group + "/sensor/" + 0;
            passSensor = Program.group_id + "/" + group + "/sensor/" + 1;
            westSensor = Program.group_id + "/" + group + "/sensor/" + 2;

            warning_light = Program.group_id + "/" + group + "/warning_light/" + 0;
            barrier = Program.group_id + "/" + group + "/barrier/" + 0;

            eastLight = Program.group_id + "/" + group + "/train_light/" + 0;
            westLight = Program.group_id + "/" + group + "/train_light/" + 1;


            groupedLanes = new string[motorisedNumbers.Length + cycleNumbers.Length + footNumbers.Length + vesselNumbers.Length + trackNumbers.Length];

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

            for (int i = 0; i < vesselNumbers.Length; i++)
            {
                groupedLanes[i + motorisedNumbers.Length + cycleNumbers.Length + footNumbers.Length] = "vessel/" + Convert.ToString(vesselNumbers[i]);
            }

            for (int i = 0; i < trackNumbers.Length; i++)
            {
                groupedLanes[i + motorisedNumbers.Length + cycleNumbers.Length + footNumbers.Length + vesselNumbers.Length] = "track/" + Convert.ToString(trackNumbers[i]);
            }

            this.group = group;
            client = new MqttClient(Program.brokerAddress);
            byte code = client.Connect(Guid.NewGuid().ToString());
        }

        public void CheckPriority()
        {
            string value;
            eastPriority = 0;
            westPriority = 0;
            if (Program.messages.TryGetValue(eastSensor, out value))
            {
                if (value == "1") { eastPriority = int.MaxValue / 2; }
            }
            if (Program.messages.TryGetValue(westSensor, out value))
            {
                if (value == "1") { westPriority = int.MaxValue / 2; }
            }
        }

        public void RedLight()
        {
            publishThread = new Thread(closeTrack);
            publishThread.Start();
        }
        public void OrangeLight() { }
        public void GreenLight()
        {
            publishThread = new Thread(openTrack);
            publishThread.Start();
        }
    }
}

