import { Component, OnInit, Injector } from '@angular/core';
import { Router } from '@angular/router';
import { WebsocketService } from '../../services/websocket.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

  private msg;
  private router:Router;
  private ws:WebsocketService;
  private friend;
  private myMessagesFromFriend:Array<{"sender": String, "receiver":String, "content":String, "timestamp":number}> = [];

  constructor(private rt:Router, private injector:Injector) {
    this.router = rt;
    this.ws = injector.get(WebsocketService);
  }
  
  ngOnInit() {
    console.log(this.ws);
    if(this.ws["logged"]==false){
        this.router.navigateByUrl('/');
    }
  }

 
  searchUser(data){
    if(data.username==undefined){
      data.username = "";
    }
    if(data.name==undefined){
      data.name="";
    }
    if(data.surname==undefined){
      data.surname="";
    }
    this.msg = "{\"type\":\"user_search\","
             + "\"data\":{"
             + "\"searcher\":\"" + this.ws["username"] + "\","
             + "\"username\":\"" + data.username + "\","
             + "\"name\":\"" + data.name + "\","
             + "\"surname\":\"" + data.surname + "\"}"
             + "}";
    console.log(this.msg);
    this.ws.sendMsg(this.msg);
    //this.router.navigateByUrl('/search');
  }

  logout(){
    this.msg = "{\"type\":\"logout\","
             + "\"data\":{"
             + "\"username\":\"" + this.ws["username"] + "\"}"
             + "}";
    console.log(this.msg);
    this.ws["logged"]=false;
    this.ws.sendMsg(this.msg);
  }

  showMessages(friend){
    this.friend = friend;
    this.ws.myMessagesMilli = [];
    this.ws.myMessagesDate = [];
    this.myMessagesFromFriend = []; //TODO: obrisati myMessagesFromFriend, nece nam trebati
   
    this.ws.myMessagesMilli = [{sender:"mitep", receiver:"masato", content:"halo halo", timestamp:123123123299}, //1973
                               {sender:"masato", receiver:"mitep", content:"halo da li se cujemo", timestamp:1231231232121}, //2009
                               {sender:"mitep", receiver:"masato", content:"cujemo se", timestamp:1531231200000}, 
                               {sender:"masato", receiver:"mitep", content:"apsolutno", timestamp:1530000000000}, //2018
                               {sender:"mitep", receiver:"masato", content:"sta ima", timestamp:1551231200000} //2019
                              ]; 
    //myMessagesMilli -- iz svih poruka(this.ws.myAllMessages) izdvajamo one koje su od kliknutog frienda
    
    /* this.ws.myMessagesMilli = this.ws.myAllMessages.filter(function(item){
         if(item.sender == this.friend || item.receiver == this.friend)
            return item;
    }); */
    
    this.myMessagesFromFriend = this.ws.myMessagesMilli.filter(function(item) {
       if(item.sender == friend || item.receiver == friend)
            return item;
    });

    this.ws.myMessagesMilli.sort(function(a, b) { return a.timestamp - b.timestamp; });
    var myTimestamps:Array<number> = new Array<number>();

    for(var i = 0; i < this.ws.myMessagesMilli.length; i++){
      myTimestamps.push(this.ws.myMessagesMilli[i].timestamp);
    }
    for(var i = 0; i< this.ws.myMessagesMilli.length; i++){
        var o = {"sender": this.ws.myMessagesMilli[i].sender, "receiver":this.ws.myMessagesMilli[i].receiver, "content":this.ws.myMessagesMilli[i].content, "date":new Date(myTimestamps[i])};
        this.ws.myMessagesDate.push(o);
    }
    //console.log(this.ws.myMessagesDate); 
    /* this.msg = "{\"type\":\"show_messages\","
             + "\"data\":{"
             + "\"my_username\":\"" + this.ws["username"] + "\","
             + "\"friends_username\":\"" + friend + "\"}" //TODO: promijeniti username prijatelja
             + "}"; */
   // console.log(this.msg);
    //console.log(this.friend);
    //this.ws.sendMsg(this.msg);
  }

  sendMessage(content){
    console.log("To: " + this.friend + "; Content: " + content.message_content);
    this.msg = "{\"type\":\"send_message\","
             + "\"data\":{"
             + "\"content\":\"" + content.message_content + "\","
             + "\"timestamp\":\"" + new Date().valueOf() + "\","
             + "\"sender\":\"" + this.ws["username"] + "\","
             + "\"receiver\":\"" + this.friend + "\"}" //TODO: promijeniti username prijatelja
             + "}";
    console.log(this.msg);
    this.ws.sendMsg(this.msg);
  }

}
