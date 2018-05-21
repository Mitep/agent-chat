import { Injectable, Injector } from '@angular/core';
import { Router } from '@angular/router';
import * as Rx from 'rxjs/Rx';

const url = 'ws://localhost:8080/ChatAppWAR/websocket/echo';

@Injectable()
export class WebsocketService {

  public ws: WebSocket;
  public router:Router;
  public logged:Boolean = false;
  public username:String;

  constructor(rt:Router) { 
      this.router = rt;
  }

  public connect(): void {

    var rt = this.router;
    var l = this.logged;
    var w = this;
    this.ws = new WebSocket(url);
    
    this.ws.onmessage = function(data){
      
      console.log("stiglo nesto: " + data);  

      var json = JSON.parse(data.data);
      var type = json.type;
      var status = json.status;

      console.log(type);
      console.log(status);

      switch(type){
          case('register'):{
              if(status=='success'){
                  l = true; //TODO: promijeniti u false kad bude logovanje gotovo
                  rt.navigateByUrl('/home');
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
                  rt.navigateByUrl('/home')
              }else if(status=='fail'){
                  l = false;
                  rt.navigateByUrl('/login');
                  alert(json.info);
              }           
              break;
          }
          case("user_search"):{
              console.log(data);
              rt.navigateByUrl('/search');
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