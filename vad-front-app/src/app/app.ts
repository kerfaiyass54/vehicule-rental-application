import {Component, OnInit, signal} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {SessionService} from './services/session-service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit{
  protected readonly title = signal('vad-front-app');

  constructor(private sessionService:SessionService) {
  }

  ngOnInit() {
    this.sessionService.saveSession();
  }
}
