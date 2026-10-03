import { RepairEstimateTask } from './repair-estimate-task.model';

export interface RepairEstimate {
  description: string;
  tasks: RepairEstimateTask[];
  totalEstimatedMinutes: number;
  dataset: string;
  datasetUrl: string;
}
