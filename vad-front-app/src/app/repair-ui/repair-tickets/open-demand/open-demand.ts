import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';

import { ActivatedRoute, Router } from '@angular/router';

import Keycloak from 'keycloak-js';

import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatFormFieldModule
} from '@angular/material/form-field';

import {
  MatInputModule
} from '@angular/material/input';

import {
  MatSelectModule
} from '@angular/material/select';

import {
  MatProgressSpinnerModule
} from '@angular/material/progress-spinner';

import {
  MatSnackBar,
  MatSnackBarModule
} from '@angular/material/snack-bar';

import {
  Subject,
  forkJoin,
  finalize,
  takeUntil
} from 'rxjs';
import {RepairTicketService} from '../../../services/repair-services/repair-ticket.service';
import {RepairDemandService} from '../../../services/repair-services/repair-demand.service';
import {DemandType} from '../../../client-ui/enums/demand-type';
import {TicketVehicule} from '../../models/ticket-vehicule.model';
import {TicketClient} from '../../models/ticket-client.model';
import {TicketDetailsModel} from '../../models/ticket-details.model';
import {CreateDemand} from '../../models/create-demand.model';




@Component({
  selector: 'app-open-demand',
  standalone: true,

  imports: [
    CommonModule,
    ReactiveFormsModule,

    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],

  templateUrl: './open-demand.html',
  styleUrl: './open-demand.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class OpenDemand implements OnInit, OnDestroy {

  // =========================================================
  // INJECT
  // =========================================================

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  private readonly keycloak =
    inject(Keycloak);

  private readonly ticketService =
    inject(RepairTicketService);

  private readonly demandService =
    inject(RepairDemandService);

  private readonly snackBar =
    inject(MatSnackBar);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly fb =
    inject(FormBuilder);


  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // STATE
  // =========================================================

  ticketId = 0;

  repairEmail = '';

  supplierEmail = '';

  vehiculeId = 0;


  ticket =
    null as TicketDetailsModel | null;

  client =
    null as TicketClient | null;

  vehicule =
    null as TicketVehicule | null;


  loading = true;

  submitting = false;

  error = false;


  readonly demandTypes = [
    DemandType.CONFIRMATION,
    DemandType.UPDATE
  ];


  // =========================================================
  // FORM
  // =========================================================

  readonly demandForm =
    this.fb.nonNullable.group({

      type: [
        DemandType.CONFIRMATION,
        Validators.required
      ],

      estimatedTime: [
        1,
        [
          Validators.required,
          Validators.min(1)
        ]
      ]

    });


  // =========================================================
  // INIT
  // =========================================================

  ngOnInit(): void {

    this.loadRepairEmail();

    this.loadTicketId();

    this.loadData();

  }


  // =========================================================
  // DESTROY
  // =========================================================

  ngOnDestroy(): void {

    this.destroy$.next();
    this.destroy$.complete();

  }


  // =========================================================
  // KEYCLOAK
  // =========================================================

  private loadRepairEmail(): void {

    const token =
      this.keycloak.tokenParsed;

    this.repairEmail =
      token?.['email'] ?? '';

    if (!this.repairEmail) {

      console.error(
        'Repair email could not be retrieved from Keycloak.'
      );

      this.error = true;
      this.loading = false;

      this.cdr.markForCheck();

    }

  }


  // =========================================================
  // ROUTE
  // =========================================================

  private loadTicketId(): void {

    const id =
      this.route.snapshot.paramMap.get('id');

    this.ticketId =
      Number(id);

    if (!this.ticketId || this.ticketId <= 0) {

      console.error(
        'Invalid ticket ID.'
      );

      this.error = true;
      this.loading = false;

      this.cdr.markForCheck();

    }

  }


  // =========================================================
  // LOAD DATA
  // =========================================================

  private loadData(): void {

    if (
      !this.ticketId ||
      !this.repairEmail
    ) {
      return;
    }


    this.loading = true;
    this.error = false;

    this.cdr.markForCheck();


    forkJoin({

      ticket:
        this.ticketService.getTicketInfo(
          this.ticketId
        ),

      client:
        this.ticketService.getClient(
          this.ticketId
        ),

      vehicule:
        this.ticketService.getVehicule(
          this.ticketId
        )

    })

      .pipe(

        takeUntil(this.destroy$),

        finalize(() => {

          this.loading = false;

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: response => {

          this.ticket =
            response.ticket;

          this.client =
            response.client;

          this.vehicule =
            response.vehicule;


          /*
           * IMPORTANT:
           *
           * TicketVehicule should contain vehiculeId.
           */

          this.vehiculeId =
            (response.vehicule as TicketVehicule & {
              vehiculeId?: number
            }).vehiculeId ?? 0;


          this.loadSupplierEmail();

        },

        error: error => {

          console.error(
            'Unable to load ticket information',
            error
          );

          this.error = true;

          this.ticket = null;
          this.client = null;
          this.vehicule = null;

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // SUPPLIER EMAIL
  // =========================================================

  private loadSupplierEmail(): void {

    if (!this.vehiculeId) {

      console.error(
        'Vehicle ID is missing.'
      );

      return;

    }


    this.demandService
      .getSupplierEmail(
        this.vehiculeId
      )

      .pipe(
        takeUntil(this.destroy$)
      )

      .subscribe({

        next: email => {

          this.supplierEmail =
            email;

          this.cdr.markForCheck();

        },

        error: error => {

          console.error(
            'Unable to retrieve supplier email',
            error
          );

          this.supplierEmail = '';

          this.showMessage(
            'Unable to retrieve the supplier email.'
          );

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // SUBMIT
  // =========================================================

  submitDemand(): void {

    if (
      this.demandForm.invalid ||
      this.submitting
    ) {

      this.demandForm.markAllAsTouched();

      return;

    }


    if (!this.ticketId) {

      this.showMessage(
        'Invalid ticket.'
      );

      return;

    }


    if (!this.repairEmail) {

      this.showMessage(
        'Repair email is missing.'
      );

      return;

    }


    if (!this.supplierEmail) {

      this.showMessage(
        'Supplier email is missing.'
      );

      return;

    }


    if (!this.vehiculeId) {

      this.showMessage(
        'Vehicle ID is missing.'
      );

      return;

    }


    const form =
      this.demandForm.getRawValue();


    const demand: CreateDemand = {

      ticketId:
      this.ticketId,

      repairEmail:
      this.repairEmail,

      supplierEmail:
      this.supplierEmail,

      type:
      form.type,

      estimatedTime:
      form.estimatedTime,

      vehiculeId:
      this.vehiculeId

    };


    console.log(
      'Creating demand:',
      demand
    );


    this.submitting = true;

    this.cdr.markForCheck();


    this.demandService
      .createDemand(demand)

      .pipe(

        takeUntil(this.destroy$),

        finalize(() => {

          this.submitting = false;

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: response => {

          console.log(
            'Demand created successfully',
            response
          );


          this.showMessage(
            'Repair demand created successfully.'
          );


          setTimeout(() => {

            this.router.navigate([
              '/repair/demands'
            ]);

          }, 800);

        },

        error: error => {

          console.error(
            'Unable to create repair demand',
            error
          );


          this.showMessage(
            'Unable to create the repair demand.'
          );

        }

      });

  }


  // =========================================================
  // CANCEL
  // =========================================================

  cancel(): void {

    this.router.navigate([
      '/repair-tickets',
      this.ticketId
    ]);

  }


  // =========================================================
  // SNACKBAR
  // =========================================================

  private showMessage(
    message: string
  ): void {

    this.snackBar.open(
      message,
      'Close',
      {
        duration: 3500,
        horizontalPosition: 'right',
        verticalPosition: 'top'
      }
    );

  }

}
