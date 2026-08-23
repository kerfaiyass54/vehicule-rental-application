import { ConfirmStatus } from './confirm-status.enum';

export interface DemandResponse {

  idDemand: number;

  type: string;

  dateAsk: string;

  estimatedTime: number;

  status: ConfirmStatus;

  vehiculeName: string;

  repairName: string;

  ticketId: number;
}
