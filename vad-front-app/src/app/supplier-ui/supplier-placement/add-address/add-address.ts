import { Component, inject, OnInit } from '@angular/core';

import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule
} from '@angular/forms';

import { CommonModule } from '@angular/common';

import { Router } from '@angular/router';

import { MatStepperModule } from '@angular/material/stepper';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';

import Keycloak from 'keycloak-js';

import {
  SupplierDetailsService
} from '../../../services/supplier-details-service';



import {
  AddressCreation
} from '../../../models/Supplier-creation-dto.models';
import {LocationService} from '../../../services/location-service';


@Component({

  selector: 'app-add-address',

  standalone: true,

  imports: [

    CommonModule,
    ReactiveFormsModule,
    MatStepperModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatIconModule

  ],

  templateUrl: './add-address.html',

  styleUrl: './add-address.css'

})

export class AddAddress implements OnInit {

  step1Form!: FormGroup;

  step2Form!: FormGroup;

  email = '';

  protected keycloak = inject(Keycloak);


  // populated dynamically from DB
  countries: string[] = [];

  availableCities: string[] = [];


  constructor(

    private fb: FormBuilder,

    private supplierService: SupplierDetailsService,

    private locationService: LocationService,

    private router: Router

  ) {}


  ngOnInit(): void {

    // get supplier email from Keycloak token
    const token = this.keycloak.tokenParsed;

    if (token) {

      this.email = token['email'] ?? '';

    }


    // build stepper forms
    this.step1Form = this.fb.group({

      road: ['', Validators.required],

      number: ['', Validators.required]

    });


    this.step2Form = this.fb.group({

      country: ['', Validators.required],

      city: ['', Validators.required]

    });


    // load countries from backend
    this.loadCountries();


    // when country changes → load cities dynamically
    this.step2Form
      .get('country')
      ?.valueChanges
      .subscribe(country => {

        if (country) {

          this.loadCities(country);

        }

      });

  }


  // load countries from DB

  loadCountries() {

    this.locationService
      .getCountries()
      .subscribe({

        next: data => {

          // remove duplicates + sort alphabetically
          this.countries =
            [...new Set(data)].sort();

        },

        error: err =>
          console.error('Failed loading countries', err)

      });

  }


  // load cities based on selected country

  loadCities(country: string) {

    this.locationService
      .getCitiesByCountry(country)
      .subscribe({

        next: data => {

          this.availableCities =
            [...new Set(data)].sort();

          // reset selected city after country change
          this.step2Form
            .get('city')
            ?.reset();

        },

        error: err =>
          console.error('Failed loading cities', err)

      });

  }


  // submit address to backend

  submit() {

    const payload: AddressCreation = {

      road: this.step1Form.value.road,

      number: this.step1Form.value.number,

      location: this.step2Form.value.city

    };


    this.supplierService
      .addAddressNew(this.email, payload)
      .subscribe({

        next: () => {

          alert('✅ Address created successfully');

          this.router.navigate(['/supplier']);

        },

        error: err => {

          console.error(err);

          alert('❌ Address creation failed');

        }

      });

  }


  // cancel wizard

  cancel(stepper: any) {

    this.step1Form.reset();

    this.step2Form.reset();

    stepper.reset();

    this.router.navigate(['/supplier']);

  }

}
