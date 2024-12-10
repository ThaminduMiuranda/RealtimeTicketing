import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TicketService {
  private apiBase = 'http://localhost:8080/api/tickets'; // Update with your backend URL

  constructor(private http: HttpClient) {}

  getTicketStatus(): Observable<{ totalTickets: number; ticketsSold: number; ticketsAvailable: number }> {
    return this.http.get<{
      totalTickets: number;
      ticketsSold: number;
      ticketsAvailable: number
    }>(`${this.apiBase}/status`);
  }
}
