import {
  Component,
  EventEmitter,
  Output,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';

import {
  FormsModule
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
import {MatInputModule} from '@angular/material/input';
import {TicketType} from '../../../enums/ticket-type';




@Component({
  selector: 'app-ticket-form',

  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule
  ],

  templateUrl: './ticket-form.html',

  styleUrl: './ticket-form.css'
})
export class TicketForm {


  // =========================================================
  // OUTPUT
  // =========================================================

  @Output()
  formSubmitted =
    new EventEmitter<{
      type: TicketType;
      decription: string;
    }>();


  // =========================================================
  // STATE
  // =========================================================

  readonly selectedType =
    signal<TicketType | null>(null);

  readonly description =
    signal('');


  // =========================================================
  // TICKET TYPES
  // =========================================================

  readonly ticketTypes = [
    {
      value: TicketType.REPARATION,
      label: 'Reparation',
      icon: 'build'
    },
    {
      value: TicketType.MODIFICATION,
      label: 'Modification',
      icon: 'edit'
    }
  ];


  // =========================================================
  // SELECT TYPE
  // =========================================================

  selectType(
    type: TicketType
  ): void {

    this.selectedType.set(type);

  }


  // =========================================================
  // DESCRIPTION
  // =========================================================

  onDescriptionChange(
    event: Event
  ): void {

    const value =
      (event.target as HTMLTextAreaElement).value;

    this.description.set(value);

  }


  // =========================================================
  // VALIDATION
  // =========================================================

  isValid(): boolean {

    return (
      this.selectedType() !== null &&
      this.description().trim().length > 0
    );

  }


  // =========================================================
  // SUBMIT
  // =========================================================

  submit(): void {

    const type =
      this.selectedType();

    const description =
      this.description().trim();

    if (!type || !description) {
      return;
    }

    this.formSubmitted.emit({
      type,
      decription: description
    });

  }

  protected readonly TicketType = TicketType;
}
