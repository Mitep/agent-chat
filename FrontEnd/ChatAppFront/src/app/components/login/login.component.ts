import { Component, OnInit } from '@angular/core';
import { WebsocketService } from '../../services/websocket.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  private msg; 

  constructor(private wsService:WebsocketService) { }
  
  ngOnInit() {
  }

submitForm(data){
      this.msg = "{\"type\":\"login\","
               + " \"data\":{"
               + " \"email\":\"" + data.email + "\","
               + " \"password\":\"" + data.password +"\"}"
               + "}";
      console.log(this.msg);
      this.wsService.sendMsg(this.msg);
  }
}
