import { Component } from '@angular/core';

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

export class AddCategory {

  categoryForm: FormGroup;
  stockForm: FormGroup;

  categoryNames = Object.values(CategoryName);


  constructor(
    private fb: FormBuilder,
    private router: Router
  ) {

    this.categoryForm = this.fb.group({

      nameCategory: ['', Validators.required],
      typeCategory: ['', Validators.required]

    });


    this.stockForm = this.fb.group({

      stock: ['', Validators.required]

    });

  }


  submit() {

    const payload = {

      ...this.categoryForm.value,
      ...this.stockForm.value

    };

    console.log('Category created:', payload);

    this.router.navigate(['/supplier']);

  }


  cancel() {

    this.router.navigate(['/supplier']);

  }

}
