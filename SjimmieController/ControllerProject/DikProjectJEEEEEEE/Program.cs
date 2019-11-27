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
        public static string brokerAddress = "arankieskamp.com";

        public static bool BetweenRanges(int a, int b, int number)
        {
            return (a <= number && number <= b);
        }

        public static Dictionary<string, string> messages = new Dictionary<string, string>();
        private static Dictionary<string, Lane> lanes = new Dictionary<string, Lane>();

        private static void Subscribe()
        {
            //Mqtt Connection
            try
            {
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
                string value = "";
                if (messages.TryGetValue(topic, out value))
                {
                    messages[topic] = mqttMessage;
                }
                else { messages.Add(topic, mqttMessage); }
                
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
            
            lanes.Add("motorised/0", new Lane("motorised/0", 1, 2, new int[] {1, 2, 3, 6, 8}, new int[] {2,3,4}, new int[] {3,4,5,6}));
            lanes.Add("motorised/1", new Lane("motorised/1", 2, 2, new int[] {0,2,3,5}, new int[] {1,4}, new int[] {2,6}));
            lanes.Add("motorised/2", new Lane("motorised/2", 1, 2, new int[] { }, new int[] { }, new int[] { }));
            lanes.Add("motorised/3", new Lane("motorised/3", 1, 2, new int[] { }, new int[] { }, new int[] { }));
            lanes.Add("motorised/4", new Lane("motorised/4", 1, 2, new int[] { }, new int[] { }, new int[] { }));
            lanes.Add("motorised/5", new Lane("motorised/5", 2, 2, new int[] { }, new int[] { }, new int[] { }));
            lanes.Add("motorised/6", new Lane("motorised/6", 1, 2, new int[] { }, new int[] { }, new int[] { }));
            lanes.Add("motorised/7", new Lane("motorised/7", 1, 2, new int[] { }, new int[] { }, new int[] { }));
            lanes.Add("motorised/8", new Lane("motorised/8", 1, 2, new int[] { }, new int[] { }, new int[] { }));

            lanes.Add("cycle/0", new Lane("cycle/0", 1, 1));
            lanes.Add("cycle/1", new Lane("cycle/1", 1, 1));
            lanes.Add("cycle/2", new Lane("cycle/2", 1, 1));
            lanes.Add("cycle/3", new Lane("cycle/3", 1, 1));
            lanes.Add("cycle/4", new Lane("cycle/4", 1, 1));

            lanes.Add("foot/0", new Lane("foot/0", 1, 1));
            lanes.Add("foot/1", new Lane("foot/1", 1, 1));
            lanes.Add("foot/2", new Lane("foot/2", 1, 1));
            lanes.Add("foot/3", new Lane("foot/3", 1, 1));
            lanes.Add("foot/4", new Lane("foot/4", 1, 1));
            lanes.Add("foot/5", new Lane("foot/5", 1, 1));
            lanes.Add("foot/6", new Lane("foot/6", 1, 1));

            Subscribe();

            //Main loop
            Console.WriteLine("Starting Main Loop use the enter key to exit.");
            while (Console.ReadKey().Key != ConsoleKey.Enter)
            {

            }
            CleanUp();

        }
    }
}
