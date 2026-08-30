import {
  ChangeDetectionStrategy,
  Component,
  Inject
} from '@angular/core';

import {
  CommonModule
} from '@angular/common';

import {
  MAT_DIALOG_DATA,
  MatDialogModule
} from '@angular/material/dialog';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  DemandDetails
} from '../../models/demand-details.model';


@Component({
  selector: 'app-demand-details-dialog',
  standalone: true,

  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule
  ],

  templateUrl: './demand-details-dialog.html',
  styleUrl: './demand-details-dialog.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DemandDetailsDialog {

  constructor(
    @Inject(MAT_DIALOG_DATA)
    public readonly demand: DemandDetails
  ) {}


  formatDate(
    date: string
  ): string {

    if (!date) {
      return '—';
    }

    return new Intl.DateTimeFormat(
      'en-GB',
      {
        day: '2-digit',
        month: 'short',
        year: 'numeric'
      }
    ).format(new Date(date));

  }


  formatTime(
    date: string
  ): string {

    if (!date) {
      return '—';
    }

    return new Intl.DateTimeFormat(
      'en-GB',
      {
        hour: '2-digit',
        minute: '2-digit'
      }
    ).format(new Date(date));

  }


  getStatusClass(): string {

    switch (
      String(this.demand.status).toUpperCase()
      ) {

      case 'APPROVED':
        return 'status-approved';

      case 'REFUSED':
        return 'status-refused';

      case 'PENDING':
        return 'status-pending';

      default:
        return 'status-default';

    }

  }


  getStatusIcon(): string {

    switch (
      String(this.demand.status).toUpperCase()
      ) {

      case 'APPROVED':
        return 'check_circle';

      case 'REFUSED':
        return 'cancel';

      case 'PENDING':
        return 'schedule';

      default:
        return 'help_outline';

    }

  }

}
