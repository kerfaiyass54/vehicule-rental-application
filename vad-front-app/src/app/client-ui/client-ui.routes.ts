import { Routes } from '@angular/router';

import { ClientUi } from './client-ui';

import { ClientBuyings } from './client-buyings/client-buyings';
import { BuyVehicule } from './client-buyings/buy-vehicule/buy-vehicule';

import { ClientDashboard } from './client-dashboard/client-dashboard';
import { ClientUpdateLocation } from './client-info/client-update-location/client-update-location';

import { ClientSubscriptions } from './client-subscriptions/client-subscriptions';
import { AddSubscription } from './client-subscriptions/add-subscription/add-subscription';

import { ClientTickets } from './client-tickets/client-tickets';
import { AddTicket } from './client-tickets/add-ticket/add-ticket';
import {UserDetails} from '../user-details/user-details';
import {ClientVehicules} from './client-vehicules/client-vehicules';
import {ClientInfo} from './client-info/client-info';


export const CLIENT_ROUTES: Routes = [

  {
    path: '',
    component: ClientUi,

    children: [

      // ---------------------------------------------------------
      // CLIENT DASHBOARD
      // ---------------------------------------------------------
      {
        path: 'dashboard',
        component: ClientDashboard,
      },
      {
        path: 'details',
        component: UserDetails
      },
      {
        path: 'info',
        component: ClientInfo
      },
      {
        path: '',
        component: ClientInfo
      },

      // ---------------------------------------------------------
      // UPDATE LOCATION
      // ---------------------------------------------------------

      {
        path: 'location/update',
        component: ClientUpdateLocation
      },

      // ---------------------------------------------------------
      // CLIENT BUYINGS
      // ---------------------------------------------------------

      {
        path: 'buyings',
        component: ClientBuyings
      },

      // ---------------------------------------------------------
      // BUY VEHICULE
      // ---------------------------------------------------------

      {
        path: 'buyings/buy-vehicule',
        component: BuyVehicule
      },

      // ---------------------------------------------------------
      // CLIENT SUBSCRIPTIONS
      // ---------------------------------------------------------

      {
        path: 'subscriptions',
        component: ClientSubscriptions
      },

      {
        path: 'vehicules',
        component: ClientVehicules
      },

      // ---------------------------------------------------------
      // ADD SUBSCRIPTION
      // ---------------------------------------------------------

      {
        path: 'subscriptions/add',
        component: AddSubscription
      },

      // ---------------------------------------------------------
      // CLIENT TICKETS
      // ---------------------------------------------------------

      {
        path: 'tickets',
        component: ClientTickets
      },

      // ---------------------------------------------------------
      // ADD TICKET
      // ---------------------------------------------------------

      {
        path: 'tickets/add',
        component: AddTicket
      }

    ]
  }

];
