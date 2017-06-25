using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.ServiceModel;
using requestreply;

namespace host
{
    class Program
    {
        static void Main(string[] args)
        {
            ServiceHost host = new ServiceHost(typeof(impl));
            host.Open();
            Console.WriteLine("host started..");
            Console.ReadLine();
            Console.WriteLine("Bye");
            host.Close();
        }
    }
}
