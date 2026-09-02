import { Routes } from '@angular/router';

import { SupplierUi } from './supplier-ui';

import { SuppleirDetails } from './suppleir-details/suppleir-details';
import { SupplierHeroPage } from './supplier-hero-page/supplier-hero-page';
import { SupplierAdresses } from './supplier-adresses/supplier-adresses';
import { SupplierVehicules } from './supplier-vehicules/supplier-vehicules';

import { SupplierBuyingsComponent } from './supplier-buyings/supplier-buyings';
import { SupplierDemands } from './supplier-demands/supplier-demands';
import { SupplierLocation } from './supplier-location/supplier-location';
import { SupplierSubscriptions } from './supplier-subscriptions/supplier-subscriptions';
import {SupplierDashboard} from './supplier-dashboard/supplier-dashboard';


export const SUPPLIER_ROUTES: Routes = [

  {
    path: '',
    component: SupplierUi,

    children: [

      // ---------------------------------------------------------
      // SUPPLIER DASHBOARD
      // ---------------------------------------------------------

      {
        path: '',
        component: SupplierHeroPage
      },{
        path: 'dashboard',
        component: SupplierDashboard
      },

      // ---------------------------------------------------------
      // SUPPLIER DETAILS
      // ---------------------------------------------------------

      {
        path: 'details',
        component: SuppleirDetails
      },

      // ---------------------------------------------------------
      // ADDRESSES
      // ---------------------------------------------------------

      {
        path: 'addresses',
        component: SupplierAdresses
      },

      // ---------------------------------------------------------
      // VEHICLES
      // ---------------------------------------------------------

      {
        path: 'vehicules',
        component: SupplierVehicules
      },

      // ---------------------------------------------------------
      // BUYINGS
      // ---------------------------------------------------------

      {
        path: 'buyings',
        component: SupplierBuyingsComponent
      },

      // ---------------------------------------------------------
      // DEMANDS
      // ---------------------------------------------------------

      {
        path: 'demands',
        component: SupplierDemands
      },

      // ---------------------------------------------------------
      // LOCATIONS
      // ---------------------------------------------------------

      {
        path: 'locations',
        component: SupplierLocation
      },

      // ---------------------------------------------------------
      // SUBSCRIPTIONS
      // ---------------------------------------------------------

      {
        path: 'subscriptions',
        component: SupplierSubscriptions
      }

    ]
  }

];
