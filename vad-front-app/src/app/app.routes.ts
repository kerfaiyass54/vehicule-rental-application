import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import {roleRedirectGuard} from './guards/role-redirect.guard';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    canActivate: [roleRedirectGuard],
    children: [],      // placeholder, never actually rendered
  },
  {
    path: 'supplier',
    loadChildren: () =>
      import('./supplier-ui/supplier-ui.routes').then(m => m.SUPPLIER_ROUTES),
    canActivate: [authGuard],
    data: { roles: ['supplier'] },
  },
  {
    path: 'forbidden',
    loadComponent: () =>
      import('./forbidden-access/forbidden-access').then(m => m.ForbiddenAccess),
  },
  {
    path: 'no-role',
    loadComponent: () =>
      import('./no-role/no-role')
        .then(m => m.NoRole),
  },
  {
    path: '**',
    redirectTo: '',
  },
];
