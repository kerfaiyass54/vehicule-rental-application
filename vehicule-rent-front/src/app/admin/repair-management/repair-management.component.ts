import {Component, OnInit, ChangeDetectionStrategy} from '@angular/core';
import {Router} from "@angular/router";
import {RepairServiceAdminService} from "../Services/repair-service-admin.service";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-repair-management',
    imports: [

],
    templateUrl: './repair-management.component.html',
    styleUrl: './repair-management.component.css'
})
export class RepairManagementComponent implements OnInit{
  listRepairs: any[] = []

  constructor(private repairService:RepairServiceAdminService,private router: Router) {}

  ngOnInit() {
    this.repairService.getAllRepairs().subscribe(
      (data) => {
        this.listRepairs = data;
      }
    )
  }

  goToAddRepair(){
    this.router.navigate(['admin/add-repair']);
  }


  goToUpdate(idRepair: any) {
    this.router.navigate(['admin/update/repair',idRepair]);
  }

  deleteRepair(idRepair: any){
    this.repairService.deleteRepair(idRepair).subscribe(
      ()=>{
        console.log("success")
      }
    )
  }
}
