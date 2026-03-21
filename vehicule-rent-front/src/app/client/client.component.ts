import { Component , ChangeDetectionStrategy} from '@angular/core';
import {NavBar} from "../UIs/nav-bar/nav-bar";
import {RouterOutlet} from "@angular/router";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-client',
    imports: [
        NavBar,
        RouterOutlet
    ],
    templateUrl: './client.component.html',
    styleUrl: './client.component.css'
})
export class ClientComponent {

  mainRoute: any = '/client';
  list: any[] = [
    {route: '', name: 'Vehicules', drop: true, sublinks : [
        {link: '/client/buyings', name: 'Rents'},
        {link: '/client/tickets', name: 'Tickets'},
      ]},
    {route: '/client/subscriptions', name: 'Subscriptions', drop: false, sublinks : [
      ]},
  ]

}
