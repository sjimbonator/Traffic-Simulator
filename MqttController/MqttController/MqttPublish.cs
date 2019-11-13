using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using uPLibrary.Networking.M2Mqtt;
using uPLibrary.Networking.M2Mqtt.Messages;
using System.Diagnostics;

namespace MqttController
{
    class MqttPublish
    {
        private static string brokerAddress = "arankieskamp.com";

        public void publish(string message, string topic)
        {
            // Create Client instance
            MqttClient client = new MqttClient(brokerAddress);

            byte code = client.Connect(Guid.NewGuid().ToString());
            
            ushort msgId = client.Publish(topic, // topic
                              Encoding.UTF8.GetBytes(message), // message body
                              MqttMsgBase.QOS_LEVEL_AT_LEAST_ONCE, // QoS level
                              false); // retained
        }
    }
}
