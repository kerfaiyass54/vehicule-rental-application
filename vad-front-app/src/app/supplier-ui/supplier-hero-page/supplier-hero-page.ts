import {Component, inject, OnInit} from '@angular/core';
import Keycloak from 'keycloak-js';
import {SupplierDetailsService} from '../../services/supplier-details-service';

@Component({
  selector: 'app-supplier-hero-page',
  templateUrl: './supplier-hero-page.html',
  styleUrls: ['./supplier-hero-page.css']
})
export class SupplierHeroPage implements OnInit {

  supplier: any = {};

  vehiclesCount = 0;
  categoriesCount = 0;
  placesCount = 0;

  email: any;
  protected keycloak        = inject(Keycloak);

  constructor(private supplierService: SupplierDetailsService) {}

  ngOnInit(): void {

    const token = this.keycloak.tokenParsed;
    if (token) {
      this.email = token['email'] ?? '';
    }

    this.supplierService
      .getSupplierDetails(this.email)
      .subscribe(data =>
    {
      this.supplier = data; console.log(data);
    });


    this.supplierService
      .getSupplierVehicules(this.email)
      .subscribe(count => this.vehiclesCount = count);


    this.supplierService
      .getSupplierCategories(this.email)
      .subscribe(count => this.categoriesCount = count);


    this.supplierService
      .getSupplierAddresses(this.email)
      .subscribe(count => this.placesCount = count);

  }

}
