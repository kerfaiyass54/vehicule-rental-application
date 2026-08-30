import { Routes } from '@angular/router';

import { RepairUi } from './repair-ui';

import { RepairDetails } from './repair-details/repair-details';
import { RepairDemands } from './repair-demands/repair-demands';
import { RepairOperations } from './repair-operations/repair-operations';
import { RepairTickets } from './repair-tickets/repair-tickets';

import { RepairInfo } from './repair-operations/repair-info/repair-info';
import { OpenDemand } from './repair-tickets/open-demand/open-demand';
import {UserDetails} from '../user-details/user-details';
import {TicketDetails} from './repair-tickets/ticket-details/ticket-details';


export const REPAIR_ROUTES: Routes = [

  {
    path: '',
    component: RepairUi,

    children: [

      // ---------------------------------------------------------
      // REPAIR DASHBOARD
      // ---------------------------------------------------------

      {
        path: '',
        component: RepairDetails
      }
      ,{
        path: 'details',
        component: UserDetails
      },
      {
        path: 'ticket/:id',
        component: TicketDetails
      },
      // ---------------------------------------------------------
      // REPAIR DETAILS
      // ---------------------------------------------------------

      {
        path: 'info',
        component: RepairDetails
      },

      // ---------------------------------------------------------
      // REPAIR DEMANDS
      // ---------------------------------------------------------

      {
        path: 'demands',
        component: RepairDemands
      },

      // ---------------------------------------------------------
      // REPAIR OPERATIONS
      // ---------------------------------------------------------

      {
        path: 'operations',
        component: RepairOperations
      },

      // ---------------------------------------------------------
      // REPAIR OPERATION INFO
      // ---------------------------------------------------------

      {
        path: 'operations/:id',
        component: RepairInfo
      },

      // ---------------------------------------------------------
      // REPAIR TICKETS
      // ---------------------------------------------------------

      {
        path: 'tickets',
        component: RepairTickets
      },

      // ---------------------------------------------------------
      // OPEN DEMAND
      // ---------------------------------------------------------

      {
        path: 'tickets/open-demand',
        component: OpenDemand
      }

    ]

  }

];
