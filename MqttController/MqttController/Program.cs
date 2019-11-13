using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MqttController
{
    class Program
    {
        static void Main(string[] args)
        {
            string team_id;

            Console.WriteLine("What group do you wish to connect to?");
            team_id = Console.ReadLine();

            MqttSubscribe connection = new MqttSubscribe();
            connection.subscribe(team_id);


        }
    }
}
