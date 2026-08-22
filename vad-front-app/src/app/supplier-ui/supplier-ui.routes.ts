import { Routes } from '@angular/router';
import {SupplierUi} from './supplier-ui';
import {SuppleirDetails} from './suppleir-details/suppleir-details';
import {SupplierHeroPage} from './supplier-hero-page/supplier-hero-page';
import {SupplierAdresses} from './supplier-adresses/supplier-adresses';
import {SupplierVehicules} from './supplier-vehicules/supplier-vehicules';
import {SupplierProductions} from './supplier-productions/supplier-productions';
import {SupplierPlacement} from './supplier-placement/supplier-placement';

import {AddVehicule} from './supplier-productions/add-vehicule/add-vehicule';

export const SUPPLIER_ROUTES: Routes = [
  {
    path: '',
    component: SupplierUi,
    children:
    [
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
      },{
      path: 'add-vehicule',
      component: AddVehicule,
    }
    ]
  },
];
