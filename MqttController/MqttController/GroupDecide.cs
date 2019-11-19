using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MqttController
{
    class GroupDecide
    {
        private string mqttMessage;
        private string topic;
        private string team_id;

        private string[,] trafficLightGroups =
        {
            //Group1 2x Noord naar Zuid en 1x Zuid naar Noord
            {"/motorised/1/0/sensor/0", "/motorised/1/0/sensor/1", "/motorised/1/1/sensor/0", "/motorised/1/1/sensor/1", "/motorised/5/0/sensor/0", "/motorised/5/0/sensor/0", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",},
            //Group2 Alle voetgangers sensoren + fietsers sensoren
            {"/foot/0/0/sensor/0", "/foot/0/1/sensor/0", "/foot/1/0/sensor/0", "/foot/1/1/sensor/0", "/foot/2/sensor/0", "/foot/3/sensor/0", "/foot/4/0/sensor/0", "/foot/4/1/sensor/0", "/foot/4/2/sensor/0", "/foot/5/0/sensor/0", "/foot/5/1/sensor/0", "/foot/5/2/sensor/0", "/foot/6/sensor/0", "/foot/7/sensor/0", "cycle/0/sensor/0", "cycle/1/sensor/0", "cycle/2/sensor/0", "cycle/3/0/sensor/0", "cycle/3/1/sensor/0", "cycle/4/sensor/0", "cycle/5/sensor/0"},
        };
        public GroupDecide(string topic, string mqttMessage, string team_id)
        {
            this.mqttMessage = mqttMessage;
            this.topic = topic;
            this.team_id = team_id;
        }
    }
}
