import {ChangeDetectionStrategy, Component} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {NavBar} from "../UIs/nav-bar/nav-bar";



@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-admin',
    imports: [
        RouterOutlet,
        NavBar
    ],
    templateUrl: './admin.component.html',
    styleUrl: './admin.component.css'
})
export class AdminComponent {

  mainRoute: any = '/admin';
  list: any[] = [
    {route: '', name: 'Users', drop: true, sublinks : [
        {link: '/admin/clients', name: 'Clients'},
        {link: '/admin/repair', name: 'Repairs'},
        {link: '/admin/supplier', name: 'Suppliers'},
      ]},
    {route: '/admin/location', name: 'Locations', drop: false, sublinks : [
      ]},
  ]







}
