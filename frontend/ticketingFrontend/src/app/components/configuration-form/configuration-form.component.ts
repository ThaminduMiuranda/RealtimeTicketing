// import {Component, OnInit} from '@angular/core';
// import {FormsModule, NgForm} from '@angular/forms';
// import {HttpClient} from '@angular/common/http';
// import {ConfigurationService} from '../../services/configuration.service';
// import {Configuration} from '../../models/configuration.model';
// import {ToastService} from '../../services/toast.service';
//
// @Component({
//   selector: 'app-configuration-form',
//   templateUrl: './configuration-form.component.html',
//   imports: [
//     FormsModule
//   ],
//   styleUrls: ['./configuration-form.component.scss']
// })
// export class ConfigurationFormComponent implements OnInit{
//   configuration: Configuration = {
//     totalTickets: 100,
//     ticketReleaseRate: 10,
//     customerRetrievalRate: 5,
//     maxTicketCapacity: 50
//   };
//
//   constructor(private configService: ConfigurationService, private toast: ToastService) {}
//
//   ngOnInit(): void {
//     this.loadConfiguration();
//   }
//
//   loadConfiguration(): void{
//     this.configService.getConfiguration().subscribe({
//       next: (config)=>{
//         this.configuration = config;
//         console.log('Data Loaded from the Existing Json:', this.configuration);
//       },
//       error: (error) =>{
//         console.error('Error loading configuration:', error);
//         this.configuration = {
//           totalTickets: 100,
//           ticketReleaseRate: 10,
//           customerRetrievalRate: 5,
//           maxTicketCapacity: 50
//         };
//         this.toast.error(
//           'Failed to load configuration. Please try again later.',
//           'Error'
//         );
//       }
//     })
//   }
//
//   onSubmit(configForm: NgForm): void {
//     this.configService.saveConfiguration(this.configuration).subscribe(
//       (response) => {
//         console.log(response);
//         console.log('Data entered to the Json:', this.configuration,response);
//         this.toast.success(
//           'Configuration has been saved successfully',
//           'Success'
//         );
//         this.loadConfiguration();
//       },
//       (error) => {
//         console.error('Error saving configuration:', error);
//         this.toast.success(
//           'Error saving configuration'+ error.message,
//           'Error'
//         );
//       }
//     );
//   }
// }


import {Component, OnInit} from '@angular/core';
import {FormsModule, NgForm} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {ConfigurationService} from '../../services/configuration.service';
import {Observable} from 'rxjs';
import {ToastService} from '../../services/toast.service';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-configuration-form',
  templateUrl: './configuration-form.component.html',
  imports: [
    FormsModule,
    NgIf
  ],
  styleUrls: ['configuration-form.component.scss']
})

export class ConfigurationFormComponent implements OnInit{
  //Define the default configuration
  configuration = {
    totalTickets: 100,
    ticketReleaseRate: 10,
    customerRetrievalRate: 5,
    maxTicketCapacity: 10
  }

  constructor(private configService: ConfigurationService, private toast: ToastService) {}

  ngOnInit(): void {
    // this.configService.getConfiguration().subscribe((config) =>{
    //   this.configuration = config;
    // })
    this.loadConfiguration()
  }

  loadConfiguration():void{
    this.configService.getConfiguration().subscribe({
      next: (config) =>{
        this.configuration = config;
        console.log('Configuration loaded:',config);
      },
      error: (error) =>{
        console.error('Error loading configuration:',error);
      }
    });
  }

  /**
   * Handles the form submission
   */
  onSubmit():void{
    if (this.isFormValid()){
      this.configService.saveConfiguration(this.configuration).subscribe({
        next: (v) => {
          console.log('Data entered to the Json:', this.configuration, v);
          this.toast.success(
            'Configuration has been saved successfully',
            'Success'
          );
        },
        error: (e) => {
          console.error('Error saving configuration:', e);
          this.toast.success(
            'Error saving configuration' + e.message,
            'Error'
          );
        }
      }
    );
    } else {
      console.error('Invalid input. Please check the form');
    }
  }

  isFormValid():boolean{
    return (
      this.isInteger(this.configuration.totalTickets) &&
        this.configuration.totalTickets > 1 &&
        this.isInteger(this.configuration.ticketReleaseRate) &&
        this.configuration.ticketReleaseRate > 1 &&
        this.configuration.ticketReleaseRate <= this.configuration.totalTickets &&
        this.isInteger(this.configuration.customerRetrievalRate) &&
        this.configuration.customerRetrievalRate > 1 &&
        this.configuration.customerRetrievalRate <= this.configuration.totalTickets &&
        this.isInteger(this.configuration.maxTicketCapacity) &&
        this.configuration.maxTicketCapacity> 1
    )
  }

  private isInteger(value: any): boolean {
    return Number.isInteger(value);
  }



}
