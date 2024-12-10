import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SimulationStateService {
  private simulationRunning = new BehaviorSubject<boolean>(false);

  // Observable to track simulation state
  simulationRunning$ = this.simulationRunning.asObservable();

  setSimulationRunning(isRunning: boolean) {
    this.simulationRunning.next(isRunning);
  }
}
