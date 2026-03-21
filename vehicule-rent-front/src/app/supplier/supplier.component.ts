import { Component , ChangeDetectionStrategy} from '@angular/core';
import {NavBar} from "../UIs/nav-bar/nav-bar";
import {RouterOutlet} from "@angular/router";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-supplier',
    imports: [
        NavBar,
        RouterOutlet
    ],
    templateUrl: './supplier.component.html',
    styleUrl: './supplier.component.css'
})
export class SupplierComponent {

  mainRoute: any = '/supplier';
  list: any[] = [
    {route: '', name: 'Vehicules', drop: true, sublinks : [
        {link: '/supplier/demands', name: 'Demands'},
        {link: '/supplier/categories', name: 'Categories'},
        {link: '/supplier/vehicules', name: 'Vehicules'},
      ]},
    {route: '/supplier/adresses', name: 'Adresses', drop: false, sublinks :[
      ]},
    {route: '/supplier/subscriptions', name: 'Subscriptions', drop: false, sublinks : [
      ]},
  ]

}
