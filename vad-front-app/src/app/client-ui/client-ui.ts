import { Component } from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {NavBar} from '../components/nav-bar/nav-bar';
import {NavItem} from '../components/models/NavItem';

@Component({
  selector: 'app-client-ui',
  imports: [
    RouterOutlet,
    NavBar
  ],
  templateUrl: './client-ui.html',
  styleUrl: './client-ui.css',
})
export class ClientUi {

  CLIENT_NAV_ITEMS: NavItem[] = [
    {
      label: 'Buyings',
      link: '/client/buyings',
      icon: 'shopping_cart'
    },
    {
      label: 'Vehicules',
      link: '/client/vehicules',
      icon: 'local_shipping'
    },
    {
      label: 'Tickets',
      link: '/client/tickets',
      icon: 'confirmation_number'
    },
    {
      label: 'Subscriptions',
      link: '/client/subscriptions',
      icon: 'subscriptions'
    }
  ];

}
