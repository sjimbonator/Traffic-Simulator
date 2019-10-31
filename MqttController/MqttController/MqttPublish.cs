using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using uPLibrary.Networking.M2Mqtt;
using uPLibrary.Networking.M2Mqtt.Messages;

namespace MqttController
{
    class MqttPublish
    {
        private string team_id { get; set; }
        private static string brokerAddress = "arankieskamp.com";

        public void publish(string message, string topic)
        {
            // Create Client instance
            MqttClient myClient = new MqttClient(brokerAddress);

            byte code = myClient.Connect(Guid.NewGuid().ToString());

            if (myClient.IsConnected == true)
            {
                Console.WriteLine("Connected to " + brokerAddress);
            }

            myClient.Publish(topic, Encoding.UTF8.GetBytes(message), MqttMsgBase.QOS_LEVEL_AT_LEAST_ONCE, false);
            myClient.Disconnect();
        }
    }
}
