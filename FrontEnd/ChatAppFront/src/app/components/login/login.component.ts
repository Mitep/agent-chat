import { Component, OnInit } from '@angular/core';
import { WebsocketService } from '../../services/websocket.service';
import { AppComponent } from '../../app.component';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']

})
export class LoginComponent implements OnInit {

  private msg; 
 
  constructor(private ws:WebsocketService) { }
  
  ngOnInit() {
  }

submitForm(data){
      this.msg = "{\"type\":\"login\","
               + " \"data\":{"
               + " \"username\":\"" + data.username + "\","
               + " \"password\":\"" + data.password +"\"}"
               + "}";
      console.log(this.msg);
      this.ws.sendMsg(this.msg);
  }
}
