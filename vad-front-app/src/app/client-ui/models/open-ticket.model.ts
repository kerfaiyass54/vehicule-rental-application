import { TicketType } from '../enums/ticket-type';

export interface OpenTicket {

  type: TicketType;

  decription: string;

  repairName: string;

  clientEmail: string;

  vehiculeName: string;

}
