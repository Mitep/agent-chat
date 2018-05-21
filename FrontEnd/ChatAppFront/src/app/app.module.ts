import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { WebsocketService } from './services/websocket.service';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { ChatComponent } from './components/chat/chat.component';
import { HomeComponent } from './components/home/home.component';
import { SearchComponent } from './components/search/search.component';


const appRoutes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'chat', component: ChatComponent },
  { path: 'home', component: HomeComponent },
  { path: 'search', component: SearchComponent },
  { path: '**', redirectTo: '', pathMatch: 'full' }
];

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    ChatComponent,
    HomeComponent,
    SearchComponent
  ],
   exports: [
    LoginComponent,
    RegisterComponent,
    RouterModule
  ],
  imports: [
    FormsModule,
    BrowserModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [WebsocketService],
  bootstrap: [AppComponent]
})
export class AppModule { }
