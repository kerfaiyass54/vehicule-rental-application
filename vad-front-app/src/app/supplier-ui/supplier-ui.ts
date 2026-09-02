import { Component } from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {NavItem} from '../components/models/NavItem';
import {NavBar} from '../components/nav-bar/nav-bar';

@Component({
  selector: 'app-supplier-ui',
  imports: [
    RouterOutlet,
    NavBar
  ],
  templateUrl: './supplier-ui.html',
  styleUrl: './supplier-ui.css',
})
export class SupplierUi {

  SUPPLIER_NAV_ITEMS: NavItem[] = [
    {
      label: 'Addresses',
      link: '/supplier/addresses',
      icon: 'location_on'
    },
    {
      label: 'Dashboard',
      link: '/supplier/dashboard',
      icon: 'dashboard'
    },
    {
      label: 'Vehicules',
      link: '/supplier/vehicules',
      icon: 'local_shipping'
    },
    {
      label: 'Demands',
      link: '/supplier/demands',
      icon: 'assignment'
    },
    {
      label: 'Subscriptions',
      link: '/supplier/subscriptions',
      icon: 'subscriptions'
    },
    {
      label: 'Locations',
      link: '/supplier/locations',
      icon: 'location_city'
    },
    {
      label: 'Buyings',
      link: '/supplier/buyings',
      icon: 'shopping_cart'
    }
  ];
}
