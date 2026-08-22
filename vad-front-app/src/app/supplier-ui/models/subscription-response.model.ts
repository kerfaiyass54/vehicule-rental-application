import { SubscriptionType } from './subscription-type.enum';

export interface SubscriptionResponse {
  idSubscription: number;
  clientName: string;
  clientEmail: string;
  type: SubscriptionType;
  dateStart: string;
  price: number;
  reduce: number;
}
