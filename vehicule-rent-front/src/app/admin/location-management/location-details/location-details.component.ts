import {Component, OnInit, ChangeDetectionStrategy} from '@angular/core';

import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {LocationServiceAdminService} from "../../Services/location-service-admin.service";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-location-details',
    imports: [
    RouterLink
],
    templateUrl: './location-details.component.html',
    styleUrl: './location-details.component.css'
})
export class LocationDetailsComponent implements OnInit{

   constructor(private locationService:LocationServiceAdminService,private route:ActivatedRoute, private router: Router) {
}

  location:any;
  clientsNum: any;
  repairsNum:any;
  name:any;
  canDelete1:boolean = false;
  canDelete2: boolean = false;

  ngOnInit() {
    this.name = this.route.snapshot.paramMap.get('id');
    this.locationService.getLocation(this.name).subscribe(
      (loc)=>{
        this.location = loc;
      }
    )
    this.locationService.getClients(this.name).subscribe(
      (clients)=>{
        this.clientsNum = clients.length;
        this.canDelete1 = this.clientsNum == 0;
      }
    )
    this.locationService.getRepairs(this.name).subscribe(
      (rep)=>{
        this.repairsNum = rep.length;
        this.canDelete1 = this.clientsNum == 0;
      }
    )
  }


  confirmDelete() {
    this.locationService.deleteLocation(this.name).subscribe(
      ()=>{
        this.router.navigate(['admin/location'])
      }
    )
  }
}
