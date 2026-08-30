import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  Output,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import Keycloak from 'keycloak-js';
import {ClientTicketService} from '../../../../services/client-services/client-ticket.service';
import {TicketType} from '../../../enums/ticket-type';
import {TicketInfo} from '../../../models/ticket-info.model';


@Component({
  selector: 'app-ticket-review',
  standalone: true,

  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],

  templateUrl: './ticket-review.html',
  styleUrl: './ticket-review.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TicketReview {

  // =========================================================
  // SERVICES
  // =========================================================

  private readonly ticketService =
    inject(ClientTicketService);

  private readonly keycloak =
    inject(Keycloak);

  private readonly snackBar =
    inject(MatSnackBar);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =========================================================
  // INPUTS
  // =========================================================

  @Input()
  vehiculeName: any = '';


  @Input()
  repairName: any = '';


  @Input()
  type: TicketType | null = null;


  @Input()
  decription = '';


  // =========================================================
  // OUTPUT
  // =========================================================

  @Output()
  ticketCreated =
    new EventEmitter<TicketInfo>();


  // =========================================================
  // STATE
  // =========================================================

  submitting = false;

  completed = false;

  createdTicket: TicketInfo | null = null;


  // =========================================================
  // CLIENT EMAIL
  // =========================================================

  getClientEmail(): string {

    return this.keycloak.tokenParsed?.['email'] ?? '';

  }


  // =========================================================
  // CONFIRM TICKET
  // =========================================================

  confirmTicket(): void {

    if (this.submitting) {
      return;
    }


    const clientEmail =
      this.getClientEmail();


    // ---------------------------------------------------------
    // VALIDATION
    // ---------------------------------------------------------

    if (!clientEmail) {

      this.showError(
        'Unable to retrieve your email address.'
      );

      return;

    }


    if (!this.vehiculeName) {

      this.showError(
        'Please select a vehicle.'
      );

      return;

    }


    if (!this.repairName) {

      this.showError(
        'Please select a repair.'
      );

      return;

    }


    if (!this.type) {

      this.showError(
        'Please select a ticket type.'
      );

      return;

    }


    if (!this.decription.trim()) {

      this.showError(
        'Please enter a description.'
      );

      return;

    }


    // ---------------------------------------------------------
    // CREATE REQUEST
    // ---------------------------------------------------------

    const ticket = {

      type: this.type,

      decription: this.decription.trim(),

      repairName: this.repairName,

      clientEmail: clientEmail,

      vehiculeName: this.vehiculeName

    };


    // ---------------------------------------------------------
    // SUBMIT
    // ---------------------------------------------------------

    this.submitting = true;

    this.cdr.markForCheck();


    this.ticketService
      .openTicket(ticket)
      .subscribe({

        next: createdTicket => {

          this.createdTicket =
            createdTicket;

          this.completed =
            true;

          this.submitting =
            false;


          this.snackBar.open(
            'Ticket created successfully.',
            'Close',
            {
              duration: 4000,
              horizontalPosition: 'right',
              verticalPosition: 'top'
            }
          );


          this.ticketCreated.emit(
            createdTicket
          );


          this.cdr.markForCheck();

        },


        error: error => {

          console.error(
            'Unable to create ticket:',
            error
          );


          this.submitting =
            false;


          this.snackBar.open(
            'Unable to create the ticket. Please try again.',
            'Close',
            {
              duration: 5000,
              horizontalPosition: 'right',
              verticalPosition: 'top'
            }
          );


          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // NOTIFICATION
  // =========================================================

  private showError(
    message: string
  ): void {

    this.snackBar.open(
      message,
      'Close',
      {
        duration: 4000,
        horizontalPosition: 'right',
        verticalPosition: 'top'
      }
    );

  }

}
