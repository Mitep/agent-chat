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
 

  constructor(rt:Router) { 
      this.router = rt;
   /*    this.myMessagesMilli = [{sender:"mitep", receiver:"masato", content:"halo halo", timestamp:123123123299}, //1973
                         {sender:"masato", receiver:"mitep", content:"halo da li se cujemo", timestamp:1231231232121}, //2009
                         {sender:"mitep", receiver:"masato", content:"cujemo se", timestamp:1531231200000}, 
                         {sender:"masato", receiver:"masato", content:"haloo sss solo eskuca", timestamp:1530000}, //2018
                         {sender:"mitep", receiver:"masato", content:"sta ima", timestamp:1551231200000} //2019
                        ]; 
      this.myMessagesDate = [];
      this.myMessagesMilli.sort(function(a, b) { return a.timestamp - b.timestamp; });
      var myTimestamps:Array<number> = new Array<number>();

      for(var i = 0; i < this.myMessagesMilli.length; i++){
            myTimestamps.push(this.myMessagesMilli[i].timestamp);
      }
      for(var i = 0; i< this.myMessagesMilli.length; i++){
            var o = {"sender": this.myMessagesMilli[i].sender, "receiver":this.myMessagesMilli[i].receiver, "content":this.myMessagesMilli[i].content, "date":new Date(myTimestamps[i])};
            this.myMessagesDate.push(o);
      }
      console.log(this.myMessagesDate); */

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
               // console.log("jedna poruka: " + json.data[0].content);
                //myMessages = json.data; 
        
               // w.myMessagesMilli = myMessages;
                break;
          }
          case("receive_message"):{
                console.log("stigla poruka");
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

}