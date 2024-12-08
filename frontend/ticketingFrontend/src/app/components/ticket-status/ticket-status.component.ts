import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-ticket-status',
  templateUrl: './ticket-status.component.html',
  styleUrls: ['./ticket-status.component.scss'],
  standalone: true
})
export class TicketStatusComponent implements OnInit {
  totalTickets: number = 0;
  ticketsSold: number = 0;
  ticketsAvailable: number = 0;
  ticketsInProcess: number = 0;

  constructor() {}

  ngOnInit(): void {
    // Fetch ticket status data (replace with actual data retrieval logic)
    this.totalTickets = 100; // Example value
    this.ticketsSold = 30; // Example value
    this.ticketsAvailable = this.totalTickets - this.ticketsSold;
    this.ticketsInProcess = 5; // Example value
  }
}
