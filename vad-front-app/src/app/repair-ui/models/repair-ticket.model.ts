import {RepairDemandStatus} from '../../client-ui/enums/repair-demand-status';
import {TicketType} from '../../client-ui/enums/ticket-type';


export interface RepairTicket {

  id: number;

  type: TicketType;

  date: string;

  status: RepairDemandStatus;

}
