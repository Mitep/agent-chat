import { Injectable } from '@angular/core';
import * as Rx from 'rxjs/Rx';

const url = 'ws://localhost:8080/ChatAppWAR/websocket/echo';

@Injectable()
export class WebsocketService {

  public ws: WebSocket;
  constructor() { }

  public connect(): void {
    this.ws = new WebSocket(url);
    this.ws.onmessage = function(data){
      console.log(data.data);  
    }
  }


  public receivedMsg(msg){
    console.log(msg);
  }

  public sendMsg(msg) {
    this.ws.send(msg);
  }

}