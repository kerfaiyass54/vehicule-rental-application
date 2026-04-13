import { Component, OnInit, inject, ViewChild, AfterViewInit } from '@angular/core';
import Keycloak from 'keycloak-js';
import { SupplierDetailsService } from '../../../services/supplier-details-service';
import { MatDialogContent, MatDialogTitle } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import {
  MatCell, MatCellDef,
  MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef,
  MatRow, MatRowDef,
  MatTable,
  MatTableDataSource
} from '@angular/material/table';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-addresses-modal',
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
    MatRowDef,
    NgClass
  ],
  templateUrl: './addresses-modal.html',
  styleUrl: 'addresses-modal.css'
})
export class AddressesModal implements OnInit, AfterViewInit {

  keycloak = inject(Keycloak);

  addresses: any[] = [];

  dataSource = new MatTableDataSource<any>();

  displayedColumns = [
    'road',
    'number',
    'location'
  ];

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private supplierService: SupplierDetailsService) {}

  ngOnInit() {

    const email = this.keycloak.tokenParsed?.['email'];

    this.supplierService
      .getAddressesList(email, 20, 0)
      .subscribe(res => {

        this.addresses = res;
        this.dataSource.data = this.addresses;

      });
  }

  ngAfterViewInit() {

    this.dataSource.paginator = this.paginator;

  }
}
