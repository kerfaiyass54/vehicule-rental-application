import { Component, OnInit, inject } from '@angular/core';
import Keycloak from 'keycloak-js';
import {SupplierDetailsService} from '../../services/supplier-details-service';
import {MatDialog} from '@angular/material/dialog';
import {CountriesModal} from './countries-modal/countries-modal';
import {LocationsModal} from './locations-modal/locations-modal';
import {AddressesModal} from './addresses-modal/addresses-modal';


@Component({
  selector: 'app-supplier-adresses',
  templateUrl: './supplier-adresses.html',
  styleUrls: ['./supplier-adresses.css']
})
export class SupplierAdresses implements OnInit {

  addressesCount = 0;
  countiesCount = 0;
  locationsCount = 0;

  email: any;
  protected keycloak = inject(Keycloak);

  constructor(
    private supplierDetailsService: SupplierDetailsService,
    private dialog: MatDialog
  ) {}
  ngOnInit(): void {

    const token = this.keycloak.tokenParsed;

    if (token) {
      this.email = token['email'] ?? '';
      this.loadDashboardData();
    }

  }

  loadDashboardData() {

    this.supplierDetailsService
      .getSupplierAddresses(this.email)
      .subscribe(data => this.addressesCount = data);

    this.supplierDetailsService
      .getSupplierCountries(this.email)
      .subscribe(data => this.countiesCount = data);

    this.supplierDetailsService
      .getSupplierLocations(this.email)
      .subscribe(data => this.locationsCount = data);

  }

  openAddressesModal() {
    this.dialog.open(AddressesModal, {
      width: '900px',
      maxWidth: '95vw',
      height: 'auto',
      panelClass: 'modern-dialog'
    });
  }

  openCountriesModal() {
    this.dialog.open(CountriesModal, {
      width: '900px',
      maxWidth: '95vw',
      height: 'auto',
      panelClass: 'modern-dialog'
    });
  }

  openLocationsModal() {
    this.dialog.open(LocationsModal, {
      width: '900px',
      maxWidth: '95vw',
      height: 'auto',
      panelClass: 'modern-dialog'
    });
  }

}
