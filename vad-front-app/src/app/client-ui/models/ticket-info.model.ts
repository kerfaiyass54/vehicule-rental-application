import { RepairDemandStatus } from '../enums/repair-demand-status';
import { TicketType } from '../enums/ticket-type';

export interface TicketInfo {
  idTicket: number;
  type: TicketType;
  description: string;
  dateInsert: string;
  status: RepairDemandStatus;
  tarif: number;
  repairName: string;
  clientName: string;
  vehiculeName: string;
}
