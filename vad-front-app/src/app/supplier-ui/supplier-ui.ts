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
    { label: 'Addresses',  link: '/supplier/addresses',  icon: 'location_on' },
    { label: 'Vehicules',   link: '/supplier/vehicules',   icon: 'directions_car' },
    { label: 'Categories', link: '/supplier/categories', icon: 'category' },
  ];
}
