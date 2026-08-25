import { BuyStatus } from '../enums/buy-status';

export interface Buying {
  idBuying: number;
  dateBuy: string;
  periodBuy: number;
  buyStatus: BuyStatus;
  renew: boolean;
  vehiculeName: string;
  supplierName: string;
}
