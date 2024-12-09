import { Component } from '@angular/core';

@Component({
  selector: 'app-control-panel',
  imports: [],
  templateUrl: './control-panel.component.html',
  styleUrls: ['./control-panel.component.scss']
})
export class ControlPanelComponent {
  public isRunning: boolean = false;

  constructor() {
  }

  startSimulation(){
    if (!this.isRunning){
      console.log('Simulation started.');
      this.isRunning = true;
      //logic
    }
  }

  stopSimulation(){
    if (this.isRunning){
      console.log("Simulation is stopped.");
      this.isRunning = false;
    }
  }
}
