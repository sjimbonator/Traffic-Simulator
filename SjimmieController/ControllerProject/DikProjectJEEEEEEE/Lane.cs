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
        private string trafficLightTopic;
        private string trafficLightMessage = "0";
        private Thread publishThread;

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

            this.group = group;
            trafficLightTopic = Program.group_id + "/" + group + "/traffic_light/0";
            GreenLight();
        }

        public int GetPriority()
        {
            //WIP
            return 100;
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
