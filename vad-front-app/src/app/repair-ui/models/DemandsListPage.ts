import {DemandType} from '../../client-ui/enums/demand-type';
import {ConfirmStatus} from '../../client-ui/enums/confirm-status';

export interface DemandsListPage {

  id: number;

  type: DemandType;

  date: string;

  status: ConfirmStatus;

}
