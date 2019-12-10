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
    class Program
    {
        public static int group_id;
        public static string brokerAddress = "arankieskamp.com";

        private static Vessel boat;
        private static Track train;

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

        private static Lane FindHighestPrio( string[] keys)
        {
            Lane lane = null;
            foreach (string key in keys)
            {
                Lane value;
                if (lanes.TryGetValue(key, out value))
                {
                    if (lane == null) { lane = value; continue; }
                    else if (value.GetPriority() >= lane.GetPriority()) { lane = value; }
                }
            }

            return lane;
        }

        private static List<Lane> FindCompatibleHighestPrio(string[] lanes)
        {
            List<Lane> prioLanes= new List<Lane>();
            //Adds the lane with the highest priority of all the lanes to the list.
            Lane highestPrio = FindHighestPrio(lanes);
            if (highestPrio.GetPriority() == 0) { return prioLanes; }
            prioLanes.Add(highestPrio);

            List<Lane> compatibleLanes(string[] compatibleKeys)
            {
                Lane prio = FindHighestPrio(compatibleKeys);
                bool isCompatible = true;
                if (lanes.Contains(prio.GetGroup()))
                {
                    foreach (Lane lane in prioLanes)
                    {
                        if (!(lane.GetGroupedLanes().Contains(prio.GetGroup()))) { isCompatible = false; break; }
                    }
                    if (isCompatible) { prioLanes.Add(prio); }
                }
                

                string stringToRemove = prio.GetGroup();
                compatibleKeys = compatibleKeys.Where(val => val != stringToRemove).ToArray();
                if (compatibleKeys.Length == 0) { return prioLanes; }
                return compatibleLanes(compatibleKeys);
            }

            compatibleLanes(highestPrio.GetGroupedLanes());
            return prioLanes;
        }

        private static void UpdateLanes()
        {
            while (true)
            {
                foreach (KeyValuePair<string, Lane> entry in lanes)
                {
                    entry.Value.CheckPriority();
                }

                boat.CheckPriority();
                train.CheckPriority();
            }
            
        }

        private static void SetAllToRed()
        {
            foreach (KeyValuePair<string, Lane> entry in lanes)
            {
                entry.Value.RedLight();
            }
        }

        private static void SetListToRed(List<Lane> laneList)
        {
            WaitUntilReady(laneList);
            foreach (Lane lane in laneList)
            {
                lane.RedLight();
            }
        }

        private static void SetListToGreen(List<Lane> laneList)
        {
            WaitUntilReady(laneList);
            foreach (Lane lane in laneList)
            {
                lane.GreenLight();
            }
        }

        private static void SetListToOrange(List<Lane> laneList)
        {
            WaitUntilReady(laneList);
            foreach (Lane lane in laneList)
            {
                lane.OrangeLight();
            }
        }

        private static bool CheckList(List<Lane> laneList)
        {
            bool returnbool = true;
            foreach(Lane lane in laneList)
            {
                if (!lane.isReady()) { returnbool = false; break; }
            }

            return returnbool;
        }

        private static void WaitUntilReady(List<Lane> laneList)
        {
            bool ready = false;
            while (!ready){ ready = CheckList(laneList); }
        }

        private static string[] GetAvailableLanes()
        {
            string[] availableKeys = lanes.Keys.ToArray();
            if (!boat.IsRunning() && boat.GetPriority() > 1 && boat.IsReady()) { boat.HandleBridge();}
            if (boat.IsRunning())
            {
                List<string> tempKeys = new List<string>();
                string[] boatKeys = boat.GetGroupedLanes();
                foreach (string key in availableKeys)
                {
                    if (boatKeys.Contains(key)) { tempKeys.Add(key); }
                }
                availableKeys = tempKeys.ToArray();
            }
            if (!train.IsRunning() && train.GetPriority() > 1) { train.HandleTrack(); }
            if (train.IsRunning())
            {
                List<string> tempKeys = new List<string>();
                string[] trainKeys = train.GetGroupedLanes();
                foreach (string key in availableKeys)
                {
                    if (trainKeys.Contains(key)) { tempKeys.Add(key); }
                }
                availableKeys = tempKeys.ToArray();
            }
            return availableKeys;
        }

        private static void CleanUp()
        {
            //WIP
            Environment.Exit(0);
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
            
            lanes.Add("motorised/0", new Lane("motorised/0", 1, 2, new int[] {1,2,3,6,8}, new int[] {2,3,4}, new int[] {3,4,5,6}));
            lanes.Add("motorised/1", new Lane("motorised/1", 2, 2, new int[] {0,2,3,5}, new int[] {1,4}, new int[] {2,6}));
            lanes.Add("motorised/2", new Lane("motorised/2", 1, 2, new int[] {0,1,3,4,5,7,8}, new int[] {1,2,3}, new int[] {2,3,4,5}));
            lanes.Add("motorised/3", new Lane("motorised/3", 1, 2, new int[] {0,1,2,4,6,8}, new int[] {2,3,4}, new int[] {3,4,5,6}));
            lanes.Add("motorised/4", new Lane("motorised/4", 1, 2, new int[] {2,3,7}, new int[] {0,4}, new int[] {0,1,6}));
            lanes.Add("motorised/5", new Lane("motorised/5", 2, 2, new int[] {1,2,6,8}, new int[] {4}, new int[] {6}));
            lanes.Add("motorised/6", new Lane("motorised/6", 1, 2, new int[] {0,3,5,8}, new int[] {0,1}, new int[] {0,1,2}));
            lanes.Add("motorised/7", new Lane("motorised/7", 1, 2, new int[] {2,3,4,8}, new int[] {1,2,3}, new int[] {2,3,4,5}));
            lanes.Add("motorised/8", new Lane("motorised/8", 1, 2, new int[] {0,2,3,5,6,7}, new int[] {0,1}, new int[] {0,1,2}));

            lanes.Add("cycle/0", new Lane("cycle/0", 1, 1, new int[] {4,6,8}, new int[] {0,1,2,3,4}, new int[] {0,1,2,3,4,5,6}));
            lanes.Add("cycle/1", new Lane("cycle/1", 1, 1, new int[] {1,2,6,7,8}, new int[] {0,1,2,3,4}, new int[] {0,1,2,3,4,5,6}));
            lanes.Add("cycle/2", new Lane("cycle/2", 1, 1, new int[] {0,2,3,7}, new int[] {0,1,2,3,4}, new int[] {0,1,2,3,4,5,6}));
            lanes.Add("cycle/3", new Lane("cycle/3", 1, 1, new int[] {0,2,3,7}, new int[] {0,1,2,3,4}, new int[] {0,1,2,3,4,5,6}));
            lanes.Add("cycle/4", new Lane("cycle/4", 1, 1, new int[] {0,1,3,4,5}, new int[] {0,1,2,3,4}, new int[] {0,1,2,3,4,5,6}));

            lanes.Add("foot/0", new Lane("foot/0", 1, 1, new int[] {4,6,8}, new int[] {0,1,2,3,4}, new int[] {0,1,2,3,4,5,6}));
            lanes.Add("foot/1", new Lane("foot/1", 1, 1, new int[] {4,6,8}, new int[] {0,1,2,3,4}, new int[] {0,1,2,3,4,5,6}));
            lanes.Add("foot/2", new Lane("foot/2", 1, 1, new int[] {1,2,6,7,8}, new int[] {0,1,2,3,4}, new int[] {0,1,2,3,4,5,6}));
            lanes.Add("foot/3", new Lane("foot/3", 1, 1, new int[] {0,2,3,7}, new int[] {0,1,2,3,4}, new int[] {0,1,2,3,4,5,6}));
            lanes.Add("foot/4", new Lane("foot/4", 1, 1, new int[] {0,2,3,7}, new int[] {0,1,2,3,4}, new int[] {0,1,2,3,4,5,6}));
            lanes.Add("foot/5", new Lane("foot/5", 1, 1, new int[] {0,2,3,7}, new int[] {0,1,2,3,4}, new int[] {0,1,2,3,4,5,6}));
            lanes.Add("foot/6", new Lane("foot/6", 1, 1, new int[] {0,1,3,4,5}, new int[] {0,1,2,3,4}, new int[] {0,1,2,3,4,5,6}));

            boat = new Vessel("vessel/0", new int[] { 0, 1, 2, 4, 6, 8 }, new int[] { 0, 1, 2, 3, 4 }, new int[] { 0, 1, 2, 3, 4, 5, 6 });
            train = new Track("track/0", new int[] { 0, 2, 3, 7 }, new int[] { 0, 1, 2, 3, 4 }, new int[] { 0, 1, 2, 3, 4, 5, 6 });

            Thread subscribeThread = new Thread(Subscribe);
            subscribeThread.Start();
            //SetAllToRed();
            Thread updateThread = new Thread(UpdateLanes);
            updateThread.Start();
            //Main loop
            Console.WriteLine("Running...");
            while (true)
            {
                string[] availableKeys = GetAvailableLanes();
                List<Lane> laneList = FindCompatibleHighestPrio(availableKeys);
                SetListToGreen(laneList);
                System.Threading.Thread.Sleep(7000);
                SetListToOrange(laneList);
                System.Threading.Thread.Sleep(4000);
                SetListToRed(laneList);
                System.Threading.Thread.Sleep(1000);
            }
            CleanUp();

        }
    }
}
