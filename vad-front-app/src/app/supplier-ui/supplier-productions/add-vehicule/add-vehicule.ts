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


  ngOnInit() {
  }
}
