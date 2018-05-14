import { Component, OnInit} from '@angular/core';
import * as $ from 'jquery';
import { WebsocketService } from './services/websocket.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
	styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit{
	
	private msg;
	private serverUrl = 'ws://localhost:8080/ChatAppWAR/websocket/echo';
	

	constructor(private wsService:WebsocketService){
		
	}

	ngOnInit(){
		this.wsService.connect();
	}

	sendMessage(message){
		//this.msg = message;
		this.wsService.sendMsg(message);	
		//$('#input').val('');
	}
	
}
 