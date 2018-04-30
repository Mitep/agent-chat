import { Component} from '@angular/core';
import * as $ from 'jquery';
import { WebsocketService } from './services/websocket.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
	styleUrls: ['./app.component.css'],
	providers: [WebsocketService]
})

export class AppComponent {
	
	private msg;
	private serverUrl = 'ws://localhost:8080/ChatAppWAR/websocket/echo';

	constructor(private wsService:WebsocketService){
		wsService.connect(this.serverUrl);
	}

	sendMessage(message){
		//this.msg = message;
		this.wsService.sendMsg(message);	
		//$('#input').val('');
	}
	
}
 