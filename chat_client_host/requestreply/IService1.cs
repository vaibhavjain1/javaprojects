using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;

namespace requestreply
{
    public interface Notify
    {
        [OperationContract(IsOneWay=true)]
        void servertoclient(string msg);
        [OperationContract(IsOneWay = true)]
        void OnlineUsers(string[] userList);
    }
  [ServiceContract(CallbackContract = typeof(Notify))]
    public interface IService1
    {
        [OperationContract (IsOneWay=true)]
        void Duplexcall(string name);
        [OperationContract]
        string[] onlineusers();
        [OperationContract]
        void sendmsg(string username,string msg);
        [OperationContract]
        void disconnect(string username);
    }
 [ServiceBehavior(InstanceContextMode = InstanceContextMode.Single,ConcurrencyMode=ConcurrencyMode.Single)]
    public class impl : IService1
    {
      static Dictionary<string, Notify> log = new Dictionary<string, Notify>();
        public impl()
        {
            Console.WriteLine("server for chat started");
        }
        public void Duplexcall(string username)
        {
           Console.WriteLine("(duplexcall)request for joining by user: {0}",username);
           //call back client
           Notify clientPrxy = OperationContext.Current.GetCallbackChannel<Notify>();
           log.Add(username, clientPrxy);
           SendUsersInfo();
           Console.WriteLine("got the proxy");
           Console.WriteLine("{0} user is now in server online list", username);
         }
        public string[] onlineusers()
        {
            return log.Keys.ToArray();
        }
        public void sendmsg(string username, string msg)
        {
            Notify to = null;
            Console.WriteLine(username+"  "+msg);
            foreach (var item in log)
            {
                if (item.Key == username)
                {
                    to = item.Value;
                    to.servertoclient(msg);
                    break;
                }
            }
        }
        protected void SendUsersInfo()
        {
            string[] strUsers = log.Keys.ToArray();
            foreach (var item in log.Keys)
            {
                    Notify n = log[item];
                    n.OnlineUsers(strUsers);
            }
        }
        public void disconnect(string username)
        {
            log.Remove(username);
            Console.WriteLine(username+" is now offline ");
            SendUsersInfo();
        }
    }
}
