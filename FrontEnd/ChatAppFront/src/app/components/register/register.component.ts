import { Component, OnInit } from '@angular/core';
import { WebsocketService } from '../../services/websocket.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  private msg; 

  constructor(private wsService:WebsocketService) { }

  ngOnInit() {
  }

  submitForm(data){
    this.msg = "{\"type\":\"register\","
             + " \"data\":{"
             + " \"username\":\"" + data.username + "\","
             + " \"email\":\"" + data.email + "\","
             + " \"password\":\"" + data.password +"\"}"
             + "}";
    console.log(this.msg);
    this.wsService.sendMsg(this.msg);
  }
}
