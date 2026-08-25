export interface ClientDashboard {
  clientName: string;
  budget: number;
  totalBuyings: number;
  activeBuyings: number;
  totalTickets: number;
  pendingTickets: number;
  completedTickets: number;
  subscribed: boolean;
  subscriptionType: string;
}
