import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ConfigurationFormComponent} from './components/configuration-form/configuration-form.component';
import {ControlPanelComponent} from './components/control-panel/control-panel.component';
import {LogDisplayComponent} from './components/log-display/log-display.component';
import {TicketStatusComponent} from './components/ticket-status/ticket-status.component';

@Component({
  selector: 'app-root',
  imports: [ConfigurationFormComponent, ControlPanelComponent, LogDisplayComponent, TicketStatusComponent],
  templateUrl: './app.component.html',
  standalone: true,
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'ticketingFrontend';
}
