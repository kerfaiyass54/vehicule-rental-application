import {Component, OnInit, ChangeDetectionStrategy} from '@angular/core';
import { Router, RouterLink} from "@angular/router";
import {LocationServiceAdminService} from "../Services/location-service-admin.service";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-location-management',
    imports: [
    RouterLink
],
    templateUrl: './location-management.component.html',
    styleUrl: './location-management.component.css'
})
export class LocationManagementComponent implements OnInit{

  listLocations: any[] = [];

  constructor(private locationService:LocationServiceAdminService, private router: Router) {

  }

  ngOnInit() {
    this.locationService.getAllLocations().subscribe(
      (res) =>{
        this.listLocations = res;
      }
    )
  }



  goToAddLocation() {
    this.router.navigate(['admin/add-location']);
  }
}
