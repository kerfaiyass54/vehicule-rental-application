import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
  ViewChild,
  inject,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';

import {
  MatStepper,
  MatStepperModule
} from '@angular/material/stepper';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatProgressSpinnerModule
} from '@angular/material/progress-spinner';

import {
  MatSnackBar,
  MatSnackBarModule
} from '@angular/material/snack-bar';

import {
  finalize
} from 'rxjs';

import Keycloak from 'keycloak-js';

import { ChooseVehicule } from './choose-vehicule/choose-vehicule';
import { ChooseRepair } from './choose-repair/choose-repair';
import { TicketForm } from './ticket-form/ticket-form';
import { TicketReview } from './ticket-review/ticket-review';

import { ClientTicketService } from '../../../services/client-services/client-ticket.service';

import { TicketType } from '../../enums/ticket-type';
import { OpenTicket } from '../../models/open-ticket.model';
import { TicketInfo } from '../../models/ticket-info.model';


@Component({
  selector: 'app-add-ticket',

  standalone: true,

  imports: [
    CommonModule,

    MatStepperModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,

    ChooseVehicule,
    ChooseRepair,
    TicketForm,
    TicketReview
  ],

  templateUrl: './add-ticket.html',

  styleUrl: './add-ticket.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AddTicket implements OnInit {


  // =========================================================
  // SERVICES
  // =========================================================

  private readonly keycloak =
    inject(Keycloak);

  private readonly ticketService =
    inject(ClientTicketService);

  private readonly snackBar =
    inject(MatSnackBar);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =========================================================
  // STEPPER
  // =========================================================

  @ViewChild('stepper')
  stepper!: MatStepper;


  // =========================================================
  // STATE
  // =========================================================

  readonly vehiculeName =
    signal<string | null>(null);

  readonly repairName =
    signal<string | null>(null);

  readonly type =
    signal<TicketType | null>(null);

  readonly decription =
    signal<string>('');

  readonly clientEmail =
    signal<string>('');

  readonly submitting =
    signal(false);

  readonly completed =
    signal(false);


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {

    const email =
      this.keycloak.tokenParsed?.['email'] ?? '';

    this.clientEmail.set(email);

  }


  // =========================================================
  // STEP 1
  // VEHICULE SELECTED
  // =========================================================

  onVehiculeSelected(
    name: string
  ): void {

    this.vehiculeName.set(name);

    // If vehicle changes, reset following steps
    this.repairName.set(null);
    this.type.set(null);
    this.decription.set('');

    this.cdr.markForCheck();

  }


  // =========================================================
  // STEP 2
  // REPAIR SELECTED
  // =========================================================

  onRepairSelected(
    name: string
  ): void {

    this.repairName.set(name);

    // Reset ticket form if repair changes
    this.type.set(null);
    this.decription.set('');

    this.cdr.markForCheck();

  }


  // =========================================================
  // STEP 3
  // TICKET FORM
  // =========================================================

  onTicketFormCompleted(
    data: {
      type: TicketType;
      decription: string;
    }
  ): void {

    this.type.set(data.type);

    this.decription.set(
      data.decription
    );

    this.cdr.markForCheck();

  }


  // =========================================================
  // STEP VALIDATION
  // =========================================================

  get vehiculeSelected(): boolean {

    return !!this.vehiculeName();

  }


  get repairSelected(): boolean {

    return !!this.repairName();

  }


  get ticketFormCompleted(): boolean {

    return (
      this.type() !== null &&
      this.decription().trim().length > 0
    );

  }


  get readyToSubmit(): boolean {

    return (
      this.vehiculeSelected &&
      this.repairSelected &&
      this.ticketFormCompleted &&
      !!this.clientEmail()
    );

  }


  // =========================================================
  // CONFIRM TICKET
  // =========================================================

  confirmTicket(): void {

    if (!this.readyToSubmit) {

      return;

    }

    if (this.submitting()) {

      return;

    }


    const ticket: OpenTicket = {

      type: this.type()!,

      decription:
        this.decription().trim(),

      repairName:
        this.repairName()!,

      clientEmail:
        this.clientEmail(),

      vehiculeName:
        this.vehiculeName()!

    };


    this.submitting.set(true);

    this.cdr.markForCheck();


    this.ticketService
      .openTicket(ticket)

      .pipe(

        finalize(() => {

          this.submitting.set(false);

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: (createdTicket: TicketInfo) => {

          this.completed.set(true);

          this.cdr.markForCheck();


          this.snackBar.open(
            'Ticket opened successfully.',
            'Close',
            {
              duration: 4000,
              horizontalPosition: 'right',
              verticalPosition: 'top',
              panelClass: ['success-snackbar']
            }
          );

        },


        error: error => {

          console.error(
            'Unable to open ticket:',
            error
          );


          this.snackBar.open(
            'Unable to open the ticket. Please try again.',
            'Close',
            {
              duration: 5000,
              horizontalPosition: 'right',
              verticalPosition: 'top',
              panelClass: ['error-snackbar']
            }
          );


          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // NAVIGATION
  // =========================================================

  previousStep(): void {

    if (this.stepper) {

      this.stepper.previous();

    }

  }


  nextStep(): void {

    if (this.stepper) {

      this.stepper.next();

    }

  }


  // =========================================================
  // START AGAIN
  // =========================================================

  reset(): void {

    this.vehiculeName.set(null);

    this.repairName.set(null);

    this.type.set(null);

    this.decription.set('');

    this.completed.set(false);

    this.cdr.markForCheck();


    if (this.stepper) {

      this.stepper.reset();

    }

  }

}
