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

import Keycloak from 'keycloak-js';

import { SupplierDetailsService }
  from '../../../services/supplier-details-service';

import { CategoryCreation }
  from '../../../models/Supplier-creation-dto.models';


export enum CategoryName {

  PASSENGER_VEHICLES = 'PASSENGER_VEHICLES',
  COMMERCIAL_VEHICLES = 'COMMERCIAL_VEHICLES',
  MOTORCYCLES = 'MOTORCYCLES',
  ELECTRIC_VEHICLES = 'ELECTRIC_VEHICLES',
  OFF_ROAD_VEHICLES = 'OFF_ROAD_VEHICLES',
  HEAVY_DUTY_VEHICLES = 'HEAVY_DUTY_VEHICLES',
  EMERGENCY_VEHICLES = 'EMERGENCY_VEHICLES',
  AGRICULTURAL_VEHICLES = 'AGRICULTURAL_VEHICLES',
  MARINE_VEHICLES = 'MARINE_VEHICLES',
  AERIAL_VEHICLES = 'AERIAL_VEHICLES'

}


@Component({

  selector: 'app-add-category',

  standalone: true,

  imports: [

    CommonModule,
    ReactiveFormsModule,
    MatStepperModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule

  ],

  templateUrl: './add-category.html',

  styleUrl: './add-category.css'

})

export class AddCategory implements OnInit {

  categoryForm!: FormGroup;

  stockForm!: FormGroup;

  categoryNames = Object.values(CategoryName);

  existingCategories: string[] = [];

  email = '';

  protected keycloak = inject(Keycloak);


  constructor(

    private fb: FormBuilder,
    private router: Router,
    private supplierService: SupplierDetailsService

  ) {}


  ngOnInit(): void {

    const token = this.keycloak.tokenParsed;

    if (token) {

      this.email = token['email'] ?? '';

      this.loadExistingCategories();

    }


    this.categoryForm = this.fb.group({

      nameCategory: ['', Validators.required],
      typeCategory: ['', Validators.required]

    });


    this.stockForm = this.fb.group({

      stock: ['', Validators.required]

    });

  }


  loadExistingCategories() {

    this.supplierService
      .getCategoriesNames(this.email)
      .subscribe({

        next: data => this.existingCategories = data,

        error: err =>
          console.error('Failed loading categories list', err)

      });

  }


  submit() {

    const selectedCategory =
      this.categoryForm.value.nameCategory;


    if (this.existingCategories.includes(selectedCategory)) {

      alert('⚠️ Category already exists');

      return;

    }


    const payload: CategoryCreation = {

      nameCategory:
      this.categoryForm.value.nameCategory,

      typeCategory:
      this.categoryForm.value.typeCategory,

      stock:
      this.stockForm.value.stock

    };


    this.supplierService
      .addCategoryNew(this.email, payload)
      .subscribe({

        next: () => {

          alert('✅ Category created successfully');

          this.router.navigate(['/supplier']);

        },

        error: err => {

          console.error(err);

          alert('❌ Category creation failed');

        }

      });

  }


  cancel() {

    this.router.navigate(['/supplier']);

  }

}
