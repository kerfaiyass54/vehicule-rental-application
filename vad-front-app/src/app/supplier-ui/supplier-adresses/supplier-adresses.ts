import { Component, OnInit, inject } from '@angular/core';
import Keycloak from 'keycloak-js';
import {SupplierDetailsService} from '../../services/supplier-details-service';

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

  constructor(private supplierDetailsService: SupplierDetailsService) {}

  async ngOnInit(): Promise<void> {

    await this.keycloak.updateToken(30);

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

}
