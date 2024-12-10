import { Component } from '@angular/core';
import {SimulationService} from '../../services/simulation.service';
import {SimulationStateService} from '../../services/simulation-state.service';

@Component({
  selector: 'app-control-panel',
  imports: [],
  templateUrl: './control-panel.component.html',
  styleUrls: ['./control-panel.component.scss']
})
export class ControlPanelComponent {
  public isRunning: boolean = false;

  constructor(
    private simulationService: SimulationService,
    private simulationStateService: SimulationStateService
  ) {}

  startSimulation() {
    this.simulationService.startSimulation().subscribe({
      next: (response) => {
        console.log('Simulation started:', response);
        this.isRunning = true;
        this.simulationStateService.setSimulationRunning(true);
      },
      error: (err) => {
        console.error('Error starting simulation:', err);
      },
    });
  }

  stopSimulation() {
    this.simulationService.stopSimulation().subscribe({
      next: (response) => {
        console.log('Simulation stopped:', response);
        this.isRunning = false;
        this.simulationStateService.setSimulationRunning(false);
      },
      error: (err) => {
        console.error('Error stopping simulation:', err);
      },
    });
  }
}
