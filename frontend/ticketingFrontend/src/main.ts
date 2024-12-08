import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import {provideHttpClient} from '@angular/common/http';
import {ConfigurationFormComponent} from './app/components/configuration-form/configuration-form.component';
import {provideRouter, Routes} from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

const routes: Routes = [
  { path: '', component: ConfigurationFormComponent },
  { path: 'configuration', component: ConfigurationFormComponent },
];

bootstrapApplication(AppComponent, {
  providers:[
    provideRouter(routes),
    provideHttpClient(), provideAnimationsAsync()
  ]
})
  .catch((err) => console.error(err));
