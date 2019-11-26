using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using uPLibrary.Networking.M2Mqtt;
using uPLibrary.Networking.M2Mqtt.Messages;

namespace Controller
{
    class Program
    {
        public static int group_id;

        public static bool BetweenRanges(int a, int b, int number)
        {
            return (a <= number && number <= b);
        }

        private static Queue<MqttMsgPublishEventArgs> messageProcessingQueue = new Queue<MqttMsgPublishEventArgs>();

        private static void ConnectToMqtt()
        {
            //Mqtt Connection
            try
            {
                string brokerAddress = "arankieskamp.com";
                string id = Convert.ToString(Program.group_id);
                // Create Client instance
                MqttClient myClient = new MqttClient(brokerAddress);

                byte code = myClient.Connect(Guid.NewGuid().ToString());

                if (myClient.IsConnected == true)
                {
                    Console.WriteLine("Connected to " + brokerAddress);
                }

                myClient.Subscribe(new String[] { id + "/#" }, new byte[] { MqttMsgBase.QOS_LEVEL_AT_MOST_ONCE });

                Console.WriteLine("Subscribed to all topics of team: " + id);
                Console.WriteLine("-------------------------------------------------");

                myClient.MqttMsgPublishReceived += client_recievedMessage;
            }



            catch
            {
                Console.WriteLine("Connection failed");
            }

            void client_recievedMessage(object sender, MqttMsgPublishEventArgs e)
            {
                // Handle message received
                string mqttMessage = Encoding.Default.GetString(e.Message);
                string topic = e.Topic;

                Console.WriteLine("Message received from topic: " + topic);
                Console.WriteLine("Message: " + mqttMessage);
                Console.WriteLine("");

                messageProcessingQueue.Enqueue(e);

            }


        }


        private static void CleanUp()
        {
            //WIP
        }

        static void Main(string[] args)
        {
            //Ask and check for valid group id.
            Console.WriteLine("What is your group ID?");
            while (!int.TryParse(Console.ReadLine(), out group_id))
            {
                Console.Clear();
                Console.WriteLine("You entered an invalid number");
                Console.Write("What is your group ID?");
                
            }
            
            ConnectToMqtt();

            //Main loop
            Console.WriteLine("Starting Main Loop use the enter key to exit.");
            while (Console.ReadKey().Key != ConsoleKey.Enter)
            {

            }
            CleanUp();

        }
    }
}
