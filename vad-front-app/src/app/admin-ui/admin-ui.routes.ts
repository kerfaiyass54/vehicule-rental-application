import { Routes } from '@angular/router';

import { AdminUi } from './admin-ui';

import { AdminDashboard } from './admin-dashboard/admin-dashboard';
import { AdminDetails } from './admin-details/admin-details';

import { AdminClients } from './admin-clients/admin-clients';
import { AddClient } from './admin-clients/add-client/add-client';

import { AdminLocations } from './admin-locations/admin-locations';
import { AddLocation } from './admin-locations/add-location/add-location';

import { AdminRepairs } from './admin-repairs/admin-repairs';
import { AddRepair } from './admin-repairs/add-repair/add-repair';

import { AdminSuppliers } from './admin-suppliers/admin-suppliers';
import { SupplierAdd } from './admin-suppliers/supplier-add/supplier-add';
import {UserDetails} from '../user-details/user-details';
import {AdminCreation} from './admin-creation/admin-creation';


export const ADMIN_ROUTES: Routes = [

  {
    path: '',
    component: AdminUi,
    children: [

      // =====================================================
      // DASHBOARD
      // =====================================================

      {
        path: 'dashboard',
        component: AdminDashboard
      },
      {
        path: 'creation',
        component: AdminCreation
      },
      {
        path: 'details',
        component: UserDetails
      },

      // =====================================================
      // ADMIN DETAILS
      // =====================================================

      {
        path: 'info',
        component: AdminDetails
      },


      // =====================================================
      // CLIENTS
      // =====================================================

      {
        path: 'clients',
        component: AdminClients
      },

      {
        path: 'clients/add',
        component: AddClient
      },


      // =====================================================
      // LOCATIONS
      // =====================================================

      {
        path: 'locations',
        component: AdminLocations
      },

      {
        path: 'locations/add',
        component: AddLocation
      },


      // =====================================================
      // REPAIRS
      // =====================================================

      {
        path: 'repairs',
        component: AdminRepairs
      },

      {
        path: 'repairs/add',
        component: AddRepair
      },


      // =====================================================
      // SUPPLIERS
      // =====================================================

      {
        path: 'suppliers',
        component: AdminSuppliers
      },

      {
        path: 'suppliers/add',
        component: SupplierAdd
      },


      // =====================================================
      // DEFAULT
      // =====================================================

      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'info'
      }

    ]
  }

];
