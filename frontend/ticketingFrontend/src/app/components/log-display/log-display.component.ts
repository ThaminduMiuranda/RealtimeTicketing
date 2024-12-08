import { Component, OnInit } from '@angular/core';
import { LogService } from '../../services/log.service';
import { Observable } from 'rxjs';
import {AsyncPipe, NgClass, NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-log-display',
  templateUrl: './log-display.component.html',
  styleUrls: ['./log-display.component.scss'],
  standalone: true,
  imports: [
    NgClass,
    NgIf,
    NgForOf,
    AsyncPipe
  ] // Add necessary Angular modules here
})
export class LogDisplayComponent implements OnInit {
  logs$: Observable<{ timestamp: string; level: string; message: string; }[]> | undefined;

  constructor(private logService: LogService) {}

  ngOnInit(): void {
    this.logs$ = this.logService.getLogs();
  }
}
