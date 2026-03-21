import {APP_INITIALIZER, ApplicationConfig, importProvidersFrom, ChangeDetectionStrategy} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from "@angular/common/http";
import {provideToastr} from "ngx-toastr";
import {provideAnimations} from "@angular/platform-browser/animations";

import {initializeKeycloak} from "./shared/keycloak-init";
import {authInterceptor} from "./shared/auth.interceptor";



export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes),provideHttpClient(withInterceptors([authInterceptor])),
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true
    },provideHttpClient(),provideAnimations(),
    provideToastr()]
};
