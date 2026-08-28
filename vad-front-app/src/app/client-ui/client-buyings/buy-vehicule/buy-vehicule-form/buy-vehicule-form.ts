import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Output
} from '@angular/core';

import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';


export interface RentalFormData {

  period: number;

  renew: boolean;

}


@Component({
  selector: 'app-buy-vehicule-form',

  standalone: true,

  imports: [
    ReactiveFormsModule
  ],

  templateUrl: './buy-vehicule-form.html',

  styleUrl: './buy-vehicule-form.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class BuyVehiculeForm {


  // =========================================================
  // OUTPUT
  // =========================================================

  @Output()
  formSubmitted =
    new EventEmitter<RentalFormData>();


  // =========================================================
  // FORM BUILDER
  // =========================================================

  private readonly fb =
    new FormBuilder();


  // =========================================================
  // FORM
  // =========================================================

  form =
    this.fb.group({

      period: [
        null,
        [
          Validators.required,
          Validators.min(1)
        ]
      ],

      renew: [
        false,
        Validators.required
      ]

    });


  // =========================================================
  // CONSTRUCTOR
  // =========================================================

  constructor() {

    this.form.valueChanges.subscribe(value => {

      if (
        value.period !== null &&
        value.period !== undefined
      ) {

        this.formSubmitted.emit({

          period: Number(value.period),

          renew: Boolean(value.renew)

        });

      }

    });

  }


  // =========================================================
  // GETTERS
  // =========================================================

  get period() {

    return this.form.get('period');

  }


  get renew() {

    return this.form.get('renew');

  }


  // =========================================================
  // VALIDATION
  // =========================================================

  isValid(): boolean {

    return this.form.valid;

  }


  // =========================================================
  // FORM VALUE
  // =========================================================

  getFormValue(): RentalFormData {

    const value =
      this.form.getRawValue();

    return {

      period: Number(value.period),

      renew: Boolean(value.renew)

    };

  }

}
