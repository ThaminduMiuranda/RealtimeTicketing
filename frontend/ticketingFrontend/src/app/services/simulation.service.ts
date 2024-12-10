// src/app/services/simulation.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SimulationService {
  private apiBase = 'http://localhost:8080/api/simulation'; // Adjust if backend runs on a different port

  constructor(private http: HttpClient) {}

  startSimulation(): Observable<string> {
    return this.http.post<string>(`${this.apiBase}` + '/start', {});
  }

  stopSimulation(): Observable<string> {
    return this.http.post<string>(`${this.apiBase}` + '/stop', {});
  }

  getSimulationStatus(): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiBase}`+ '/status');
  }
}
