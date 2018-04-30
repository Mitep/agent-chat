import { Injectable } from '@angular/core';
//import * as Rx from 'rxjs/Rx';

@Injectable()
export class WebsocketService {
  constructor() { }

  //private subject: Rx.Subject<MessageEvent>;
  private ws:WebSocket;
 

  public connect(url) {
    this.ws = new WebSocket(url);
    this.ws.onmessage = function(data){
      console.log(data);   
    }
  }

  public receivedMsg(msg){
    console.log(msg);
  }

  public sendMsg(msg) {
    this.ws.send(msg);
  }

}