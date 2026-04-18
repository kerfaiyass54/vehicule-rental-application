import { Routes } from '@angular/router';
import {SupplierUi} from './supplier-ui';
import {SuppleirDetails} from './suppleir-details/suppleir-details';
import {SupplierHeroPage} from './supplier-hero-page/supplier-hero-page';
import {SupplierAdresses} from './supplier-adresses/supplier-adresses';
import {SupplierVehicules} from './supplier-vehicules/supplier-vehicules';
import {SupplierCategories} from './supplier-categories/supplier-categories';
import {SupplierProductions} from './supplier-productions/supplier-productions';
import {SupplierPlacement} from './supplier-placement/supplier-placement';
import {AddAddress} from './supplier-placement/add-address/add-address';
import {FreeLocation} from './supplier-placement/free-location/free-location';
import {BestLocation} from './supplier-placement/best-location/best-location';
import {AddVehicule} from './supplier-productions/add-vehicule/add-vehicule';
import {AddCategory} from './supplier-productions/add-category/add-category';

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
      },
      {
        path: 'add-address',
        component: AddAddress,
      },{
      path: 'free-address',
      component: FreeLocation,
    },
      {
        path: 'best-location',
        component: BestLocation,
      },{
      path: 'add-vehicule',
      component: AddVehicule,
    },
      {
        path: 'add-category',
        component: AddCategory,
      }
    ]
  },
];
