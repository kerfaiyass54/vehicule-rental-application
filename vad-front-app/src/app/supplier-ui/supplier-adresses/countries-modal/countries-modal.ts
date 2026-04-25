import { Component, OnInit, inject } from '@angular/core';
import Keycloak from 'keycloak-js';
import { SupplierDetailsService } from '../../../services/supplier-details-service';
import {MatDialogContent, MatDialogTitle} from '@angular/material/dialog';

@Component({
  selector: 'app-countries-modal',
  standalone: true,
  templateUrl: './countries-modal.html',
  imports: [
    MatDialogContent,
    MatDialogTitle
  ],
  styleUrls: ['./countries-modal.css']
})
export class CountriesModal implements OnInit {

  protected keycloak = inject(Keycloak);

  email = '';

  countries: string[] = [];

  flagMap: Record<string, string> = {

    Tunisia: '🇹🇳',
    France: '🇫🇷',
    Germany: '🇩🇪',
    Italy: '🇮🇹',
    Spain: '🇪🇸',
    USA: '🇺🇸',
    UK: '🇬🇧'
  };

  constructor(
    private supplierDetailsService: SupplierDetailsService
  ) {}

  ngOnInit(): void {

    const token = this.keycloak.tokenParsed;

    if (token) {

      this.email = token['email'];

      this.loadCountries();
    }
  }

  loadCountries() {

    this.supplierDetailsService
      .getCountriesList(this.email)
      .subscribe(data => {

        this.countries = data;
      });
  }

  getFlag(country: string) {

    return this.flagMap[country] ?? '🌍';
  }
}
