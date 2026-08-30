import {ConfirmStatus} from '../../client-ui/enums/confirm-status';
import {DemandType} from '../../client-ui/enums/demand-type';


export interface DemandDetails {

  id: number;

  type: DemandType;

  date: string;

  status: ConfirmStatus;

  estimatedTime: number;

  ticketId: number;

  supplierEmail: string;

  vehiculeName: string;

}
