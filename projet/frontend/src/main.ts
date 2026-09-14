import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { AppComponent } from './app/app.component';
import { authInterceptor } from './app/core/auth.interceptor';
import { routes } from './app/app.routes';

Chart.register(...registerables);

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
  ],
}).catch((err) => console.error(err));