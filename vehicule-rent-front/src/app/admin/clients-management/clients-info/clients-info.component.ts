import {ChangeDetectionStrategy,Component, OnInit, viewChild} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Client} from "../../../models/client";
import {ClientServiceAdminService} from "../../Services/client-service-admin.service";
import {LocationServiceAdminService} from "../../Services/location-service-admin.service";
import {ToastrService} from "ngx-toastr";
import {MatButtonModule} from '@angular/material/button';
import {provideNativeDateAdapter} from '@angular/material/core';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatAccordion, MatExpansionModule} from '@angular/material/expansion';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-clients-info',
    imports: [
        MatButtonModule,
        MatExpansionModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        MatDatepickerModule,
    ],
    templateUrl: './clients-info.component.html',
    styleUrl: './clients-info.component.css',
    providers: [provideNativeDateAdapter()]
})
export class ClientsInfoComponent implements  OnInit{
  client: Client | any | undefined;
  name :any | null;
  location: Location | any;
  constructor(private router: Router, private clientService:ClientServiceAdminService, private route: ActivatedRoute, private locationService: LocationServiceAdminService, private toastr: ToastrService  ) {}

  ngOnInit() {
    this.name = this.route.snapshot.paramMap.get('id');
    this.clientService.getClient(this.name).subscribe(
      (res)=>{
        this.client = res;
        this.locationService.getLocation(res.locationName).subscribe(
          (loc)=>{
            this.location = loc;
          }
        )
      }
    );
  }


  accordion = viewChild.required(MatAccordion);





  goToUpdate(){
    this.router.navigate(['admin/update',this.name]);
  }


  confirmDelete() {
    this.clientService.deleteClient(this.client.idClient).subscribe(
      ()=>{
        this.toastr.success("Client deleted", "SUCCESS");
        this.router.navigate(['admin/clients']);
      }
    )
  }


}
