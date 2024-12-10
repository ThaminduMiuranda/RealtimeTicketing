import {Component, OnDestroy, OnInit} from '@angular/core';
import {interval, Observable, Subject, Subscription, switchMap, takeUntil} from 'rxjs';
import { LogService } from '../../services/log.service';
import {AsyncPipe, NgForOf, NgIf} from '@angular/common';
import {SimulationStateService} from '../../services/simulation-state.service';

@Component({
  selector: 'app-log-display',
  templateUrl: './log-display.component.html',
  styleUrls: ['./log-display.component.scss'],
  imports: [
    AsyncPipe,
    NgIf,
    NgForOf
  ]
})
export class LogDisplayComponent implements OnInit, OnDestroy {
  logs$: Observable<string[]> | undefined;
  private simulationStateSubscription: Subscription | undefined;
  private stopPolling$ = new Subject<void>();

  constructor(
    private logService: LogService,
    private simulationStateService: SimulationStateService
  ) {}

  ngOnInit(): void {
    this.simulationStateSubscription = this.simulationStateService.simulationRunning$.subscribe(
      (isRunning) =>{
        if (isRunning){
          this.startPollingLogs();
          console.log('Log fetched from API');
          // this.fetchLogs();
        }else{
          this.stopPollingLogs();
        }
      }
    )
  }

  fetchLogs() {
    this.logs$ = this.logService.getLogs();
    this.logService.getLogs().subscribe({
      next: (logs) => {
        console.log('Logs fetched from API:', logs);
      },
      error: (err) => {
        console.error('Error fetching logs:', err);
      },
    });
  }


  private startPollingLogs() {
    this.logs$ = interval(1000).pipe(
      switchMap(()=>this.logService.getLogs()), //Fetch logs every second
      takeUntil(this.stopPolling$) //stop polling when the subject emits
    )
  }

  stopPollingLogs(){
    this.stopPolling$.next(); //Signal to stop polling
    this.stopPolling$.complete(); //Complete the subject to clean up
    console.log("Stopped fetching logs.")
  }

  ngOnDestroy(): void {
    // Unsubscribe to avoid memory leaks
    this.simulationStateSubscription?.unsubscribe();
    this.stopPollingLogs();
  }
}
