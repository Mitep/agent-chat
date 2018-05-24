import { Component, OnInit, Injector, OnChanges, Input, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';

import { WebsocketService } from '../../services/websocket.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  private msg;
  private ws:WebsocketService;
  private router:Router;
  //public searchResults;
  
  constructor(private rt:Router, private injector:Injector) {
    
     this.router = rt;
     this.ws = injector.get(WebsocketService);
  }

  ngOnInit() {
    //this.searchResults = this.ws["searchResults"];
    this.ws.myMessagesMilli = [];
    this.ws.myMessagesDate = [];
    this.ws.myGroupMessagesMilli = [];
    this.ws.myGroupMessagesDate = [];
    this.ws["friend"] = null;
    this.ws["group"] = null;
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
    //this.searchResults = this.ws["searchResults"];
  }

  logout(){
    this.msg = "{\"type\":\"logout\","
              + " \"data\":{"
              + " \"username\":\"" + this.ws["username"] + "\"}"
              + "}";
    console.log(this.msg);
    this.ws["logged"]=false;
    this.ws.sendMsg(this.msg);
  }

  isFriend(username): Boolean{
    var flag = false;
    for(var i = 0; i < this.ws["myFriends"].length; i++){
       if(this.ws["myFriends"][i]==username){
         flag = true;
         break;
       }
    }
    return flag;
  }

  hasSentMeRequest(username): Boolean{
    var flag = false;
    for(var i = 0; i < this.ws["myReceivedRequests"].length; i++){
      if(this.ws["myReceivedRequests"][i]==username){
        flag = true;
        break;
      }
    }
    return flag;
  }


  haveISentRequest(username): Boolean{
    var flag = false;
    for(var i = 0; i < this.ws["mySentRequests"].length; i++){
      if(this.ws["mySentRequests"][i]==username){
        flag = true;
        break;
      }
    }
    return flag;
  }

  isMe(username): Boolean{
     var flag = false;
     if(this.ws["username"] == username){
       flag = true;
     }
     return flag;
  }

  addFriend(username){
    this.msg = "{\"type\":\"friend_add\","
    + " \"data\":{"
    + "\"sender\":\"" + this.ws["username"] + "\","
    + " \"receiver\":\"" + username + "\"}"
    + "}";
    console.log(this.msg);
    this.ws.sendMsg(this.msg);
    this.ws.mySentRequests.push(username);
  }

  
  deleteFriend(username){
    this.msg = "{\"type\":\"friend_remove\","
    + " \"data\":{"
    + "\"sender\":\"" + this.ws["username"] + "\","
    + "\"receiver\":\"" + username + "\"}"
    + "}";
    console.log(this.msg);
    this.ws.sendMsg(this.msg);
    for(var i = 0; i < this.ws.myFriends.length; i++){
      if(this.ws.myFriends[i]==username){
        this.ws.myFriends.splice(i,1);
      }
    }
    for(var i = 0; i < this.ws.onlineFriends.length; i++){
      if(this.ws.onlineFriends[i]==username){
        this.ws.onlineFriends.splice(i,1);
      }
    }

    for(var i = 0; i < this.ws.offlineFriends.length; i++){
      if(this.ws.offlineFriends[i]==username){
        this.ws.offlineFriends.splice(i,1);
      }
    }
  }

  acceptRequest(username){
    this.msg = "{\"type\":\"friend_accept\","
    + " \"data\":{"
    + "\"sender\":\"" + this.ws["username"] + "\","
    + "\"receiver\":\"" + username + "\"}"
    + "}";
    console.log(this.msg);
    this.ws.sendMsg(this.msg);

    this.ws.myFriends.push(username);
    for(var i = 0; i < this.ws.myReceivedRequests.length; i++){
      if(this.ws.myReceivedRequests[i]==username){
          this.ws.myReceivedRequests.splice(i,1);
          break;
      }
    }
    for(var i = 0; i < this.ws.onlineUsers.length; i++){
        if(this.ws.onlineUsers[i]==username){
          this.ws.onlineFriends.push(username);
          break;
        }
    }
  }

  declineRequest(username){
    this.msg = "{\"type\":\"friend_reject\","
    + " \"data\":{"
    + "\"sender\":\"" + this.ws["username"] + "\","
    + "\"receiver\":\"" + username + "\"}"
    + "}";
    console.log(this.msg);
    this.ws.sendMsg(this.msg);

    for(var i = 0; i < this.ws.myReceivedRequests.length; i++){
      if(this.ws.myReceivedRequests[i]==username){
        this.ws.myReceivedRequests.splice(i,1);
      }
    }
  }

  

}
