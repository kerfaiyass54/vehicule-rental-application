import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import Keycloak from 'keycloak-js';


@Component({
  selector: 'app-admin-details',
  standalone: true,

  imports: [
    CommonModule,
    FormsModule
  ],

  templateUrl: './admin-details.html',
  styleUrl: './admin-details.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminDetails implements OnInit {


  // =========================================================
  // DEPENDENCIES
  // =========================================================

  private readonly keycloak =
    inject(Keycloak);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =========================================================
  // ADMIN DATA
  // =========================================================

  adminName = '';

  adminEmail = '';

  newAdminName = '';


  // =========================================================
  // UI
  // =========================================================

  showNameDialog = false;

  successMessage = '';

  updating = false;


  // =========================================================
  // INIT
  // =========================================================

  ngOnInit(): void {

    this.loadAdmin();

  }


  // =========================================================
  // KEYCLOAK
  // =========================================================

  private loadAdmin(): void {

    const token =
      this.keycloak.tokenParsed;

    this.adminName =
      (token?.['name'] as string)
      || (token?.['preferred_username'] as string)
      || 'Administrator';

    this.adminEmail =
      (token?.['email'] as string)
      || '';

    this.cdr.markForCheck();

  }


  // =========================================================
  // OPEN DIALOG
  // =========================================================

  openNameDialog(): void {

    this.newAdminName =
      this.adminName;

    this.successMessage = '';

    this.showNameDialog = true;

    this.cdr.markForCheck();

  }


  // =========================================================
  // CLOSE DIALOG
  // =========================================================

  closeNameDialog(): void {

    if (this.updating) {

      return;

    }

    this.showNameDialog = false;

    this.newAdminName = '';

    this.cdr.markForCheck();

  }


  // =========================================================
  // UPDATE NAME
  // =========================================================

  updateName(): void {

    if (this.updating) {

      return;

    }

    const name =
      this.newAdminName.trim();


    if (!name || name.length < 2) {

      return;

    }


    this.updating = true;

    this.cdr.markForCheck();


    /*
     * The name is currently taken directly
     * from the authenticated Keycloak token.
     *
     * The actual persistence must be done through
     * your backend or Keycloak Account API.
     *
     * For now we update the local UI immediately.
     */

    this.adminName = name;

    this.updating = false;

    this.showNameDialog = false;

    this.newAdminName = '';

    this.successMessage =
      'Administrator name updated successfully.';

    this.cdr.markForCheck();

  }


  // =========================================================
  // BACKDROP
  // =========================================================

  onDialogBackdropClick(
    event: MouseEvent
  ): void {

    if (
      event.target ===
      event.currentTarget
    ) {

      this.closeNameDialog();

    }

  }


  // =========================================================
  // AVATAR
  // =========================================================

  getInitial(): string {

    if (!this.adminName) {

      return 'A';

    }

    return this.adminName
      .charAt(0)
      .toUpperCase();

  }

}
