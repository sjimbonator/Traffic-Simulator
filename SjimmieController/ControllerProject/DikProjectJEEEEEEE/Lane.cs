using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Controller
{
    interface Lane
    {
        string GetGroup();
        int GetPriority();
        bool isReady();
        string[] GetGroupedLanes();
        void CheckPriority();
        void RedLight();
        void OrangeLight();
        void GreenLight();
    }
}
