import { Component, OnInit, Injector } from '@angular/core';
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
  private router:Router;
  private ws:WebsocketService;

  constructor(private rt:Router, private injector:Injector) {
      this.router = rt;
      this.ws = injector.get(WebsocketService);
   }
  
  ngOnInit() {
    if(this.ws["logged"]==true){
      this.router.navigateByUrl('/home');
    }
  }

  submitForm(data){
      this.msg = "{\"type\":\"login\","
               + " \"data\":{"
               + " \"username\":\"" + data.username + "\","
               + " \"password\":\"" + data.password +"\"}"
               + "}";
      console.log(this.msg);
      this.ws["username"] = data.username;
      console.log(this.ws["username"]);
      this.ws.sendMsg(this.msg);
  }
}
