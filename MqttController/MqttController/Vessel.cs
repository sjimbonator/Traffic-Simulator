using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MqttController
{
    class Vessel
    {
        private string topic;
        private string mqttMessage;
        private string team_id;

        public Vessel(string topic, string mqttMessage, string team_id)
        {
            this.topic = topic;
            this.mqttMessage = mqttMessage;
            this.team_id = team_id;


        }
    }
}
