import { Component, OnInit, Injector } from '@angular/core';
import { WebsocketService } from '../../services/websocket.service';
import { AppComponent } from '../../app.component';
import {Router} from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  private msg; 
  private date: Date;
  private router:Router;
  private ws:WebsocketService;
  //constructor(private ws:WebsocketService, private rt:Router) {
      
   //}
   constructor(private rt:Router, private injector:Injector){
      this.router = rt;
      this.ws = injector.get(WebsocketService);
      
   }

  ngOnInit() {
    if(this.ws["logged"]==true){
      this.router.navigateByUrl('/home');
    }
  }

  submitForm(data){
    
    this.date = new Date();
   // console.log(this.date.valueOf());

    this.msg = "{\"type\":\"register\","
             + " \"data\":{"
             //+ " \"timestamp\":" + this.date.valueOf() + ","
             + " \"username\":\"" + data.username + "\","
             + " \"name\":\"" + data.firstname + "\","
             + " \"surname\":\"" + data.lastname + "\","                    
             + " \"password\":\"" + data.password +"\"}"
             + "}";
    console.log(this.msg);
    //this.ws["username"] = data.username; //TODO: obrisati kad bude gotovo logovanje
    this.ws.sendMsg(this.msg);
    
  }
}
