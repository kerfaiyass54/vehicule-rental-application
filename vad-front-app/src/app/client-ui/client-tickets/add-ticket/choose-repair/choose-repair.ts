import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  OnInit,
  Output,
  inject,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatProgressSpinnerModule
} from '@angular/material/progress-spinner';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  MatFormFieldModule
} from '@angular/material/form-field';

import {
  MatInputModule
} from '@angular/material/input';



import {
  catchError,
  finalize,
  of
} from 'rxjs';
import {ClientTicketService} from '../../../../services/client-services/client-ticket.service';
import {Repair} from '../../../models/repair.model';


@Component({
  selector: 'app-choose-repair',

  standalone: true,

  imports: [
    CommonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule
  ],

  templateUrl: './choose-repair.html',

  styleUrl: './choose-repair.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ChooseRepair implements OnInit {


  // =========================================================
  // SERVICES
  // =========================================================

  private readonly ticketService =
    inject(ClientTicketService);

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =========================================================
  // OUTPUT
  // =========================================================

  @Output()
  repairSelected =
    new EventEmitter<string>();


  // =========================================================
  // STATE
  // =========================================================

  readonly repairs =
    signal<Repair[]>([]);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);

  readonly searchTerm =
    signal('');

  readonly selectedRepair =
    signal<Repair | null>(null);


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {

    this.loadRepairs();

  }


  // =========================================================
  // LOAD REPAIRS
  // =========================================================

  loadRepairs(): void {

    this.loading.set(true);

    this.error.set(false);


    this.ticketService
      .getRepairs(0, 50)

      .pipe(

        catchError(error => {

          console.error(
            'Unable to load repairs:',
            error
          );

          this.error.set(true);

          return of({
            content: [],
            totalElements: 0
          } as any);

        }),

        finalize(() => {

          this.loading.set(false);

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: response => {

          this.repairs.set(
            response.content ?? []
          );

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // FILTERED REPAIRS
  // =========================================================

  get filteredRepairs(): Repair[] {

    const search =
      this.searchTerm()
        .trim()
        .toLowerCase();


    if (!search) {

      return this.repairs();

    }


    return this.repairs().filter(
      repair => {

        const name =
          repair.nameRepair
            ?.toLowerCase() ?? '';

        const location =
          repair.locationName
            ?.toLowerCase() ?? '';

        const email =
          repair.email
            ?.toLowerCase() ?? '';


        return (
          name.includes(search) ||
          location.includes(search) ||
          email.includes(search)
        );

      }
    );

  }


  // =========================================================
  // SEARCH
  // =========================================================

  onSearch(
    event: Event
  ): void {

    const input =
      event.target as HTMLInputElement;

    this.searchTerm.set(
      input.value
    );

  }


  // =========================================================
  // SELECT REPAIR
  // =========================================================

  selectRepair(
    repair: Repair
  ): void {

    this.selectedRepair.set(
      repair
    );

    /*
     * IMPORTANT:
     * The parent only receives the repair name.
     */

    this.repairSelected.emit(
      repair.nameRepair
    );

    this.cdr.markForCheck();

  }


  // =========================================================
  // CHECK SELECTION
  // =========================================================

  isSelected(
    repair: Repair
  ): boolean {

    return (
      this.selectedRepair()?.idRepair ===
      repair.idRepair
    );

  }


  // =========================================================
  // RETRY
  // =========================================================

  retry(): void {

    if (this.loading()) {

      return;

    }

    this.loadRepairs();

  }

}
