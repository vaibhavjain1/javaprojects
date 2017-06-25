using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using client_methods.ServiceReference1;
using System.Threading;
using System.ServiceModel;
using System.Collections.ObjectModel;


namespace client_methods
{
    public class Csink : IService1Callback
    {
        public string servertoclient(string message)
        {   string mess="thanks";
           
                Console.WriteLine(message);
                Thread.Sleep(4000);
       
              return mess;
        }

    }

    class Program
    {
        static string myusername = null;
        static void Main(string[] args)
        {   
            List<string> ousers = new List<string>();
            string usertochat = string.Empty;
            string message = string.Empty;
            Csink obj = new Csink();
            InstanceContext cntxt = new InstanceContext(obj);
            Service1Client proxy = new Service1Client(cntxt);
            Console.WriteLine("enter username");
            string username = Console.ReadLine();
            myusername = username;
            proxy.Duplexcall(username);   //just change the name of function to call
 
            Console.WriteLine("client connected");
            Thread.Sleep(1000);
           /* Console.WriteLine("enter user to chat, message");
            usertochat = Console.ReadLine();
            message = Console.ReadLine();
            proxy.chatrequest(username,usertochat,message);
            Console.ReadLine(); */
            
            while (true)
            {
                var x = proxy.onlineusers();
                Console.WriteLine("online users");
                foreach (var item in x)
                {   
                    Console.WriteLine(item);
                }

                Console.WriteLine("Enter username to chat");
                string getto = Console.ReadLine();
                Console.WriteLine("enter message");
                string tomess = Console.ReadLine();
                proxy.sendmsg(getto,myusername+": "+tomess);
                
            }
  
        }
    }
}
