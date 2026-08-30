import { Component } from '@angular/core';
import {NavBar} from "../components/nav-bar/nav-bar";
import {RouterOutlet} from "@angular/router";
import {NavItem} from '../components/models/NavItem';

@Component({
  selector: 'app-repair-ui',
    imports: [
        NavBar,
        RouterOutlet
    ],
  templateUrl: './repair-ui.html',
  styleUrl: './repair-ui.css',
})
export class RepairUi {

  REPAIR_NAV_ITEMS: NavItem[] = [
    {
      label: 'Demands',
      link: '/repair/demands',
      icon: 'assignment'
    },
    {
      label: 'Operations',
      link: '/repair/operations',
      icon: 'build'
    },
    {
      label: 'Tickets',
      link: '/repair/tickets',
      icon: 'confirmation_number'
    }
  ];

}
