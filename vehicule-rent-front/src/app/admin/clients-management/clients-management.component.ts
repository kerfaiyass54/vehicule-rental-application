import {AfterViewInit, Component, OnInit, ViewChild, ChangeDetectionStrategy} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {ClientServiceAdminService} from "../Services/client-service-admin.service";
import {} from "@angular/common/http";
import {KeycloakService} from "../../shared/keycloak.service";
import {FormsModule} from "@angular/forms";
import {MatPaginator, MatPaginatorModule, PageEvent} from "@angular/material/paginator";
import {
  MatCell, MatCellDef,
  MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef,
  MatRow, MatRowDef,
  MatTable,
  MatTableDataSource
} from "@angular/material/table";
import {Client} from "../../models/client";
import {ToastrService} from "ngx-toastr";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-clients-management',
    imports: [
        FormsModule,
        MatTable,
        MatHeaderCell,
        MatCell,
        MatHeaderRow,
        MatRow,
        MatPaginatorModule,
        MatColumnDef,
        MatHeaderCellDef,
        MatCellDef,
        MatHeaderRowDef,
        MatRowDef,
        RouterLink
    ],
    templateUrl: './clients-management.component.html',
    styleUrl: './clients-management.component.css'
})
export class ClientsManagementComponent implements OnInit{

  displayedColumns: string[] = [
    'idClient',
    'nameClient',
    'email',
    'locationName',
  ];

  dataSource = new MatTableDataSource<Client>([]);
  totalElements = 0;
  pageSize = 2;
  pageIndex = 0;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private router:Router, private keycloak: KeycloakService, private clientService: ClientServiceAdminService, private toastrService: ToastrService) {

  }
  ngOnInit(): void {
    this.loadClients();
  }

  loadClients(page: number = 0, size: number = this.pageSize): void {
    this.clientService.getClients(page,size)
      .subscribe({
        next: (data) => {
          this.dataSource.data = data.content;
          this.totalElements = data.totalElements;
          this.toastrService.success("Clients displayed","SUCCESS");
        },
        error: (err) => this.toastrService.error("There is an expected error! Try again later","ERROR")
      });
  }

  onPageChange(event: any): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadClients(this.pageIndex, this.pageSize);
  }
}
