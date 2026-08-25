import { SubscriptionType } from '../enums/subscription-type';

export interface SubscriptionInfo {
  idSubscrip: number;
  type: SubscriptionType;
  dateStart: string;
  reduce: number;
  price: number;
  supplierName: string;
  clientEmail: string;
}
