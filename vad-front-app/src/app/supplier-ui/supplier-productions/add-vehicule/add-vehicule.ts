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

export class AddVehicule {

  identityForm: FormGroup;
  specsForm: FormGroup;
  statusForm: FormGroup;

  transmissions = Object.values(Transmission);
  statuses = Object.values(VehiculeStatus);


  constructor(
    private fb: FormBuilder,
    private router: Router
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


  submit() {

    const payload = {

      ...this.identityForm.value,
      ...this.specsForm.value,
      ...this.statusForm.value

    };

    console.log('Vehicle saved:', payload);

    /* redirect after save */

    this.router.navigate(['/vehicules']);

  }


  /* cancel navigation */

  cancel() {

    this.router.navigate(['/vehicules']);

  }

}
