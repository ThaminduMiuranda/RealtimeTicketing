import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LogService {
  getLogs(): Observable<{ timestamp: string; level: string; message: string }[]> {
    // Replace this with actual API call to your backend
    const logs = [
      { timestamp: '2023-10-10 12:00:00', level: 'info', message: 'Application started.' },
      { timestamp: '2023-10-10 12:05:00', level: 'warn', message: 'High memory usage detected.' },
      { timestamp: '2023-10-10 12:10:00', level: 'error', message: 'Unhandled exception occurred.' },
    ];
    return of(logs);
  }
}
