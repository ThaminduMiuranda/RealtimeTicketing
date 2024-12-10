import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LogService {
  private apiBase = 'http://localhost:8080/api/logs'; // Adjust the URL as needed

  constructor(private http: HttpClient) {}

  getLogs(): Observable<string[]> {
    return this.http.get<string[]>(this.apiBase);

  }


}
