import { Component, OnInit } from '@angular/core';
import { WebsocketService } from '../../services/websocket.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  private msg; 
  private date: Date;

  constructor(private wsService:WebsocketService) { }

  ngOnInit() {
  }

  submitForm(data){
    
    this.date = new Date();
   // console.log(this.date.valueOf());

    this.msg = "{\"type\":\"register\","
             + " \"data\":{"
             + " \"timestamp\":" + this.date.valueOf() + ","
             + " \"username\":\"" + data.username + "\","
             + " \"firstName\":\"" + data.firstname + "\","
             + " \"lastName\":\"" + data.lastname + "\","                    
             + " \"password\":\"" + data.password +"\"}"
             + "}";
    console.log(this.msg);

    this.wsService.sendMsg(this.msg);
  }
}
