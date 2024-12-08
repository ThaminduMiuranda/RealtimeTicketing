import {Component, OnInit} from '@angular/core';
import {FormsModule, NgForm} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {ConfigurationService} from '../../services/configuration.service';
import {Configuration} from '../../models/configuration.model';
import {ToastService} from '../../services/toast.service';

@Component({
  selector: 'app-configuration-form',
  templateUrl: './configuration-form.component.html',
  imports: [
    FormsModule
  ],
  styleUrls: ['./configuration-form.component.scss']
})
export class ConfigurationFormComponent implements OnInit{
  configuration: Configuration = {
    totalTickets: 100,
    ticketReleaseRate: 10,
    customerRetrievalRate: 5,
    maxTicketCapacity: 50
  };

  constructor(private configService: ConfigurationService, private toast: ToastService) {}

  ngOnInit(): void {
    this.loadConfiguration();
  }

  loadConfiguration(): void{
    this.configService.getConfiguration().subscribe({
      next: (config)=>{
        this.configuration = config;
        console.log('Data Loaded from the Existing Json:', this.configuration);
      },
      error: (error) =>{
        console.error('Error loading configuration:', error);
        this.configuration = {
          totalTickets: 100,
          ticketReleaseRate: 10,
          customerRetrievalRate: 5,
          maxTicketCapacity: 50
        };
        this.toast.error(
          'Failed to load configuration. Please try again later.',
          'Error'
        );
      }
    })
  }

  onSubmit(configForm: NgForm): void {
    this.configService.saveConfiguration(this.configuration).subscribe(
      (response) => {
        console.log(response);
        console.log('Data entered to the Json:', this.configuration,response);
        this.toast.success(
          'Configuration has been saved successfully',
          'Success'
        );
        this.loadConfiguration();
      },
      (error) => {
        console.error('Error saving configuration:', error);
        this.toast.success(
          'Error saving configuration'+ error.message,
          'Error'
        );
      }
    );
  }
}
