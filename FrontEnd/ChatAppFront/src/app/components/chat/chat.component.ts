import { Component, OnInit, Injector } from '@angular/core';
import { Router } from '@angular/router';
import { WebsocketService } from '../../services/websocket.service';
import { NgForm } from '@angular/forms';
import { FromEventObservable } from 'rxjs/observable/FromEventObservable';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

  private msg;
  private router:Router;
  private ws:WebsocketService;
  public friend;
  public group;
 
 // private myMessagesFromFriend:Array<{"sender": String, "receiver":String, "content":String, "timestamp":number}> = [];

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
    this.ws["friend"] = friend;
    this.ws.updateMsg(); 
    
    this.ws["isGroup"] = false;
    this.ws["group"] = null;
  }

  showGroupMessages(group){
    this.group = group;
    this.ws["group"] = group;
    this.ws.updateGroupMsg();
    
    this.ws["isGroup"] = true;
    this.ws["friend"] = null;
  }

  sendMessage(content){
    console.log("To: " + this.friend + "; Content: " + content.message_content);
    var time = new Date().valueOf();
    this.msg = "{\"type\":\"send_message\","
             + "\"data\":{"
             + "\"content\":\"" + content.message_content + "\","
             + "\"timestamp\":\"" + time + "\","
             + "\"sender\":\"" + this.ws["username"] + "\","
             + "\"receiver\":\"" + this.friend + "\"}" 
             + "}";
    
    console.log(this.msg);
    this.ws.sendMsg(this.msg);
    var message ={sender:this.ws["username"], receiver:this.friend, content:content.message_content, timestamp:time};
    this.ws.myAllMessages.push(message);
    this.showMessages(this.friend);
    this.ws.updateLeftMsgList();
  }

  sendGroupMessage(content){
    var time = new Date().valueOf();
    this.msg = "{\"type\":\"send_group_message\","
             + "\"data\":{"
             + "\"content\":\"" + content.message_content + "\","
             + "\"timestamp\":\"" + time + "\","
             + "\"sender\":\"" + this.ws["username"] + "\","
             + "\"receiver\":\"" + this.group + "\"}" 
             + "}";
    console.log(this.msg);
    this.ws.sendMsg(this.msg);
    var message ={sender:this.ws["username"], receiver:this.group, content:content.message_content, timestamp:time};
    this.ws.myAllGroupMessages.push(message);
    this.showGroupMessages(this.group);
    this.ws.updateLeftGroupMsgList();
  }

  isFriendSelected(){
      if(this.friend == undefined)
        return false;
      else
        return true;
  }

  isGroupSelected(){
    if(this.group == undefined)
      return false;
    else
      return true;
  }

  createGroup(data){
    console.log("kreiranje grupe: " + data.members);
    var members = data.members.split(" ");
    var m = new Array<String>();

    m = data.members.split(" ");
   
    this.msg = "{\"type\":\"create_group\","
              + "\"data\":{"
              + "\"name\":\"" + data.groupname + "\","
              + "\"members\":[" + m + "]}" 
              + "}";
    console.log(this.msg);
  }

  friendOnline(friend){
      return true;
  }

}
