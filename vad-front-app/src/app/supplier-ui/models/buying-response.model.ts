import { BuyStatus } from './buy-status.enum';

export interface BuyingResponse {

  idBuying: number;

  vehiculeName: string;

  clientName: string;

  clientEmail: string;

  dateBuy: string;

  period: number;

  status: BuyStatus;
}
