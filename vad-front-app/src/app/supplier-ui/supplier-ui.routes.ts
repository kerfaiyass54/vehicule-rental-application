import { Routes } from '@angular/router';
import {SupplierUi} from './supplier-ui';
import {SuppleirDetails} from './suppleir-details/suppleir-details';

export const SUPPLIER_ROUTES: Routes = [
  {
    path: '',
    component: SupplierUi,
    children:
    [
      {
        path: 'details',
        component: SuppleirDetails,
      }
    ]
  },
];
