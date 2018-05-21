import { Component, OnInit, Injector } from '@angular/core';
import { Router } from '@angular/router';
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

  constructor(private rt:Router, private injector:Injector) {
     this.router = rt;
     this.ws = injector.get(WebsocketService);
  }

  ngOnInit() {
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
  
    //this.router.navigateByUrl('/search');
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

}
