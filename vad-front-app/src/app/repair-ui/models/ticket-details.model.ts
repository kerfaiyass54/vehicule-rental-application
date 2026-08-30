import {RepairDemandStatus} from '../../client-ui/enums/repair-demand-status';
import {TicketType} from '../../client-ui/enums/ticket-type';


export interface TicketDetailsModel {

  id: number;

  type: TicketType;

  description: string;

  date: string;

  status: RepairDemandStatus;

  tariff: number;

}
