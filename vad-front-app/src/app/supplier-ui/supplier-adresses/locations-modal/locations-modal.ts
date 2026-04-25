import { Component, OnInit, inject, ViewChild, AfterViewInit } from '@angular/core';
import Keycloak from 'keycloak-js';
import { SupplierDetailsService } from '../../../services/supplier-details-service';
import { MatDialogContent, MatDialogTitle } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable,
  MatTableDataSource
} from '@angular/material/table';

@Component({
  selector: 'app-locations-modal',
  standalone: true,
  imports: [
    MatDialogContent,
    MatDialogTitle,
    MatPaginator,
    MatHeaderRow,
    MatRow,
    MatHeaderCell,
    MatCell,
    MatTable,
    MatColumnDef,
    MatHeaderCellDef,
    MatCellDef,
    MatHeaderRowDef,
    MatRowDef
  ],
  templateUrl: './locations-modal.html',
  styleUrl: 'locations-modal.css'
})
export class LocationsModal implements OnInit, AfterViewInit {

  keycloak = inject(Keycloak);

  locations: any[] = [];

  dataSource = new MatTableDataSource<any>();

  displayedColumns = [
    'idLoc',
    'name',
    'country'
  ];

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private supplierService: SupplierDetailsService) {}

  ngOnInit() {

    const email = this.keycloak.tokenParsed?.['email'];

    this.supplierService
      .getLocationsList(email, 20, 0)
      .subscribe(res => {

        this.locations = res;

        this.dataSource.data = this.locations;

      });
  }

  ngAfterViewInit() {

    this.dataSource.paginator = this.paginator;

  }
}
