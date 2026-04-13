import { Routes } from '@angular/router';
import {SupplierUi} from './supplier-ui';
import {SuppleirDetails} from './suppleir-details/suppleir-details';
import {SupplierHeroPage} from './supplier-hero-page/supplier-hero-page';
import {SupplierAdresses} from './supplier-adresses/supplier-adresses';
import {SupplierVehicules} from './supplier-vehicules/supplier-vehicules';
import {SupplierCategories} from './supplier-categories/supplier-categories';
import {SupplierProductions} from './supplier-productions/supplier-productions';
import {SupplierPlacement} from './supplier-placement/supplier-placement';

export const SUPPLIER_ROUTES: Routes = [
  {
    path: '',
    component: SupplierUi,
    children:
    [{
      path: 'categories',
      component: SupplierCategories,
    },
      {
        path: 'details',
        component: SuppleirDetails,
      },
      {
        path: '',
        component: SupplierHeroPage,
      },
      {
        path: 'addresses',
        component: SupplierAdresses,
      },
      {
        path: 'vehicules',
        component: SupplierVehicules,
      },{
      path: 'productions',
      component: SupplierProductions,
    },
      {
        path: 'placement',
        component: SupplierPlacement,
      }
    ]
  },
];
