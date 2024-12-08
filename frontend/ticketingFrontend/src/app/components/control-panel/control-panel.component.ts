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

  pauseSimulation(){
    if (this.isRunning){
      console.log("Simulation paused.");
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
