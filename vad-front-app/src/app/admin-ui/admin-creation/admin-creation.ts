import {
  ChangeDetectionStrategy,
  Component,
  inject
} from '@angular/core';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  Router
} from '@angular/router';


@Component({
  selector: 'app-admin-creation',
  standalone: true,

  imports: [
    MatIconModule
  ],

  templateUrl: './admin-creation.html',
  styleUrl: './admin-creation.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminCreation {

  private readonly router =
    inject(Router);


  // =========================================================
  // NAVIGATION
  // =========================================================

  goToClientCreation(): void {

    this.router.navigate([
      '/admin/clients/add'
    ]);

  }


  goToLocationCreation(): void {

    this.router.navigate([
      '/admin/locations/add'
    ]);

  }


  goToRepairCreation(): void {

    this.router.navigate([
      '/admin/repairs/add'
    ]);

  }


  goToSupplierCreation(): void {

    this.router.navigate([
      '/admin/suppliers/add'
    ]);

  }

}
