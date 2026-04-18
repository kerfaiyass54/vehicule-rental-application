import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';

import { CommonModule } from '@angular/common';

import { MatStepperModule } from '@angular/material/stepper';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';

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
    MatIconModule,
  ],
  templateUrl: './add-address.html',
  styleUrl: './add-address.css',
})
export class AddAddress {

  step1Form: FormGroup;
  step2Form: FormGroup;

  countries = ['France', 'Germany', 'USA'];

  cities: any = {
    France: ['Paris', 'Lyon', 'Marseille'],
    Germany: ['Berlin', 'Munich', 'Hamburg'],
    USA: ['New York', 'Chicago', 'Los Angeles'],
  };

  availableCities: string[] = [];

  constructor(private fb: FormBuilder) {

    this.step1Form = this.fb.group({
      road: ['', Validators.required],
      number: ['', Validators.required],
    });

    this.step2Form = this.fb.group({
      country: ['', Validators.required],
      city: ['', Validators.required],
    });

    this.step2Form.get('country')?.valueChanges.subscribe(country => {
      this.availableCities = this.cities[country] || [];
      this.step2Form.get('city')?.reset();
    });
  }

  submit() {

    const address = {
      ...this.step1Form.value,
      ...this.step2Form.value,
    };

    console.log('Saved address:', address);
  }

  cancel(stepper: any) {

    this.step1Form.reset();
    this.step2Form.reset();

    stepper.reset();

    console.log('Operation cancelled');
  }
}
