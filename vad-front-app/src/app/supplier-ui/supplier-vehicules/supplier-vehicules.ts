import {
  Component,
  ElementRef,
  ViewChild,
  inject,
  signal,
  OnInit
} from '@angular/core';

import Keycloak from 'keycloak-js';

import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule
} from '@angular/forms';


type VehiculeStatus =
  | 'AVAILABLE'
  | 'TAKEN'
  | 'REPARATION';

@Component({
  selector: 'app-supplier-vehicules',
  standalone: true,
  templateUrl: './supplier-vehicules.html',
  styleUrl: './supplier-vehicules.css',
  imports: [ReactiveFormsModule]
})
export class SupplierVehicules implements OnInit {


  ngOnInit() {
  }
}
