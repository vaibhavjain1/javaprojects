﻿//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.1
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace client_methods.ServiceReference1 {
    
    
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    [System.ServiceModel.ServiceContractAttribute(ConfigurationName="ServiceReference1.IService1", CallbackContract=typeof(client_methods.ServiceReference1.IService1Callback))]
    public interface IService1 {
        
        [System.ServiceModel.OperationContractAttribute(Action="http://tempuri.org/IService1/Duplexcall", ReplyAction="http://tempuri.org/IService1/DuplexcallResponse")]
        void Duplexcall(string name);
        
        [System.ServiceModel.OperationContractAttribute(Action="http://tempuri.org/IService1/chatrequest", ReplyAction="http://tempuri.org/IService1/chatrequestResponse")]
        void chatrequest(string mainuser, string usertochat, string message);
        
        [System.ServiceModel.OperationContractAttribute(Action="http://tempuri.org/IService1/onlineusers", ReplyAction="http://tempuri.org/IService1/onlineusersResponse")]
        string[] onlineusers();
        
        [System.ServiceModel.OperationContractAttribute(Action="http://tempuri.org/IService1/sendmsg", ReplyAction="http://tempuri.org/IService1/sendmsgResponse")]
        void sendmsg(string username, string msg);
    }
    
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    public interface IService1Callback {
        
        [System.ServiceModel.OperationContractAttribute(Action="http://tempuri.org/IService1/servertoclient", ReplyAction="http://tempuri.org/IService1/servertoclientResponse")]
        string servertoclient(string name);
    }
    
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    public interface IService1Channel : client_methods.ServiceReference1.IService1, System.ServiceModel.IClientChannel {
    }
    
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.ServiceModel", "4.0.0.0")]
    public partial class Service1Client : System.ServiceModel.DuplexClientBase<client_methods.ServiceReference1.IService1>, client_methods.ServiceReference1.IService1 {
        
        public Service1Client(System.ServiceModel.InstanceContext callbackInstance) : 
                base(callbackInstance) {
        }
        
        public Service1Client(System.ServiceModel.InstanceContext callbackInstance, string endpointConfigurationName) : 
                base(callbackInstance, endpointConfigurationName) {
        }
        
        public Service1Client(System.ServiceModel.InstanceContext callbackInstance, string endpointConfigurationName, string remoteAddress) : 
                base(callbackInstance, endpointConfigurationName, remoteAddress) {
        }
        
        public Service1Client(System.ServiceModel.InstanceContext callbackInstance, string endpointConfigurationName, System.ServiceModel.EndpointAddress remoteAddress) : 
                base(callbackInstance, endpointConfigurationName, remoteAddress) {
        }
        
        public Service1Client(System.ServiceModel.InstanceContext callbackInstance, System.ServiceModel.Channels.Binding binding, System.ServiceModel.EndpointAddress remoteAddress) : 
                base(callbackInstance, binding, remoteAddress) {
        }
        
        public void Duplexcall(string name) {
            base.Channel.Duplexcall(name);
        }
        
        public void chatrequest(string mainuser, string usertochat, string message) {
            base.Channel.chatrequest(mainuser, usertochat, message);
        }
        
        public string[] onlineusers() {
            return base.Channel.onlineusers();
        }
        
        public void sendmsg(string username, string msg) {
            base.Channel.sendmsg(username, msg);
        }
    }
}
