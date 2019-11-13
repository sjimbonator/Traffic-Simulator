using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using uPLibrary.Networking.M2Mqtt;
using uPLibrary.Networking.M2Mqtt.Messages;

namespace MqttController
{
    class MqttSubscribe
    {
        private string team_id { get; set; }
        private static string brokerAddress = "arankieskamp.com";

        public void subscribe(string _team_id)
        {
            this.team_id = _team_id;

            try
            {
                // Create Client instance
                MqttClient myClient = new MqttClient(brokerAddress);

                byte code = myClient.Connect(Guid.NewGuid().ToString());

                if (myClient.IsConnected == true)
                {
                    Console.WriteLine("Connected to " + brokerAddress);
                }

                myClient.Subscribe(new String[] { team_id + "/#" }, new byte[] { MqttMsgBase.QOS_LEVEL_AT_MOST_ONCE });

                Console.WriteLine("Subscribed to all topics of team: " + team_id);
                Console.WriteLine("-------------------------------------------------");

                myClient.MqttMsgPublishReceived += client_recievedMessage;
            }

            catch
            {
                Console.WriteLine("Connection failed");
            }


        }

        public void client_recievedMessage(object sender, MqttMsgPublishEventArgs e)
        {
            // Handle message received
            string mqttMessage = Encoding.Default.GetString(e.Message);
            string topic = e.Topic;

            Console.WriteLine("Message received from topic: " + topic);
            Console.WriteLine("Message: " + mqttMessage);
            Console.WriteLine("");

            //splits topic string to determine topic type
            string[] delimiters = { "/" };
            string[] pieces = topic.Split(delimiters, StringSplitOptions.None);

            switch (pieces[1])
            {
                case "foot":
                    break;
                case "cycle":
                    break;
                case "motorised":
                    if(mqttMessage == "1")
                    {
                        Motorised motorised = new Motorised(topic, mqttMessage, team_id);
                    }
                    break;
                case "vessel":
                    break;
                case "track":
                    break;
            }

        }
    }
}
