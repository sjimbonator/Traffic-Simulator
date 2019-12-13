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
        public int GetPriority()
        {
            if (running) { return 0; }
            return eastPriority + westPriority;
        }

        bool running = false;
        public bool IsRunning() { return running; }

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
        private void WaitForValue(string topic, string desiredValue)
        {
            bool valueFound = false;
            while (!valueFound)
            {
                valueFound = false;
                string value;
                if (Program.messages.TryGetValue(topic, out value))
                {
                    if (value == desiredValue) { valueFound = true; }
                }
                else if (desiredValue == "0") { valueFound = true; }
            }
        }

        private void openTrack()
        {
            Publish(barrier, "0");
            Thread.Sleep(4000);
            Publish(warning_light, "0");
            Thread.Sleep(4000);
            running = false;
        }

        private void closeTrack()
        {
            string value;
            bool east = false;
            if (eastPriority > 0) { east = true; }
            else if (westPriority > 0) { east = false; }
            Publish(warning_light, "1");
            Thread.Sleep(2000);
            Publish(barrier, "1");
            Thread.Sleep(4000);
            if (east) { Publish(eastLight, "1"); }
            else  { Publish(westLight, "1"); }
            WaitForValue(passSensor, "1");
            if (east) { Publish(eastLight, "0"); WaitForValue(westSensor, "1"); WaitForValue(westSensor, "0"); }
            else { Publish(westLight, "0"); WaitForValue(eastSensor, "1"); WaitForValue(eastSensor, "0"); }
            openTrack();

        }

        public Track(string group, int[] motorisedNumbers, int[] cycleNumbers, int[] footNumbers)
        {

            eastSensor = Program.group_id + "/" + group + "/sensor/" + 0;
            passSensor = Program.group_id + "/" + group + "/sensor/" + 1;
            westSensor = Program.group_id + "/" + group + "/sensor/" + 2;

            warning_light = Program.group_id + "/" + group + "/warning_light/" + 0;
            barrier = Program.group_id + "/" + group + "/barrier/" + 0;

            eastLight = Program.group_id + "/" + group + "/train_light/" + 0;
            westLight = Program.group_id + "/" + group + "/train_light/" + 1;


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

        public void HandleTrack()
        {
            running = true;
            publishThread = new Thread(closeTrack);
            publishThread.Start();
        }
    }
}

