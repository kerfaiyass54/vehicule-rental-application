import {RepairStatus} from '../../client-ui/enums/repair-status';

export interface RepairInfoModel {

  idRepairInfo: number;

  vehiculeName: string;

  clientName: string;

  repairName: string;

  dateStart: string;

  repairStatus: RepairStatus;

}
