import {
  ChangeDetectionStrategy,
  Component,
  inject
} from '@angular/core';

import {
  CommonModule
} from '@angular/common';

import {
  FormsModule
} from '@angular/forms';

import {
  Router
} from '@angular/router';

import {
  MatIconModule
} from '@angular/material/icon';


@Component({
  selector: 'app-add-client',
  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    MatIconModule
  ],

  templateUrl: './add-client.html',
  styleUrl: './add-client.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AddClient {

  private readonly router =
    inject(Router);


  // =========================================================
  // FORM DATA
  // =========================================================

  client = {

    nameClient: '',

    email: '',

    nationality: '',

    budget: 0,

    locationId: null as number | null,

    locationName: ''

  };


  // =========================================================
  // UI STATE
  // =========================================================

  submitted = false;


  // =========================================================
  // LOCATIONS
  // =========================================================

  /*
   * Replace this later with data coming from
   * LocationManagementService.
   */
  locations = [

    {
      id: 1,
      name: 'Tunis'
    },

    {
      id: 2,
      name: 'Paris'
    },

    {
      id: 3,
      name: 'Berlin'
    },

    {
      id: 4,
      name: 'Munich'
    },

    {
      id: 5,
      name: 'Madrid'
    }

  ];


  // =========================================================
  // LOCATION CHANGE
  // =========================================================

  onLocationChange(): void {

    const location =
      this.locations.find(
        item =>
          item.id === this.client.locationId
      );

    this.client.locationName =
      location?.name ?? '';

  }


  // =========================================================
  // SUBMIT
  // =========================================================

  createClient(): void {

    this.submitted = true;

    if (
      !this.client.nameClient.trim() ||
      !this.client.email.trim() ||
      !this.client.nationality.trim() ||
      this.client.budget < 0 ||
      !this.client.locationId
    ) {

      return;

    }


    /*
     * Your current backend ClientManagementController
     * does not expose a POST /api/v1/clients endpoint.
     *
     * Add the service call here once the backend
     * creation endpoint exists.
     */

    console.log(
      'Client to create:',
      this.client
    );

  }


  // =========================================================
  // CANCEL
  // =========================================================

  cancel(): void {

    this.router.navigate([
      '/admin/clients'
    ]);

  }


  // =========================================================
  // RESET
  // =========================================================

  reset(): void {

    this.client = {

      nameClient: '',

      email: '',

      nationality: '',

      budget: 0,

      locationId: null,

      locationName: ''

    };

    this.submitted = false;

  }

}
