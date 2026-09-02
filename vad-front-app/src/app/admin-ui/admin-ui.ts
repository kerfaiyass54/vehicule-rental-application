import { Component } from '@angular/core';
import {NavBar} from "../components/nav-bar/nav-bar";
import {RouterOutlet} from "@angular/router";
import {NavItem} from '../components/models/NavItem';

@Component({
  selector: 'app-admin-ui',
    imports: [
        NavBar,
        RouterOutlet
    ],
  templateUrl: './admin-ui.html',
  styleUrl: './admin-ui.css',
})
export class AdminUi {

  ADMIN_NAV_ITEMS: NavItem[] = [
    {
      label: 'Dashboard',
      link: '/admin/dashboard',
      icon: 'dashboard'
    },

    {
      label: 'Clients',
      link: '/admin/clients',
      icon: 'people'
    },
    {
      label: 'Suppliers',
      link: '/admin/suppliers',
      icon: 'business'
    },
    {
      label: 'Repairs',
      link: '/admin/repairs',
      icon: 'build'
    },
    {
      label: 'Locations',
      link: '/admin/locations',
      icon: 'location_on'
    },{
      label: 'Creation',
      link: '/admin/creation',
      icon: 'create'
    }
  ];

}
