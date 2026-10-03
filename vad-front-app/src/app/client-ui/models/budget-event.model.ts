export interface BudgetEvent {
  event_id: string;
  event_type: string;
  entity_type: string;
  entity_id: string;
  email: string;
  budget: number;
  previous_budget?: number | null;
  date: string;
}
