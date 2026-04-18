import { Component, OnInit, inject } from '@angular/core';

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

import { SupplierDetailsService } from '../../../services/supplier-details-service';
import { VehiculeCreation } from '../../../models/Supplier-creation-dto.models';

import Keycloak from 'keycloak-js';


enum Transmission {

  MANUAL = 'MANUAL',
  AUTOMATIC = 'AUTOMATIC'

}

enum VehiculeStatus {

  TAKEN = 'TAKEN',
  AVAILABLE = 'AVAILABLE',
  REPARATION = 'REPARATION'

}


@Component({

  selector: 'app-add-vehicule',

  standalone: true,

  imports: [

    CommonModule,
    ReactiveFormsModule,
    MatStepperModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule

  ],

  templateUrl: './add-vehicule.html',

  styleUrl: './add-vehicule.css'

})

export class AddVehicule implements OnInit {

  identityForm: FormGroup;
  specsForm: FormGroup;
  statusForm: FormGroup;

  transmissions = Object.values(Transmission);
  statuses = Object.values(VehiculeStatus);

  categories: string[] = [];
  existingVehiculeNames: string[] = [];

  email: string = '';

  protected keycloak = inject(Keycloak);


  constructor(
    private fb: FormBuilder,
    private router: Router,
    private supplierService: SupplierDetailsService
  ) {

    this.identityForm = this.fb.group({

      nameVehicule: ['', Validators.required],
      brand: ['', Validators.required],
      category: ['', Validators.required]

    });


    this.specsForm = this.fb.group({

      price: ['', Validators.required],
      highSpeed: ['', Validators.required],
      transmission: ['', Validators.required]

    });


    this.statusForm = this.fb.group({

      color: ['', Validators.required],
      vehiculeStatus: ['', Validators.required]

    });

  }


  ngOnInit(): void {

    const token = this.keycloak.tokenParsed;

    if (token) {

      this.email = token['email'] ?? '';

      this.loadValidationData();

    }

  }


  loadValidationData() {

    this.supplierService
      .getVehiculesNames(this.email)
      .subscribe(data => this.existingVehiculeNames = data);


    this.supplierService
      .getCategoriesNames(this.email)
      .subscribe(data => this.categories = data);

  }


  submit() {

    if (!this.email) {

      alert('Supplier email missing');

      return;

    }


    const vehicleName = this.identityForm.value.nameVehicule;


    if (this.existingVehiculeNames.includes(vehicleName)) {

      alert('Vehicle name already exists');

      return;

    }


    const payload: VehiculeCreation = {

      nameVehicule: vehicleName,
      brand: this.identityForm.value.brand,
      category: this.identityForm.value.category,

      price: this.specsForm.value.price,
      highSpeed: this.specsForm.value.highSpeed,
      transmission: this.specsForm.value.transmission,

      color: this.statusForm.value.color

    };


    this.supplierService
      .addVehiculeNew(this.email, payload)
      .subscribe({

        next: () => {

          alert('Vehicle created successfully');

          this.router.navigate(['/vehicules']);

        },

        error: err => {

          console.error(err);

          alert('Vehicle creation failed');

        }

      });

  }


  cancel() {

    this.router.navigate(['/vehicules']);

  }

}
