import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface TicketStatus {
  totalTickets: number;
  ticketsSold: number;
  ticketsAvailable: number;
  ticketsInProcess: number;
}

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  constructor() {}

  getTicketStatus(): Observable<TicketStatus> {
    // Replace with actual HTTP request
    return new Observable<TicketStatus>((observer) => {
      const status: TicketStatus = {
        totalTickets: 100,
        ticketsSold: 30,
        ticketsAvailable: 70,
        ticketsInProcess: 5
      };
      observer.next(status);
      observer.complete();
    });
  }
}
