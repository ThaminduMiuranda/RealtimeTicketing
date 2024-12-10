import {Component, OnDestroy, OnInit} from '@angular/core';
import {interval, Subscription, switchMap} from 'rxjs';
import {TicketService} from '../../services/ticket.service';

@Component({
  selector: 'app-ticket-status',
  templateUrl: './ticket-status.component.html',
  styleUrls: ['./ticket-status.component.scss'],
  standalone: true
})
export class TicketStatusComponent implements OnInit, OnDestroy {
  totalTickets: number | null = null;
  ticketsSold: number | null = null;
  ticketsAvailable: number | null = null;

  private statusSubscription: Subscription | undefined;

  constructor(private ticketService: TicketService) {}

  ngOnInit(): void {
    // this.statusSubscription = interval(1000)
    //   .pipe(switchMap(()=>this.ticketService.getTicketStatus()))
    //   .subscribe({
    //     next: (status) =>{
    //       this.totalTickets = status.totalTickets;
    //       this.ticketsSold = status.ticketsSold;
    //       this.ticketsAvailable = status.ticketsAvailable;
    //     },
    //     error: (err) => {
    //       console.error('Error fetching ticket status:', err);
    //     },
    //   })
    this.fetchTicketStatus();
  }

  fetchTicketStatus() {
    this.ticketService.getTicketStatus().subscribe({
      next: (status) => {
        this.totalTickets = status.totalTickets;
        this.ticketsSold = status.ticketsSold;
        this.ticketsAvailable = status.ticketsAvailable;
      },
      error: (err) => {
        console.error('Error fetching ticket status:', err);
      },
    });
  }

  ngOnDestroy(): void {
    // Cleanup the subscription to avoid memory leaks
    this.statusSubscription?.unsubscribe();
  }
}
