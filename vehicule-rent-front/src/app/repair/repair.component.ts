import { Component , ChangeDetectionStrategy} from '@angular/core';
import {NavBar} from "../UIs/nav-bar/nav-bar";
import {RouterOutlet} from "@angular/router";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-repair',
    imports: [
        NavBar,
        RouterOutlet
    ],
    templateUrl: './repair.component.html',
    styleUrl: './repair.component.css'
})
export class RepairComponent {

  mainRoute: any = '/repair';
  list: any[] = [
    {route: '', name: 'Vehicules', drop: true, sublinks : [
        {link: '/repair/ticket', name: 'Tickets'},
        {link: '/repair/demand', name: 'Demands'},
        {link: '/repair/veh-repair', name: 'Repairs'},
      ]}
  ]


}
