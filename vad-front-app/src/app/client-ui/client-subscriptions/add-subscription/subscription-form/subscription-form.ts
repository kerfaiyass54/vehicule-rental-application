import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Output,
  inject,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';

import { SubscriptionType } from '../../../enums/subscription-type';


@Component({
  selector: 'app-subscription-form',

  standalone: true,

  imports: [
    CommonModule
  ],

  templateUrl: './subscription-form.html',

  styleUrl: './subscription-form.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SubscriptionForm {


  // =========================================================
  // SERVICES
  // =========================================================

  private readonly cdr =
    inject(ChangeDetectorRef);


  // =========================================================
  // OUTPUT
  // =========================================================

  @Output()
  subscriptionSelected =
    new EventEmitter<{
      type: SubscriptionType;
      price: number;
      reduction: number;
    }>();


  // =========================================================
  // STATE
  // =========================================================

  selectedType =
    signal<SubscriptionType | null>(null);


  // =========================================================
  // SUBSCRIPTION OPTIONS
  // =========================================================

  readonly subscriptionOptions = [

    {
      type: SubscriptionType.BASIC,
      price: 99,
      reduction: 5
    },

    {
      type: SubscriptionType.PREMIUM,
      price: 199,
      reduction: 15
    },

    {
      type: SubscriptionType.MONTHLY,
      price: 39,
      reduction: 10
    },

    {
      type: SubscriptionType.ANNUAL,
      price: 399,
      reduction: 25
    }

  ];


  // =========================================================
  // SELECT SUBSCRIPTION
  // =========================================================

  selectSubscription(
    type: SubscriptionType
  ): void {

    const option =
      this.subscriptionOptions.find(
        item => item.type === type
      );


    if (!option) {

      return;

    }


    this.selectedType.set(
      type
    );


    this.subscriptionSelected.emit({

      type: option.type,

      price: option.price,

      reduction: option.reduction

    });


    this.cdr.markForCheck();

  }


  // =========================================================
  // CHECK SELECTION
  // =========================================================

  isSelected(
    type: SubscriptionType
  ): boolean {

    return this.selectedType() === type;

  }


  // =========================================================
  // GET SELECTED OPTION
  // =========================================================

  get selectedOption() {

    const type =
      this.selectedType();


    if (!type) {

      return null;

    }


    return this.subscriptionOptions.find(
      option => option.type === type
    ) ?? null;

  }

}
