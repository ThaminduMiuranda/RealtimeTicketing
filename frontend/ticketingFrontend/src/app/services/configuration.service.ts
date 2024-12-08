import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Configuration } from '../models/configuration.model';
// import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ConfigurationService {
  private apiUrl = `http://localhost:8080/api/configuration`;

  constructor(private http: HttpClient) {}

  /**
   * Fetches the current configuration from the backend
   * @return Observable containing the configuration json object.
   */
  getConfiguration(): Observable<Configuration> {
    return this.http.get<Configuration>(this.apiUrl);
  }
  /**
   * Saves the configuration to the backend
   */
  saveConfiguration(configuration: any): Observable<Configuration> {
    return this.http.post<Configuration>(this.apiUrl, configuration);
  }
}
