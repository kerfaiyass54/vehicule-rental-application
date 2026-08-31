import {DemandType} from '../../client-ui/enums/demand-type';


export interface CreateDemand {
  ticketId: number;
  repairEmail: string;
  supplierEmail: string;
  type: DemandType;
  estimatedTime: number;
  vehiculeId: number;
}
