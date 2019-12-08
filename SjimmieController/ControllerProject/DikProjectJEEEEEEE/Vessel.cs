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
    class Vessel
    {
        private string group;
        public string GetGroup() { return group; }

        private Thread publishThread;

        private string eastSensor;
        private string westSensor;

        private string deckSensor;
        private string bridgeSensor;

        private string warning_light;
        private string barrier;
        private string deck;

        private string eastBoat_light;
        private string westBoat_light;

        private int eastPriority = 0;
        private int westPriority = 0;
        public int GetPriority() {  return ((eastPriority + westPriority) / 10); }

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

        private void openBridge()
        {
            string value;
            running = true;
            Publish(warning_light, "1");
            bool bridgeIsEmpty = false;
            while(!bridgeIsEmpty)
            {
                bridgeIsEmpty = true;
                if (Program.messages.TryGetValue(deckSensor, out value))
                {
                    if (value == "1") { bridgeIsEmpty = false; }
                }
            }
            Publish(barrier, "1");
            Thread.Sleep(4000);
            Publish(deck, "1");
            Thread.Sleep(10000);
            if (Program.messages.TryGetValue(eastSensor, out value))
            {
                if (value == "1")
                {
                    Publish(eastBoat_light, "1");
                    Thread.Sleep(8000);
                    Publish(eastBoat_light, "0");
                }
            }
            if (Program.messages.TryGetValue(westSensor, out value))
            {
                if (value == "1")
                {
                    Publish(westBoat_light, "1");
                    Thread.Sleep(8000);
                    Publish(westBoat_light, "0");
                }
            }
            closeBridge();
        }

        private void closeBridge()
        {
            string value;
            bool waterIsEmpty = false;
            while (!waterIsEmpty)
            {
                waterIsEmpty = true;
                if (Program.messages.TryGetValue(bridgeSensor, out value))
                {
                    if (value == "1") { waterIsEmpty = false; }
                }
            }
            
            Publish(deck, "0");
            Thread.Sleep(10000);
            Publish(barrier, "0");
            Thread.Sleep(4000);
            Publish(warning_light, "0");

            running = false;

        }

        public Vessel(string group, int[] motorisedNumbers, int[] cycleNumbers, int[] footNumbers, int[] vesselNumbers, int[] trackNumbers)
        {

            eastSensor = Program.group_id + "/" + group + "/sensor/" + 0;
            westSensor = Program.group_id + "/" + group + "/sensor/" + 2;

            deckSensor = Program.group_id + "/" + group + "/sensor/" + 3;
            bridgeSensor = Program.group_id + "/" + group + "/sensor/" + 1;

            warning_light = Program.group_id + "/" + group + "/warning_light/" + 0;
            barrier = Program.group_id + "/" + group + "/barrier/" + 0;
            deck = Program.group_id + "/" + group + "/deck/" + 0;

            eastBoat_light = Program.group_id + "/" + group + "/boat_light/" + 0;
            westBoat_light = Program.group_id + "/" + group + "/boat_light/" + 1;


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
            if (Program.messages.TryGetValue(eastSensor, out value))
            {
                if (value == "1") { eastPriority += 1; }
                else { eastPriority = 0; }
            }
            if (Program.messages.TryGetValue(westSensor, out value))
            {
                if (value == "1") { westPriority += 1; }
                else { westPriority = 0; }
            }
        }

        public void HandleBridge()
        {
            publishThread = new Thread(closeBridge);
            publishThread.Start();
        }
    }
}
