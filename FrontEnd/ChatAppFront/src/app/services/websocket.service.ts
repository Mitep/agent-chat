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
  public myMessagesDate:Array<{"sender": String, "receiver":String, "content":String, "date":Date}> = [];
  public myMessagesMilli:Array<{"sender": String, "receiver":String, "content":String, "timestamp":number}> = [];
  public myFriends:Array<String>;
  public myReceivedRequests:Array<String>;
  public mySentRequests:Array<String>;
  public myAllMessages:Array<{"sender": String, "receiver":String, "content":String, "timestamp":number}>=[];
  public friend;

  constructor(rt:Router) { 
      this.router = rt;
  }

  public connect(): void {
   
    var rt = this.router;
    var l = this.logged;
    var w = this;

    var searchResults = this.searchResults;
    var myMessages = this.myMessagesMilli;
    
    this.ws = new WebSocket(url);
    
    this.ws.onmessage = function(data){
      
      console.log("data: " + data);  
      console.log("data.data: " + data.data);
      var json = JSON.parse(data.data);
      var type = json.type;
      var status = json.status;
      console.log("json(JSON.parse(data.data)): " + json);
      console.log("type(json.type): " + type);
      console.log("status(json.status): " + status);

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
                  //myFriends = json.data.friends;
                  w.myFriends = json.data.friends;
                  console.log("myFriends: " + w.myFriends);
                  w.myReceivedRequests = json.data.receivedRequests;
                  console.log("stigli zahtjevi: " + w.myReceivedRequests);
                  w.mySentRequests = json.data.sentRequests;
                  console.log("poslati zahtjevi: " + w.mySentRequests);
                  //w.myAllMessages = json.data.messages;
                  
              }else if(status=='fail'){
                  l = false;
                  rt.navigateByUrl('/login');
                  alert(json.info);
              }           
              break;
          }
          case("user_search"):{            
              console.log("ko sam ja: " + json.searcher);
              console.log("searchovani korisnici[0]: " + json.data[0].username);
              searchResults = json.data;
              w.searchResults = searchResults;
              console.log(searchResults);
              rt.navigateByUrl('/search');
              break;
          }
          case("show_messages"):{
               
                break;
          }
          case("receive_message"):{
                var time = parseInt(json.timestamp);
                var message = {sender:json.sender, receiver:json.receiver, content:json.content, timestamp:time};             
                w.myAllMessages.push(message);
                w.updateMsg();

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
        var f = this.friend
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
        }
  }

}