import {Component, Inject, OnDestroy, OnInit, signal} from '@angular/core';
import {interval, Subscription, switchMap} from 'rxjs';
import {TicketService} from '../../services/ticket.service';
import {isPlatformBrowser} from '@angular/common';
import {PLATFORM_ID} from '@angular/core';

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

  isBrowser = signal(false);
  private intervalId: any;

  private statusSubscription: Subscription | undefined;

  constructor(private ticketService: TicketService,
  @Inject(PLATFORM_ID) private platformId: Object) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.intervalId = setInterval(() => {
        this.fetchTicketStatus();
      }, 500); // Refresh every 0.5 seconds
    }
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
    if(this.intervalId){
      clearInterval(this.intervalId)
    }
  }
}
