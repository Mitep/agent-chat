import { Injectable, Injector} from '@angular/core';
import { Router } from '@angular/router';
import * as Rx from 'rxjs/Rx';
import { SearchComponent } from '../components/search/search.component';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';
const url = 'ws://localhost:8080/ChatAppWAR/websocket/echo';

@Injectable()
export class WebsocketService {

  public ws: WebSocket;
  public router:Router;
  public logged:Boolean = false;
  public username:String;
  public searchResults:Array<{"username": String, "name":String, "surname":String}>;
  //public searchResultsSource = new Subject<Array<{"username": String, "name":String, "surname":String}>>();
  public myAllMessages:Array<{"sender": String, "receiver":String, "content":String, "timestamp":number}>=[];
  public myMessagesDate:Array<{"sender": String, "receiver":String, "content":String, "date":Date}> = [];
  public myMessagesMilli:Array<{"sender": String, "receiver":String, "content":String, "timestamp":number}> = [];
  public myFriends:Array<String>;
  public myReceivedRequests:Array<String>;
  public mySentRequests:Array<String>;
  
  public myAllGroupMessages:Array<{"sender": String, "receiver":String, "content":String, "timestamp":number}> = [];
  public myGroupMessagesMilli:Array<{"sender": String, "receiver":String, "content":String, "timestamp":number}> = [];
  public myGroupMessagesDate:Array<{"sender": String, "receiver":String, "content":String, "date":Date}> = [];
  public myGroups:Array<String>=[];
  
  
  public friend;
  public group;
  public isGroup: Boolean;
  public myLeftMsgsMap: Map<String, String>;
  public myLeftMsgsArray: [String, String][];

  public myLeftGroupMsgsMap: Map<String,String>;
  public myLeftGroupMsgsArray: [String, String][];

  public onlineUsers:Array<String> = [];
  public onlineFriends:Array<String> = [];
  public offlineFriends:Array<String> = [];

  constructor(rt:Router) { 
      this.router = rt;
      this.myLeftMsgsMap = new Map();
      this.myLeftMsgsArray = new Array<[String, String]>();
      this.myLeftGroupMsgsMap = new Map();
      this.myLeftGroupMsgsArray = new Array<[String, String]>();
  }

  public connect(): void {
   
    var rt = this.router;
    var l = this.logged;
    var w = this;

    var searchResults = this.searchResults;
    //var myMessages = this.myMessagesMilli;
    
    this.ws = new WebSocket(url);
    
    this.ws.onmessage = function(data){
      
      console.log("data.data: " + data.data);
      var json = JSON.parse(data.data);
      var type = json.type;
      var status = json.status;
      //console.log("json(JSON.parse(data.data)): " + json);
      console.log("type(json.type): " + type);
      //console.log("status(json.status): " + status);

      switch(type){
          case("register"):{
                if(status=='success'){
                    l = false;
                    rt.navigateByUrl('/login');
                }else if(status=='fail'){
                    l = false;
                    rt.navigateByUrl('/register');
                    alert(json.info);
                }
                break;
          }
          case("login"):{
                if(status=='success'){
                    l = true;
                    rt.navigateByUrl('/home');
                    
                    w.myFriends = json.data.friends;
                    //console.log("myFriends: " + w.myFriends);
                    w.myReceivedRequests = json.data.receivedRequests;
                    //console.log("stigli zahtjevi: " + w.myReceivedRequests);
                    w.mySentRequests = json.data.sentRequests;
                    //console.log("poslati zahtjevi: " + w.mySentRequests);
                    
                    var msgs = json.data.messages;
                    for(var i = 0; i < msgs.length; i++){
                        if(msgs[i].type==0){
                            var m = {sender:msgs[i].sender, receiver:msgs[i].receiver, content:msgs[i].content, timestamp:msgs[i].timestamp};
                            w.myAllMessages.push(m);
                        }
                    } 
                    for(var i = 0; i < msgs.length; i++){
                        if(msgs[i].type==1){
                            var m = {sender:msgs[i].sender, receiver:msgs[i].receiver, content:msgs[i].content, timestamp:msgs[i].timestamp};
                            w.myAllGroupMessages.push(m);
                        }
                    }

                    w.myGroups = json.data.groups;
                    w.onlineUsers = json.data.online_users;
                   
                    for(var i = 0; i < w.onlineUsers.length; i++){
                        if(w.isMyFriend(w.onlineUsers[i])){
                            w.onlineFriends.push(w.onlineUsers[i]);
                        }
                    }
                    for(var i = 0; i < w.myFriends.length; i++){
                        if(!w.onlineFriends.includes(w.myFriends[i])){
                            w.offlineFriends.push(w.myFriends[i]);
                        }
                    }
                   
                    w.updateLeftMsgList();   
                    w.updateLeftGroupMsgList();

                }else if(status=='fail'){
                    l = false;
                    rt.navigateByUrl('/login');
                    alert(json.info);
                }           
                break;
          }
          case("user_search"):{            
                searchResults = json.data;
                w.searchResults = searchResults;
                console.log(searchResults);
                rt.navigateByUrl('/search');
                break;
          }
          case("receive_message"):{
                var time = parseInt(json.timestamp);
                var message = {sender:json.sender, receiver:json.receiver, content:json.content, timestamp:time};             
                w.myAllMessages.push(message);
                w.updateMsg();
                w.updateLeftMsgList();
                break;
          }
          case("receive_group_message"):{
                var time = parseInt(json.timestamp);
                var message = {sender:json.sender, receiver:json.receiver, content:json.content, timestamp:time};             
                console.log("message: " + message);
                if(w.username != json.sender){
                    w.myAllGroupMessages.push(message);
                }
                
                w.updateGroupMsg();
                w.updateLeftGroupMsgList();
                break;
          }
          case("online_user"):{
                w.onlineUsers.push(json.username);
                if(w.isMyFriend(json.username)){
                    w.onlineFriends.push(json.username);
                    for(var i = 0; i < w.offlineFriends.length; i++){
                        if(json.username == w.offlineFriends[i]){
                            w.offlineFriends.splice(i, 1);
                        }
                    }
                }
                break;
          }
          case("offline_user"):{
              
              for(var i = 0; i < w.onlineUsers.length; i++){
                  if(w.onlineUsers[i] == json.username){                   
                      w.onlineUsers.splice(i, 1);
                  }
              }

              if(w.isMyFriend(json.username)){
                  w.offlineFriends.push(json.username);
                  for(var i = 0; i < w.onlineFriends.length; i++){
                      if(w.onlineFriends[i] == json.username){
                          w.onlineFriends.splice(i, 1);
                      }
                  }
              }
              break;
          }
          case("friend_accept"):{
              w.myFriends.push(json.sender);
              var flag = false;
              for(var i = 0; i < w.onlineUsers.length; i++){
                  if(w.onlineUsers[i]==json.sender){
                      w.onlineFriends.push(json.sender);
                      flag = true;
                  }
              }
              if(!flag){
                  w.offlineFriends.push(json.sender);
              }

              for(var i = 0; i < w.mySentRequests.length; i++){
                  if(w.mySentRequests[i]==json.sender){
                      w.mySentRequests.splice(i,1);
                  }
              }
              break;
          }
          case("friend_reject"):{
              for(var i = 0; i < w.mySentRequests.length; i++){
                if(w.mySentRequests[i]==json.sender){
                    w.mySentRequests.splice(i,1);
                }
              }
              break;
          }
          case("friend_remove"):{
              for(var i = 0; i < w.myFriends.length; i++){
                  if(w.myFriends[i]==json.sender){
                      w.myFriends.splice(i,1);
                  }
              }
              for(var i = 0; i < w.onlineFriends.length; i++){
                if(w.onlineFriends[i]==json.sender){
                    w.onlineFriends.splice(i,1);
                }
              }
              for(var i = 0; i < w.offlineFriends.length; i++){
                if(w.offlineFriends[i]==json.sender){
                    w.offlineFriends.splice(i,1);
                }
              }
              break;
          }
          case("friend_add"):{
              w.myReceivedRequests.push(json.sender);
              break;
          }

      }      
      w.logged = l;
    }
  }
 
  public receivedMsg(msg){
        console.log(msg);
  }

  public sendMsg(msg) {
        this.ws.send(msg);
  }

  public updateMsg(){
        var f = this.friend;
        if(f!=null){
            this.myMessagesMilli = [];
            this.myMessagesDate = [];
            
            this.myMessagesMilli = this.myAllMessages.filter(function(item){
                if(item.sender == f || item.receiver == f)
                    return item;
            }); 

            this.myMessagesMilli.sort(function(a, b) { return a.timestamp - b.timestamp; });
            var myTimestamps:Array<number> = new Array<number>();

            for(var i = 0; i < this.myMessagesMilli.length; i++){
                 myTimestamps.push(this.myMessagesMilli[i].timestamp);
            }
            for(var i = 0; i< this.myMessagesMilli.length; i++){
                var o = {"sender": this.myMessagesMilli[i].sender, "receiver":this.myMessagesMilli[i].receiver, "content":this.myMessagesMilli[i].content, "date":new Date(myTimestamps[i])};
                this.myMessagesDate.push(o);
                console.log(this.myMessagesDate);
            }
        }
  }

  public updateGroupMsg(){
    var g = this.group;
    if(g!=null){
        this.myGroupMessagesMilli = [];
        this.myGroupMessagesDate = [];
        
        this.myGroupMessagesMilli = this.myAllGroupMessages.filter(function(item){
            if(item.receiver == g)
                return item;
        }); 

        this.myGroupMessagesMilli.sort(function(a, b) { return a.timestamp - b.timestamp; });
        var myTimestamps:Array<number> = new Array<number>();

        for(var i = 0; i < this.myGroupMessagesMilli.length; i++){
            myTimestamps.push(this.myGroupMessagesMilli[i].timestamp);
        }
        for(var i = 0; i< this.myGroupMessagesMilli.length; i++){
            var o = {"sender": this.myGroupMessagesMilli[i].sender, "receiver":this.myGroupMessagesMilli[i].receiver, "content":this.myGroupMessagesMilli[i].content, "date":new Date(myTimestamps[i])};
            this.myGroupMessagesDate.push(o);
        }
    }
}

  public updateLeftMsgList(){

        this.myAllMessages.sort(function(a, b) { return a.timestamp - b.timestamp; });
        
        for(var i = 0; i < this.myAllMessages.length; i++){          
           if(this.myAllMessages[i].sender != this.username){
                this.myLeftMsgsMap.set(this.myAllMessages[i].sender, this.myAllMessages[i].content);
           }else if(this.myAllMessages[i].receiver != this.username){
                this.myLeftMsgsMap.set(this.myAllMessages[i].receiver, this.myAllMessages[i].content);
           }      
        }
        this.myLeftMsgsArray = this.getEntries(this.myLeftMsgsMap);
    
  }

  public updateLeftGroupMsgList(){
        this.myAllGroupMessages.sort(function(a, b) { return a.timestamp - b.timestamp; });

        for(var i = 0; i < this.myAllGroupMessages.length; i++){           
            this.myLeftGroupMsgsMap.set(this.myAllGroupMessages[i].receiver, this.myAllGroupMessages[i].content);         
        }
        this.myLeftGroupMsgsArray = this.getEntries(this.myLeftGroupMsgsMap);
  }

  public getEntries(map):[String, String][] {
        return Array.from(map.entries());
  }

  public isMyFriend(user): Boolean{
      var flag = false;
      for(var i = 0; i < this.myFriends.length; i++){
            if(user == this.myFriends[i]){
                flag = true;
                break;
            }
      }
      return flag;
  }
}